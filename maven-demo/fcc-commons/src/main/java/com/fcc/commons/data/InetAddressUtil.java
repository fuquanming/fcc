package com.fcc.commons.data;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

public class InetAddressUtil {
	/**
	 * 获取本机MAC地址
	 * @return
	 * @throws SocketException
	 */
	public static String getLocalMac() {
		// 获取网卡，获取地址
		try {
			NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			if (network != null) {
				byte[] mac = network.getHardwareAddress();
				if (mac != null) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i],
								(i < mac.length - 1) ? "-" : ""));
					}
					return sb.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getLocalMac());
	}
}
