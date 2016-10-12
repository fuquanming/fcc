package com.fcc.commons.mail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.fcc.commons.utils.FileUtil;

/**
 * <p>Description:发送电子邮件封装类,完成发信功能(包括普通文本,HTML,带附件的电子邮件) </p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class MailMessage {
	
	private static Logger logger = Logger.getLogger(MailMessage.class.getName());
	/** 读取数据 */
	private Queue<String> readDataQueue = new ConcurrentLinkedQueue<String>();
	interface StatusCode {
		/** 服务就绪 220 */
		public String ServiceReady = "220";
		/** 所要求的邮件动作完成，可以继续邮件对话。250  */
		public String RequestedOK = "250";
	}
	/** SMTP服务器地址 */
	private String smtpServer;
	/** SMTP服务端口 */
	private int port = 25;
	/** 发送者邮件 */
	private String userName;
	/** 发送者邮件密码 */
	private String password;
	/** 邮件主题 */
	private String subject;
	/** 邮件内容 */
	private String content;
	
	private boolean html;
	/** 邮件附件 */
	private List<File> files;
	/** 是否认证，帐号密码信息 */
	private boolean auth = true;
	/** 请求数据的字符编码 */
	private String requestCharset = "GBK";
	
	private boolean logFlag = false;
	
	private DataOutputStream dos = null;
	private InputStream is = null;
	private InputStreamReader isr = null;
	private BufferedReader br = null;
	private Socket socket = null;
	// 查找返回结束标识
	private Pattern endPattern = Pattern.compile("^\\d{3} .*");
	
	private String to;// 收件人
	private String cc;// 抄送人
	private String bcc;// 暗送人
	private String splitFlag = ",";
	/**
	 * @param smtpServer	SMTP服务器地址
	 * @param userName		发送者邮箱
	 * @param password		发送者邮箱密码
	 */
	public MailMessage(String smtpServer, String userName, String password) {
		this(smtpServer, 25, userName, password);
	}
	/**
	 * @param smtpServer	SMTP服务器地址
	 * @param userName		发送者邮箱
	 * @param password		发送者邮箱密码
	 */
	public MailMessage(String smtpServer, String userName, String password, boolean auth) {
		this(smtpServer, 25, userName, password);
		this.auth = auth;
	}
	/**
	 * @param smtpServer	SMTP服务器地址
	 * @param port			SMTP服务器端口号
	 * @param userName		发送者邮箱
	 * @param password		发送者邮箱密码
	 */
	public MailMessage(String smtpServer, int port, String userName, String password) {
		this.smtpServer = smtpServer;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}
	/**
	 * 读取数据
	 * @throws IOException
	 */
	private void readData() throws IOException {
		String line = null;
//		while ((line = IOUtils.readLine(is, "utf-8")) != null) {
		while ((line = br.readLine()) != null) {
			readDataQueue.offer(line);
			if (logFlag) logger.info("MailMessage.readData():" + line);
			// 表示发送完成的标识 250 
			Matcher m = endPattern.matcher(line);
			if (m.find()) {
				return;
			}
		}
	}
	
	/** 发送数据 */
	private void writeData(String line) throws IOException {
		if (logFlag) logger.info("MailMessage.writeData():" + line);
		dos.write((line + "\r\n").getBytes());
	}
	
	public boolean sendMail(String to) throws IOException {
		return sendMail(to, null, null);
	}
	
	public boolean sendMail(String to, String cc) throws IOException {
		return sendMail(to, cc, null);
	}
	
	/**
	 * 发送邮件
	 * @param to	收件人	多人用;分隔
	 * @param cc	抄送		多人用;分隔
	 * @param bcc	暗送		多人用;分隔
	 * @return
	 */
	public boolean sendMail(String to, String cc, String bcc) throws IOException {
		try {
			this.to = to;
			this.cc = cc;
			this.bcc = bcc;
			boolean flag = connection();
			if (flag == false) return flag;
			
			flag = sendEHLO();
			if (flag == false) return flag;
			
			if (auth) {
				flag = sendLogin();
				if (flag == false) return flag;
			}
			
			flag = sendSender();
			if (flag == false) return flag;
			
			flag = sendRecipient();
			if (flag == false) return flag;
			
			flag = sendData();
			if (flag == false) return flag;
			
			flag = sendQuit();
			if (flag == false) return flag;
			
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(dos);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(socket);
		}
		return true;
	}
	/** 获取读取数据的最后一行 */
	private String getReadData() throws IOException {
		readData();
		String line = null;
		while (readDataQueue.size() > 0) {
			line = readDataQueue.poll();
		}
		return line;
	}
	
	/** 连接SMTP邮件服务器 */
	private boolean connection() throws IOException {
		try {
			socket = new Socket(smtpServer, port);
		} catch (IOException e) {
			logger.log(Level.WARNING, "MailMessage connection fail:" + smtpServer + ":" + port + "");
			return false;
		}
		dos = new DataOutputStream(socket.getOutputStream());
		is = socket.getInputStream();
		isr = new InputStreamReader(is, "UTF-8");
		br = new BufferedReader(isr);
		// 解析返回数据
		String line = getReadData();
		if (!line.startsWith(StatusCode.ServiceReady)) {
			logger.info("MailMessage connection Server fail:" + line);
			return false;
		}
		return true;
	}
	/** 发送 邮件服务器身份 */
	private boolean sendEHLO() throws IOException {
		// 发送 邮件服务器身份
		writeData("EHLO FuQuanming");
		// 解析返回数据
		String line = getReadData();
		if (!line.startsWith(StatusCode.RequestedOK)) {
			logger.info("MailMessage sendEHLO fail:" + line);
			return false;
		}
		return true;
	}
	/** 发送 登录请求 */
	private boolean sendLogin() throws IOException {
		// 发送 登录请求
		String writeLine = "AUTH LOGIN";
		writeData(writeLine);
		// 解析返回数据
		String line = getReadData();
		if (!line.startsWith("334")) {
			logger.info("MailMessage sendLogin Server fail:" + line);
			return false;
		}
		
		// 发送 登录名
		writeLine = base64Encode(userName.substring(0, userName.indexOf("@")));
		writeData(writeLine);
		
		line = getReadData();
		
		// 发送 密码
		writeLine = base64Encode(password);
		writeData(writeLine);
		
		line = getReadData();
		if (line == null || !line.startsWith("235")) {// 235 #2.0.0 OK Authenticated
			logger.info("MailMessage sendLogin fail:" + line);
			return false;
		}
		return true;
	}
	/** 发送 发件人 */
	private boolean sendSender() throws IOException {
		// 发送 发件人
		writeData("MAIL FROM:<" + userName + ">");
		
		String line = getReadData();
		if (line == null || !line.startsWith(StatusCode.RequestedOK)) {// 250 sender <qqwwee_0@sina.com> ok
			logger.info("MailMessage sendSender fail:" + line);
			return false;
		}
		return true;
	}
	/** 发送 收件人、抄送、暗送 */
	private boolean sendRecipient() throws IOException {
		// 发送 收件人、抄送、暗送
		String[] recipients = to.split(splitFlag);
		String writeLine = null;
		String line = null;
		for (String recipient : recipients) {
			writeLine = "RCPT TO:<" + recipient + ">";
			writeData(writeLine);
			line = getReadData();
			if (!line.startsWith(StatusCode.RequestedOK)) {// 250 sender <qqwwee_0@sina.com> ok
				logger.info("MailMessage sendRecipient fail:" + line);
				return false;
			}
		}
		if (cc != null && !"".equals(cc)) {
			recipients = cc.split(splitFlag);
			for (String recipient : recipients) {
				writeLine = "RCPT TO:<" + recipient + ">";
				writeData(writeLine);
				line = getReadData();
				if (!line.startsWith(StatusCode.RequestedOK)) {// 250 sender <qqwwee_0@sina.com> ok
					logger.info("MailMessage sendRecipient fail:" + line);
					return false;
				}
			}
		}
		if (bcc != null && !"".equals(bcc)) {
			recipients = bcc.split(splitFlag);
			for (String recipient : recipients) {
				writeLine = "RCPT TO:<" + recipient + ">";
				writeData(writeLine);
				line = getReadData();
				if (!line.startsWith(StatusCode.RequestedOK)) {// 250 sender <qqwwee_0@sina.com> ok
					logger.info("MailMessage sendRecipient fail:" + line);
					return false;
				}
			}
		}
		return true;
	}
	/** 发送 邮件 */
	private boolean sendData() throws IOException {
		// 发送 数据请求
		String writeLine = "DATA";
		writeData(writeLine);
		
		String line = getReadData();
		if (!line.contains("354")) {// 354 go ahead
			logger.info("MailMessage sendData fail:" + line);
			return false;
		}

		// 发送邮件
		String boundary = "=====FuQuanming122843802357_=====";
		String split = "--" + boundary;
		StringBuilder sb = new StringBuilder();
		String rn = "\r\n";
		SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
		sb.append("Date: ")
		.append(format.format(new Date()))
		.append(rn)
		.append("From: ").append(userName).append(rn)
		.append("To:");
		String[] tos = to.split(splitFlag);
		for (String to : tos) {
			sb.append(" ").append(to).append(",");
		}
		sb.delete(sb.length() - 1, sb.length());
		sb.append(rn);
		if (cc != null && !"".equals(cc)) {
			sb.append("Cc:");
			String[] ccs = cc.split(splitFlag);
			for (String c : ccs) {
				sb.append(" ").append(c).append(",");
			}
			sb.delete(sb.length() - 1, sb.length());
			sb.append(rn);
		}
		sb.append("Message-ID: <fuquanming@gmail.com>").append(rn)
		.append("Subject: =?").append(requestCharset).append("?B?").append(base64Encode(subject, requestCharset)).append("?=").append(rn)
		.append("X-mailer: FuQuanming [cn]").append(rn)
		.append("Mime-Version: 1.0").append(rn)
		.append("Content-Type: multipart/alternative;boundary=\"").append(boundary).append("\"").append(rn)
		.append(rn);
		if (content == null) {
			content = "";
			html = false;
		}
		sb.append(split).append(rn);
		if (html) {
			sb.append("Content-Type: text/html;charset=").append(requestCharset).append(rn);
		} else {
			sb.append("Content-Type: text/plain;charset=").append(requestCharset).append(rn);
		}
		sb.append("Content-Transfer-Encoding: base64").append(rn)
		.append(rn)
		.append(base64Encode(content, requestCharset))
		.append(rn)
		;
		writeData(sb.toString());
		if (files != null && files.size() > 0) {
			for (File file : files) {
				sb.delete(0, sb.length());
				sb.append(split).append(rn);
				String fileName = file.getName();
				String fileType = FileUtil.getFileContentType(fileName.substring(fileName.indexOf(".") + 1));
				if (fileType == null) fileType = "text/plain";
//				Base64 base64 = new Base64();
//				base64.
				String fileContent = base64Encode(IOUtils.toByteArray(new FileInputStream(file)));
				sb.append(split).append(rn)
				.append("Content-Type: ").append(fileType).append(";name=\"").append(fileName).append("\"").append(rn)
				.append("Content-Transfer-Encoding: base64").append(rn)
				.append("Content-Disposition: attachment;filename=\"").append(fileName).append("\"").append(rn)
				.append(rn)
				.append(fileContent).append(rn);
				// 后期添加文件数据量判断防止内存溢出
				writeData(sb.toString());
			}
		}
		sb.delete(0, sb.length());
		sb.append(split).append("--").append(rn)
		.append(rn)
		.append(".")
		;
		
		// 发送 邮件数据
		writeLine = sb.toString();
		writeData(writeLine);
		
		line = getReadData();
		if (line == null || !line.contains(StatusCode.RequestedOK)) {//250 ok:  Message 1006441088 accepted
			logger.info("MailMessage sendData fail:" + line);
			return false;
		}
		return true;
	}
	/** 发送 退出 */
	private boolean sendQuit() throws IOException {
		// 发送 退出
		writeData("QUIT");
		getReadData();
		return true;
	}
	
	private String base64Encode(String str) {
	    Base64 base64 = new Base64();
	    String val = null;
	    try {
	        val = base64.encode(str).toString();
        } catch (EncoderException e) {
            e.printStackTrace();
        }
	    return val;
	}
	
	private String base64Encode(String str, String charset) {
        Base64 base64 = new Base64();
        String val = null;
        try {
            val = base64.encode(str.getBytes(charset)).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return val;
    }
	
	private String base64Encode(byte[] bytes) {
        Base64 base64 = new Base64();
        return base64.encode(bytes).toString();
    }
	
	/**
	 * 邮件主题
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * 邮件内容
	 * @param text
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 邮件附件
	 * @param files
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}
	/**
	 * 设置SMTP服务端口号
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isLogFlag() {
		return logFlag;
	}
	public void setLogFlag(boolean logFlag) {
		this.logFlag = logFlag;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}
	public String getRequestCharset() {
		return requestCharset;
	}
	public void setRequestCharset(String requestCharset) {
		this.requestCharset = requestCharset;
	}
}