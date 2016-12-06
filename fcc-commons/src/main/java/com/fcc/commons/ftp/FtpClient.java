package com.fcc.commons.ftp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

/**
 * <p>Description:FTP客户端</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class FtpClient {
	
	private Logger logger = Logger.getLogger(FtpClient.class.getName());
	/**
	 * FTP 返回代码
	 */
	interface FtpReturnCode {
//		110  新文件指示器上的重启标记 
//		120 服务器准备就绪的时间（分钟数） 
//		125 打开数据连接，开始传输 
//		150 打开连接 
//		200 成功 
//		202 命令没有执行 
//		211 系统状态回复 
//		212 目录状态回复 
//		213 文件状态回复 
//		214 帮助信息回复 
//		215 系统类型回复 
//		220 服务就绪 
//		221 退出网络 
//		225 打开数据连接 
//		226 结束数据连接 
//		227 进入被动模式（IP 地址、ID 端口） 
//		230 登录因特网 
//		250 文件行为完成 
//		257 路径名建立 
//		331 要求密码 
//		332 要求帐号 
//		350 文件行为暂停 
//		421 服务关闭 
//		425 无法打开数据连接 
//		426 结束连接 
//		450 文件不可用 
//		451 遇到本地错误 
//		452 磁盘空间不足 
//		500 无效命令 
//		501 错误参数 
//		502 命令没有执行 
//		503 错误指令序列 
//		504 无效命令参数 
//		530 未登录网络 
//		532 存储文件需要帐号 
//		550 文件不可用 
//		551 不知道的页类型 
//		552 超过存储分配 
//		553 文件名不允许
		/** 220 服务就绪 建立连接时 */
		public static String SERVER_OK = "220";
		/** 331 要求密码 发送:USER 123 */
		public static String NEED_PASSWORD = "331";
		/** 230 登录因特网 发送:PASS 123 */
		public static String LOGIN_OK = "230";
		/** 215 系统类型回复 发送:SYST */
		public static String SYS_TYPE = "215";
		/** 211 系统状态回复 发送:FEAT */
		public static String SYS_STATUS = "211";
		/** 257 显示当前路径 发送:PWD */
		public static String CURRENT_DIRECTORY = "257";
		/** 250 文件行为完成 发送:CWD */
		public static String DIRECTORY_CHANGED = "250";
		/** 200 成功 例如 发送:TYPE I */
		public static String SUCCESS = "200";
		/** 227 进入被动模式（IP 地址、ID 端口） 发送:PASV */
		public static String ENTERING_PASSIVE_MODE = "227";
		/** 150 打开连接 发送:LIST -al */
		public static String OPENING_CONNECTION = "150";
		/** 226 结束数据连接 发送:RETR 11 */
		public static String CLOSE_CONNECTION = "226";
		/** 350 文件行为暂停 发送:REST 73728 */
		public static String FILE_PAUSE = "350";
	}

	/** 读取命令返回数据队列 */
	private Queue<String> readCommandQueue = new ConcurrentLinkedQueue<String>();
	
	// 查找返回结束标识
	private Pattern endPattern = Pattern.compile("^\\d{3} .*");
	
	/** 根目录时间格式化 */
	private SimpleDateFormat rootFormatYear = new SimpleDateFormat("MMM d yyyy", Locale.US);
	private SimpleDateFormat rootFormatTime = new SimpleDateFormat("MMM d HH:mm yyyy", Locale.US);
	
	/** 解析数据端口 */
	private Pattern dataPortPattern = Pattern.compile(".*,.*,.*,.*,(.*),(\\d{0,3})\\)");
	/** 解析当前请求的目录 */
	private Pattern directoryPattern = Pattern.compile(".*\"(.*)\".*");
	
	/** 根路径 */
	private String rootDirectory = "/";
	/** 当前路径 */
	private String currentDirectory = rootDirectory;
	/** 命令连接 */
	private Socket commandSocket;
	private DataOutputStream commandDos;
	private InputStream commandIs;
	private InputStreamReader commandIsr;
	private BufferedReader commandBr;
	/** 请求数据字符编码 */
	private String charset = "GBK";
	/** 数据连接 */
	private Socket dataSocket;
	private DataOutputStream dataDos;
	private InputStream dataIs;
	
	private String host;
	private int port = 21;
	
	
	/** 返回的文件列表-文件 */
	private List<FtpFile> fileList = new ArrayList<FtpFile>();
	/** 返回的文件列表-文件夹 */
	private List<FtpFile> directoryFileList = new ArrayList<FtpFile>();
	
	private List<String> tempList = new ArrayList<String>();
	
	private boolean logFlag = false;
	
	public FtpClient() {
	}
	
	/** 发送命令数据 */
	private void writeCommandData(String data) throws IOException {
		if (logFlag) logger.info("wCommandData:" + data);
		commandDos.write((data + "\r\n").getBytes(charset));
		commandDos.flush();
	}
	/** 读取命令返回数据 */
	private void readCommandData() throws IOException {
		String line = null;
		while ((line = commandBr.readLine()) != null) {
			if (logFlag)logger.info("rCommandData:" + line);
			readCommandQueue.offer(line);
			// 表示发送完成的标识 250 
			Matcher m = endPattern.matcher(line);
			if (m.find()) {
				return;
			}
		}
	}
	/** 获取命令返回数据的最后一行 */
	private String getReadCommandData() throws IOException {
		readCommandData();
		String line = null;
		while (readCommandQueue.size() > 0) {
			line = readCommandQueue.poll();
		}
		return line;
	}
	/**
	 * 匹配 指定指令
	 * @param command 
	 * @return 
	 * @throws IOException
	 */
	private boolean checkReadCommand(String line, String command) throws IOException {
		if (line == null || !line.startsWith(command)) {
			return false;
		}
		return true;
	}
	
	public boolean connection(String host, String userName, String password) throws IOException {
		return connection(host, port, userName, password);
	}
	
	public boolean connection(String host, int port, String userName, String password) throws IOException {
		try {
			this.host = host;
			commandSocket = new Socket(host, port);
		} catch (IOException e) {
			logger.info("connection fail:" + host + ":" + port);
			return false;
		}
		commandDos = new DataOutputStream(commandSocket.getOutputStream());
		commandIs = commandSocket.getInputStream();
		commandIsr = new InputStreamReader(commandIs);
		commandBr = new BufferedReader(commandIsr);
		// 建立连接 解析返回数据
		String line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.SERVER_OK) == false) {
			logger.info("connection Server fail:" + line);
			return false;
		}
		// 发送 登录帐号
		writeCommandData("USER " + userName);
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.NEED_PASSWORD) == false) {
			logger.info("login user fail:" + line);
			return false;
		}
		// 发送 密码
		writeCommandData("PASS " + password);
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.LOGIN_OK) == false) {
			logger.info("login pass fail:" + line);
			return false;
		}
		// 报告远程系统的操作系统类型
		writeCommandData("SYST");
		line = getReadCommandData();
		// 系统状态
		writeCommandData("FEAT");
		line = getReadCommandData();
		
		// 显示服务器端的当前工作目录名
		writeCommandData("PWD");
		line = getReadCommandData();
		
		Matcher directoryMatcher = directoryPattern.matcher(line);
		if (directoryMatcher.find()) rootDirectory = directoryMatcher.group(1);
		// 改变远程系统的工作目录
		writeCommandData("CWD " + rootDirectory);
		line = getReadCommandData();
		// TYPE 指定文件类型，参数可以是A、E、I、L只有TYPE A和TYPE I常用 I:设置二进制流数据传输。A:ASCII码
		writeCommandData("TYPE I");
		line = getReadCommandData();
		// PASV 指定服务器数据传输过程监听等待客户端的数据连接连接建立请求
		writeCommandData("PASV");
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.ENTERING_PASSIVE_MODE) == false) {
			logger.info("connection lose:" + line);
			return false;
		}
		// 解析数据端口信息
		Matcher m = dataPortPattern.matcher(line);
		if (m.find()) {
			int p1 = Integer.valueOf(m.group(1));
			int p2 = Integer.valueOf(m.group(2));
			int dataPort = p1 * 256 + p2;
			openDataMonitor(host, dataPort);
			// 发送文件列表
			writeCommandData("LIST -al");
			line = getReadCommandData();
			if (checkReadCommand(line, FtpReturnCode.OPENING_CONNECTION) == false) {
				logger.info("connection lose:" + line);
				return false;
			}
			decodeDirectoryList(directoryFileList, fileList);
			closeDataMonitor();
			line = getReadCommandData();
			return true;
		}
		return false;
	}
	/**
	 * 打开数据监听连接
	 * @param host
	 * @param dataPort
	 * @throws Exception
	 */
	private void openDataMonitor(String host, int dataPort) throws IOException {
		dataSocket = new Socket(host, dataPort);
		dataIs = dataSocket.getInputStream();
		dataDos = new DataOutputStream(dataSocket.getOutputStream());
		if (logFlag) logger.info("connDataPort:" + host + ":" + dataPort);
	}
	/**
	 * 关闭数据监听连接
	 */
	private void closeDataMonitor() {
		IOUtils.closeQuietly(dataDos);
		IOUtils.closeQuietly(dataIs);
		IOUtils.closeQuietly(dataSocket);
	}
	/**
	 * 建立目录
	 * @param path		访问的目录
	 * @throws IOException
	 */
	public void buildPath(String path) throws IOException {
		String rootPath = rootDirectory;
		if (rootDirectory.length() > 1 && !rootDirectory.substring(rootDirectory.length() - 1).equals("/")) {
			rootPath = rootDirectory + "/";
		}
		openPath(rootPath);
		if (path.substring(0, 1).equals("/")) path = path.substring(1);
		List<FtpFile> ftpFileList = getDirectoryFileList();
		String[] uploadPathParam = path.split("\\/");
		int pathParamLen = uploadPathParam.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pathParamLen; i++) {
			sb.append(uploadPathParam[i]).append("/");
			boolean findFlag = false;
			for (FtpFile ftpFile : ftpFileList) {
				if (ftpFile.getFileName().equals(uploadPathParam[i])) {
					findFlag = true;
					break;
				}
			}
			if (findFlag == false) {
				mkDir(rootPath + sb.toString());
			}
			openPath(rootPath + sb.toString());
			ftpFileList = getDirectoryFileList();
		}
	}
	
	/**
	 * 选择路径打开
	 * 例如：/驱动/4800扫描仪/Acrobat/Acroread
	 * @param path
	 */
	public boolean openPath(String path) throws IOException {
		return openPath(path, directoryFileList, fileList);
	}
	
	/**
	 * 选择路径打开
	 * 例如：/驱动/4800扫描仪/Acrobat/Acroread
	 * @param path
	 */
	public boolean openPath(String path, List<FtpFile> directoryFileList, List<FtpFile> fileList) throws IOException {
		fileList.clear();
		directoryFileList.clear();
		String line = null;
		writeCommandData("CWD " + path);
		line = getReadCommandData();
		if (line.contains("550")) {// 不存在该文件夹
			return false;
		}
		writeCommandData("PWD");
		line = getReadCommandData();
		Matcher directoryMatcher = directoryPattern.matcher(line);
		if (directoryMatcher.find()) currentDirectory = directoryMatcher.group(1);
		
		
		// PASV 指定服务器数据传输过程监听等待客户端的数据连接连接建立请求
		writeCommandData("PASV");
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.ENTERING_PASSIVE_MODE) == false) {
			logger.info("connection lose:" + line);
		}
		// 解析数据端口信息
		Matcher m1 = dataPortPattern.matcher(line);
		if (m1.find()) {
			int p1 = Integer.valueOf(m1.group(1));
			int p2 = Integer.valueOf(m1.group(2));
			int dataPort = p1 * 256 + p2;
			openDataMonitor(host, dataPort);
			// drw-rw-rw-   1 user     group           0 Jan  3  2012 .
			writeCommandData("LIST -al");
			line = getReadCommandData();
			if (checkReadCommand(line, FtpReturnCode.OPENING_CONNECTION)) {
				decodeDirectoryList(directoryFileList, fileList);
			}
			closeDataMonitor();
			getReadCommandData();
		}
		return true;
	}
	
	/**
	 * 创建文件夹
	 * @param directoryName	文件夹名称
	 * @return
	 * @throws IOException
	 */
	public boolean mkDir(String directoryName) throws IOException {
//		[右] MKD 文件夹1
//		[右] 257 "/文件夹1" directory created.
		String line = null;
		writeCommandData("MKD " + directoryName);
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.CURRENT_DIRECTORY) == false) {
			logger.info("connection lose:" + line);
			return false;
		}
		return true;
	}
	
	/**
	 * 重命名文件或文件夹
	 * @param directoryName	文件夹名称
	 * @return
	 * @throws IOException
	 */
	public boolean rename(String oldName, String newName) throws IOException {
//		[右] RNFR 文件名
//		[右] 350 Ready for RNTO.
//		[右] RNTO 新文件名
//		[右] 250 Rename successful	
		String line = null;
		writeCommandData("RNFR " + oldName);
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.FILE_PAUSE) == false) {
			logger.info("connection lose:" + line);
			return false;
		}
		writeCommandData("RNTO " + newName);
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.DIRECTORY_CHANGED) == false) {
			logger.info("connection lose:" + line);
			return false;
		}
		return true;
	} 
	
	/**
	 * 下载 文件/文件夹
	 * /驱动/4800扫描仪/Acrobat/Acroread/Reader.pdf <br/>
	 * /驱动/4800扫描仪/Acrobat/Acroread/ <br/>
	 * /驱动/4800扫描仪/Acrobat/Acroread <br/>
	 * Reader.pdf /Reader.pdf 文件 <br/>
	 * Acroread /Acroread 文件夹 <br/>
	 * @param filePath		Ftp 文件路径
	 * @param saveFilePath	保存路径
	 * @param append		true 追加，false 覆盖
	 * @throws IOException
	 */
	public void down(String filePath, String saveFilePath, boolean append) throws IOException {
		// 定位上一级路径
		String fileName = "";
		String path = "";
		int flagIndex = filePath.lastIndexOf("/");
		if (flagIndex != -1) {
			if (flagIndex == 0) {
				// 输入 /Reader.pdf 文件 /Acroread 文件夹
				path = "/";
				fileName = filePath.replace("/", "");
			} else {
				// 输入 /驱动/4800扫描仪/Acrobat/Acroread/
				// /驱动/4800扫描仪/Acrobat/Acroread/Reader.pdf
				// /驱动/4800扫描仪/Acrobat/Acroread
				String[] params = filePath.split("/");
				fileName = params[params.length - 1];
				StringBuilder sb = new StringBuilder();
				int length = params.length;
				for (int i = 0; i < length - 1; i++) {
					if (!params[i].equals(""))
						sb.append("/").append(params[i]);
				}
				path = sb.toString();
			}
		}
		
		
		List<FtpFile> directoryFileList = new ArrayList<FtpFile>();
		List<FtpFile> fileList = new ArrayList<FtpFile>();
		openPath(path, directoryFileList, fileList);
		
		// 输入 文件名或文件夹 未判断 文件夹和文件名重复 
		boolean flag = false;
		for (FtpFile ftpFile : directoryFileList) {
			if (ftpFile.getFileName().equals(fileName)) {
				// 建立本地文件夹
				File file = new File(saveFilePath + File.separatorChar + fileName);
				file.mkdirs();
				flag = true;
				break;
			}
		}
		if (flag == false) {
			for (FtpFile ftpFile : fileList) {
				if (ftpFile.getFileName().equals(fileName)) {
					downFile(fileName, saveFilePath, append);
					break;
				}
			}
		} else {
			openPath(currentDirectory + "/" + fileName, directoryFileList, fileList);
			String currentDir = currentDirectory;
			for (FtpFile data : fileList) {
				downFile(data.getFileName(), saveFilePath + File.separatorChar + fileName, append);
			}
			for (FtpFile data : directoryFileList) {
				down(currentDir + "/" + data.getFileName(), saveFilePath + File.separatorChar + fileName, append);
			}
		}
	}
	
	/**
	 * 下载单个文件
	 * 例如下载：	/驱动/4800扫描仪/Acrobat/Acroread/Reader.pdf
	 * 
	 * openPath("/驱动/4800扫描仪/Acrobat/Acroread/")
	 * downFile("Reader.pdf", "d:\\down\\", true);
	 * 
	 * @param fileName		下载文件名 Reader.pdf
	 * @param saveFilePath	保存的路径 d:\\down\\
	 * @param append		true 追加，false 覆盖
	 * @throws IOException
	 */
	private boolean downFile(String fileName, String saveFilePath, boolean append) throws IOException {
//		[右] TYPE I
//		[右] 200 Type set to I.
//		[右] PASV
//		[右] 227 Entering Passive Mode (61,187,64,227,12,217)
//		[右] 正在打开数据连接 IP: 61.187.64.227 端口: 3289
//		[右] RETR TRACK10-英雄.MP3
//		[右] 150 Opening BINARY mode data connection for TRACK10-英雄.MP3 (3374184 Bytes).
//		[右] 226 Transfer complete.
		String line = null;
		writeCommandData("TYPE I");
		getReadCommandData();
		writeCommandData("PASV");
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.ENTERING_PASSIVE_MODE) == false) {
			logger.info("connection lose:" + line);
			return false;
		}
		// 解析数据端口信息
		Matcher m1 = dataPortPattern.matcher(line);
		if (m1.find()) {
			int p1 = Integer.valueOf(m1.group(1));
			int p2 = Integer.valueOf(m1.group(2));
			int dataPort = p1 * 256 + p2;
			openDataMonitor(host, dataPort);
			// drw-rw-rw-   1 user     group           0 Jan  3  2012 .
			writeCommandData("RETR " + fileName);// 下载文件
			line = getReadCommandData();
			if (checkReadCommand(line, FtpReturnCode.OPENING_CONNECTION) == false) {
				logger.info("connection lose:" + line);
				return false;
			}
			// 取得文件总大小
			Matcher fileSizeMatcher = Pattern.compile(".*\\((.*) bytes\\).*", Pattern.CASE_INSENSITIVE).matcher(line);
			long fileLength = 0;
			if (fileSizeMatcher.find()) {
				fileLength = Long.valueOf(fileSizeMatcher.group(1));
			}
			
			File oldFile = new File(saveFilePath + File.separatorChar + fileName);
			RandomAccessFile raf = null;
			byte[] bytes = new byte[8192];
			try {
				int size = -1;
				raf = new RandomAccessFile(oldFile, "rw");// 读写方式
				if (append == true && oldFile.exists()) {
					long oldFileLength = oldFile.length();
					if (oldFileLength < fileLength) {// 小于下载文件
						dataIs.skip(oldFileLength);
						raf.seek(oldFileLength);// 指到文件尾部
					}
				}
				while ((size = dataIs.read(bytes)) != -1) {
					raf.write(bytes, 0, size);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(raf);
				bytes = null;
			}
			closeDataMonitor();
		}
		
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.CLOSE_CONNECTION) == false) {
			logger.info("connection lose:" + line);
			return false;
		}
		return false;
	}
	
	/**
	 * 上传文件夹或文件
	 * @param uploadFile
	 * @param ftpFilePath
	 * @param append
	 * @return
	 * @throws IOException
	 */
	public boolean upload(File uploadFile, String ftpFilePath, boolean append) throws IOException {
		if (uploadFile == null) {
			logger.info("uploadFile is null");
			return false;
		}
		String fileName = uploadFile.getName();
		if (uploadFile.exists() == false) {
			logger.info("uploadFile is not exist:" + uploadFile.getParent() + fileName);
			return false;
		}
		openPath(ftpFilePath);
		if (uploadFile.isDirectory()) {
			// 是否存在 该文件夹
			boolean exist = false;
			for (FtpFile ftpFile : directoryFileList) {
				if (ftpFile.getFileName().equals(fileName)) {
					exist = true;
					break;
				}
			}
			if (exist == false) {
				if (mkDir(fileName) == false) {
					return false;
				}
			}
			File[] files = uploadFile.listFiles();
			for (File file : files) {
				upload(file, ftpFilePath + "/" + fileName, append);
			}
		} else if (uploadFile.isFile()) {
			// 是否存在 该文件
			return uploadFile(uploadFile, ftpFilePath, append);
		}
		return true;
	}
	
	/**
	 * 上传单个文件
	 * 例如上传文件：H:\tempImg\AsianFontPacks\Readme.wri
	 * 到：/驱动/Microtek 4800扫描仪/Acrobat/Acroread/
	 * 
	 * uploadFile(new File("d:\\gatewayinterface 应用.war"), "/", true)
	 * 
	 * @param uploadFile	上传的文件
	 * @param ftpFilePath	FTP 服务器上传的位置 该目录存在
	 * @param append		true 追加，false 覆盖
	 * @return
	 * @throws IOException
	 */
	private boolean uploadFile(File uploadFile, String ftpFilePath, boolean append) throws IOException {
//		[右] TYPE I
//		[右] 200 Type set to I.
//		[右] PASV
//		[右] 227 Entering Passive Mode (61,187,64,227,12,152)
//		[右] 正在打开数据连接 IP: 61.187.64.227 端口: 3224
//		[右] STOR temp.txt
//		[右] 150 Opening ASCII mode data connection for temp.txt.
//		[右] 226 Transfer complete. 5 bytes transferred. 0.15 KB/sec.
		if (uploadFile == null || uploadFile.isDirectory()) return false;
		// 切换路径
		openPath(ftpFilePath);
		
		// 判断服务器上是否有该文件
		String fileName = uploadFile.getName();
		long serverFileLength = -1;// 服务器上该资源文件大小
		long uploadFileLength = uploadFile.length();// 上传文件大小
		for (FtpFile ftpFile : fileList) {
			if (ftpFile.getFileName().equals(fileName)) {
				serverFileLength = ftpFile.getFileSize();
				break;
			}
		}
		if (serverFileLength >= uploadFileLength) {
			logger.info("FTP Server exist file:" + currentDirectory + "/" + fileName + ", uploadFile return false");
			return false;
		}
		
		String line = null;
		writeCommandData("TYPE I");
		getReadCommandData();
		writeCommandData("PASV");
		line = getReadCommandData();
		if (checkReadCommand(line, FtpReturnCode.ENTERING_PASSIVE_MODE) == false) {
			logger.info("connection lose:" + line);
			return false;
		}
		// 解析数据端口信息
		Matcher m1 = dataPortPattern.matcher(line);
		if (m1.find()) {
			int p1 = Integer.valueOf(m1.group(1));
			int p2 = Integer.valueOf(m1.group(2));
			int dataPort = p1 * 256 + p2;
			openDataMonitor(host, dataPort);
			if (serverFileLength != -1) {
				writeCommandData("REST " + serverFileLength);// *REST 从服务器的一个标识处重新开始传输
				line = getReadCommandData();
				if (checkReadCommand(line, FtpReturnCode.FILE_PAUSE) == false) {
					logger.info("connection lose:" + line);
					return false;
				}
			}
			writeCommandData("STOR " + uploadFile.getName());// 上载一个文件到服务器上，若文件已经存在则覆盖
			line = getReadCommandData();
			if (checkReadCommand(line, FtpReturnCode.OPENING_CONNECTION) == false) {
				logger.info("connection lose:" + line);
				return false;
			}
			
			RandomAccessFile raf = null;
			try {
				byte[] bytes = new byte[8192];
				int size = -1;
				raf = new RandomAccessFile(uploadFile, "rw");// 读写方式
				if (append == true && serverFileLength != -1) {
					raf.seek(serverFileLength);// 跳过服务器文件大小
				}
				while ((size = raf.read(bytes)) != -1) {
					dataDos.write(bytes, 0, size);
				}
				raf.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(raf);
			}
			closeDataMonitor();
		}
		
		line = getReadCommandData();
		if (line != null && line.startsWith(FtpReturnCode.CLOSE_CONNECTION)) {
			return true;
		}
		return false;
	}
	
	public boolean delFile(String filePath, String fileName) throws IOException {
		String line = null;
		openPath(rootDirectory + "/" + filePath);
		for (FtpFile ftpFile : fileList) {
			if (ftpFile.getFileName().equals(fileName)) {
				writeCommandData("DELE " + fileName);
				line = getReadCommandData();
				if (checkReadCommand(line, FtpReturnCode.DIRECTORY_CHANGED) == false) {
					logger.info("connection lose:" + line);
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 解析Ftp目录列表，数据存储在 fileList, directoryList
	 */
	private void decodeDirectoryList(List<FtpFile> directoryFileList, List<FtpFile> fileList) throws IOException {
		String dataLine = null;
		try {
			StringBuilder sb = new StringBuilder();
			char[] dataChar = null;
			int length = 0;
			// drw-rw-rw-   1 user     group           0 Jan  3  2012 .
			while ((dataLine = commandBr.readLine()) != null) {
				tempList.clear();
				dataChar = dataLine.toCharArray();
				length = dataChar.length;
				for (int i = 0; i < length; i++) {
					char data = dataChar[i];
					if (data != ' ') {
						sb.append(data);
					} else {
						String tempSb = sb.toString();
						int tempLen =tempSb.length();
						if (tempLen > 0) {
							tempList.add(tempSb);
							sb.delete(0, tempLen);
						}
					}
				}
				String tempSb = sb.toString();
				int tempLen =tempSb.length();
				if (tempLen > 0) {
					tempList.add(tempSb);
					sb.delete(0, tempLen);
				}
				tempLen = tempList.size();
				FtpFile ftpFile = new FtpFile();
				ftpFile.setProperty(tempList.get(0));
				ftpFile.setFileSize(Integer.valueOf(tempList.get(4)));
				String month = tempList.get(5);
				String day = tempList.get(6);
				String timeOrYear = tempList.get(7);
				
				try {
					if (timeOrYear.contains(":")) {// 小时:分
						ftpFile.setModifyDate(rootFormatTime.parse(month + " " + day + " " + timeOrYear + " " + Calendar.getInstance().get(Calendar.YEAR)));
					} else {
						ftpFile.setModifyDate(rootFormatYear.parse(month + " " + day + " " + timeOrYear));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// 判断文件是否有空格
				String fileName = tempList.get(8);
				if (tempList.size() > 9) {
					for (int i = 9; i < tempList.size(); i++) {
						fileName = fileName + " " + tempList.get(i);
					}
				}
				ftpFile.setFileName(fileName);
				if (!".".equals(fileName) && !"..".equals(fileName)) {
					if (ftpFile.isDirectory()) {
						directoryFileList.add(ftpFile);
					} else {
						fileList.add(ftpFile);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			writeCommandData("QUIT");
			getReadCommandData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		IOUtils.closeQuietly(commandDos);
		IOUtils.closeQuietly(commandIs);
		IOUtils.closeQuietly(commandIsr);
		IOUtils.closeQuietly(commandBr);
		IOUtils.closeQuietly(commandSocket);
		IOUtils.closeQuietly(dataDos);
		IOUtils.closeQuietly(dataIs);
		IOUtils.closeQuietly(dataSocket);
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isLogFlag() {
		return logFlag;
	}
	public void setLogFlag(boolean logFlag) {
		this.logFlag = logFlag;
	}
	public List<FtpFile> getFileList() {
		return fileList;
	}
	public List<FtpFile> getDirectoryFileList() {
		return directoryFileList;
	}
	public String getRootDirectory() {
		return rootDirectory;
	}
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
	public String getCurrentDirectory() {
		return currentDirectory;
	}
	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

}