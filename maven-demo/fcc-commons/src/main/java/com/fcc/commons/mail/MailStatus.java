package com.fcc.commons.mail;
/**
 * <p>Description:邮件发送、接收状态</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public enum MailStatus {
	/** 建立连接 */
	SEND_CONNECTION_REQUEST,
	/** 发送 邮件服务器身份 */
	SEND_EHLO_REQUEST,
	/** 发送 登录请求 */
	SEND_LOGIN_REQUEST,
	/** 发送 登录名 */
	SEND_LOGIN_NAME_REQUEST,
	/** 发送 密码 */
	SEND_PASSWORD_REQUEST,
	/** 发送 发件人 */
	SEND_SENDER_REQUEST,
	/** 发送 收件人、抄送、暗送 */
	SEND_ADDRESSEE_REQUEST,
	/** 发送 邮件数据请求 */
	SEND_DATA_REQUEST,
	/** 发送 退出请求 */
	SEND_QUIT_REQUEST
}
