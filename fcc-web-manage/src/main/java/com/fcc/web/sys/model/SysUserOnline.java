//package com.fcc.web.sys.model;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//import org.apache.shiro.session.mgt.OnlineSession;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
//import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.annotations.Type;
//import org.springframework.format.annotation.DateTimeFormat;
//
///**
// * 当前在线会话
// * @author 傅泉明
// * @version v1.0
// * @email fuquanming@gmail.com
// */
//@Entity
//@Table(name = "SYS_RBAC_USER_ONLINE")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//public class SysUserOnline {
//
//    /**
//     * 用户会话id ===> uid
//     */
//    @Id
//    @GeneratedValue(generator = "assigned")
//    @GenericGenerator(name = "assigned", strategy = "assigned")
//    private String id;
//
//    //当前登录的用户Id
//    @Column(name = "USER_ID")
//    private String userId;
//
//    @Column(name = "USER_NAME")
//    private String username;
//
//    /**
//     * 用户主机地址
//     */
//    @Column(name = "HOST")
//    private String host;
//
//    /**
//     * 用户登录时系统IP
//     */
//    @Column(name = "SYSTEM_HOST")
//    private String systemHost;
//
//    /**
//     * 用户浏览器类型
//     */
//    @Column(name = "USER_AGENT")
//    private String userAgent;
//
//    /**
//     * 在线状态
//     */
//    @Column(name = "STATUS")
//    @Enumerated(EnumType.STRING)
//    private OnlineSession.OnlineStatus status = OnlineSession.OnlineStatus.on_line;
//
//    /**
//     * session创建时间
//     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "START_TIMESTAMP")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date startTimestamp;
//    /**
//     * session最后访问时间
//     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "LAST_ACCESS_TIME")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastAccessTime;
//
//    /**
//     * 超时时间
//     */
//    @Column(name = "TIMEOUT")
//    private Long timeout;
//
//
//    /**
//     * 备份的当前用户会话
//     */
//    @Column(name = "session")
//    @Type(type = "com.fcc.commons.web.support.hibernate.type.ObjectSerializeUserType")
//    private OnlineSession session;
//
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public Date getStartTimestamp() {
//        return startTimestamp;
//    }
//
//    public void setStartTimestamp(Date startTimestamp) {
//        this.startTimestamp = startTimestamp;
//    }
//
//    public Date getLastAccessTime() {
//        return lastAccessTime;
//    }
//
//    public void setLastAccessTime(Date lastAccessTime) {
//        this.lastAccessTime = lastAccessTime;
//    }
//
//
//    public Long getTimeout() {
//        return timeout;
//    }
//
//    public void setTimeout(Long timeout) {
//        this.timeout = timeout;
//    }
//
//    public String getHost() {
//        return host;
//    }
//
//    public void setHost(String host) {
//        this.host = host;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getUserAgent() {
//        return userAgent;
//    }
//
//    public void setUserAgent(String userAgent) {
//        this.userAgent = userAgent;
//    }
//
//    public OnlineSession.OnlineStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(OnlineSession.OnlineStatus status) {
//        this.status = status;
//    }
//
//    public OnlineSession getSession() {
//        return session;
//    }
//
//    public void setSession(OnlineSession session) {
//        this.session = session;
//    }
//
//    public String getSystemHost() {
//        return systemHost;
//    }
//
//    public void setSystemHost(String systemHost) {
//        this.systemHost = systemHost;
//    }
//
//
//    public static final SysUserOnline fromOnlineSession(OnlineSession session) {
//        SysUserOnline online = new SysUserOnline();
//        online.setId(String.valueOf(session.getId()));
//        online.setUserId(session.getUserId());
//        online.setUsername(session.getUsername());
//        online.setStartTimestamp(session.getStartTimestamp());
//        online.setLastAccessTime(session.getLastAccessTime());
//        online.setTimeout(session.getTimeout());
//        online.setHost(session.getHost());
//        online.setUserAgent(session.getUserAgent());
//        online.setSystemHost(session.getSystemHost());
//        online.setSession(session);
//
//        return online;
//    }
//
//
//}
