/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


/**
 * <p>Title:ZipUtil2.java</p>
 * <p>Package:com.fcc.commons.zip</p>
 * <p>Description:文件夹压缩，支持win和linux,依赖ant-1.6.5.jar</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class Zip {
	
	/**
	 * 为多个文件生成zip文件
	 * 但解压文件可以后的文件可以正常显示
	 * @param zipName	生成文件的文件名
	 * @param zipPath	生成文件的文件路径
	 * @param filePath	要压缩的文件
	 */
	public synchronized void zipFile(String zipName, String zipPath, List<String> filePath) {		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			File f = new File(zipPath);
			if (!f.exists()) {
				f.mkdirs();
			}
			fos = new FileOutputStream(zipPath + File.separatorChar + zipName);
			zos = new ZipOutputStream(fos);//压缩包
			zos.setEncoding("GBK");//解决linux乱码
			int size = filePath.size();
			for (int i = 0; i < size; i++) {
				write(zos, (String)filePath.get(i), "");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(zos);
			IOUtils.closeQuietly(fos);
		}
	}
	/**
	 * 写压缩流
	 * @param zos	压缩输出流
	 * @param path	压缩路径
	 * @param base	压缩式的基础目录
	 * @throws IOException
	 */
	private void write(ZipOutputStream zos, String path, String base)
			throws IOException {
		File file = new File(path);
		if (file.isDirectory()) { //文件夹，递归
			base = (base.length() == 0 ? file.getName() : base) + File.separator;
			File[] tempFiles = file.listFiles();
			int size = tempFiles.length;
			for (int i = 0; i < size; i++) {
				write(zos, tempFiles[i].getPath(), base
						+ tempFiles[i].getName());
			}
		} else {// 文件，压缩
			if (file.isHidden()) {// 隐藏文件不保存
				return;
			}
			byte[] buff = new byte[2048];
			int len = -1;
			if (base == null || "".equals(base)) {
				base = file.getName();
			}
			ZipEntry entry = new ZipEntry(base);//这是压缩包名里的文件名
			entry.setUnixMode(755);//解决linux乱码
			InputStream in = null;
			try {
				zos.putNextEntry(entry);//写入新的 ZIP 文件条目并将流定位到条目数据的开始处
				in = new BufferedInputStream(new FileInputStream(file));//打开需要添加的文件
				while ((len = in.read(buff)) != -1) {
					zos.write(buff, 0, len);
				}
				zos.flush();
			} catch (IOException e) {
				throw e;
			} finally {
				if (in  != null) {
					in.close();
					in = null;
				}
			}
			entry = null;
		}
	}

	/**
     * 解压zip格式的压缩文件到指定位置
     * @param zipFileName 压缩文件
     * @param extPlace 解压目录
     * @throws Exception
     */
	public synchronized void unZipFile(String zipFileName, String extPlace) throws Exception {
		File file = new File(zipFileName);
		if (!file.exists() || file.length() <= 0) {
			throw new Exception("要解压的文件不存在！");
		}
		File extPlaceFile = new File(extPlace);
		extPlaceFile.mkdirs();//创建目录
		ZipFile zipFile = null;
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			zipFile = new ZipFile(zipFileName);//取得压缩文件
			java.util.Enumeration<ZipEntry> e = zipFile.getEntries();
			ZipEntry ze;
			String fileName;
			String strPath = extPlaceFile.getAbsolutePath();
			while (e.hasMoreElements()) {
				ze = e.nextElement();//取得每一个压缩文件中的一个ZipEntry
				fileName = ze.getName();
				write(zipFile, ze, strPath + File.separator + fileName);
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}
	}
	
	private void write(ZipFile zip, ZipEntry entry, String file) 
			throws IOException {
		if (entry.isDirectory()) {// 文件夹
			File f = new File(file);
			f.mkdirs();
		} else {
			File f = new File(file);
			File tempFile = new File(f.getParentFile().getPath());// 创建目录
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buffer = new byte[8196];
			InputStream is = null;
			int len;
			try {
				is = zip.getInputStream(entry);
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.flush();			
			} catch (IOException e) {
				throw e;
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(fos);
			}
		}
	}
	
	public static void main(String argv[]) throws Exception {
//		long startTime = System.currentTimeMillis();
//		System.out.println("start");
//		List list = new ArrayList();
//		list.add("F:\\temp\\神仙道懒娃伴侣免费版V2.25.rar");
//		list.add("G:\\Jpcap简易教程 - - ITeye技术网站_files\\");
//		ZipUtil2.zipFile("我的文件.zip", "F:\\temp\\", list);
//		long endTime = System.currentTimeMillis();
//		System.out.println("end=" + (endTime - startTime));
		new Zip().unZipFile("F:\\nginx.zip", "F:\\temp1\\");
	}
}
