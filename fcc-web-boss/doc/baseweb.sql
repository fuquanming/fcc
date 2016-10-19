/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50532
Source Host           : localhost:3306
Source Database       : baseweb

Target Server Type    : MYSQL
Target Server Version : 50532
File Encoding         : 65001

Date: 2013-10-08 14:43:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_type
-- ----------------------------
DROP TABLE IF EXISTS sys_type;
CREATE TABLE sys_type (
  TYPE_ID varchar(32) NOT NULL COMMENT '主键',
  TYPE_NAME varchar(255) DEFAULT NULL COMMENT '名称',
  TYPE_CODE varchar(255) DEFAULT NULL COMMENT '代码',
  TYPE_LEVEL int(11) NOT NULL COMMENT '级别',
  TYPE_SORT int(11) NOT NULL COMMENT '排序',
  TYPE_DESC varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (TYPE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据类型';

-- ----------------------------
-- Table structure for index_info
-- ----------------------------
DROP TABLE IF EXISTS index_info;
CREATE TABLE index_info (
  INFO_ID varchar(32) NOT NULL,
  INFO_KEY varchar(100) DEFAULT NULL COMMENT '该索引标识区分该索引在索引工厂里',
  INDEX_DIR varchar(255) DEFAULT NULL COMMENT '索引目录',
  DOCUMENT_TYPE varchar(100) DEFAULT NULL COMMENT '该索引标识区分该索引在索引工厂里',
  CLASS_NAME varchar(100) DEFAULT NULL COMMENT '索引的类',
  ID_TYPE varchar(100) DEFAULT NULL COMMENT 'ID类型,Long, Integer, String',
  METHOD_NAMES varchar(200) DEFAULT NULL COMMENT '指定类那些方法要添加到索引中',
  IS_TOKENIZEDS varchar(200) DEFAULT NULL COMMENT '指定类那些方法的内容是要被分词',
  IS_BACKUP_FIELDS varchar(200) DEFAULT NULL COMMENT '指定类那些方法的内容是要保留一份完整的数据',
  FIELD_NAMES varchar(200) DEFAULT NULL COMMENT '索引中有哪些字段',
  DEL_FIELD_NAME varchar(10) DEFAULT NULL COMMENT '删除索引制定字段',
  ANALYZER_NAME varchar(50) DEFAULT NULL COMMENT '分词器',
  CREATE_TIME datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (INFO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of index_info
-- ----------------------------
INSERT INTO index_info VALUES ('402881e540b037e20140b039bce40000', 'SysLog', 'g:\\\\lucene\\\\SysLog', 'SysLog', 'com.fcc.app.sys.model.SysLog', 'String', 'getLogId,getUserId,getUserName,getIpAddress', 'false,false,true,false', 'true,true,true,true', 'LogId,UserId,UserName,IpAddress', 'LogId', '', '2013-08-24 19:33:17');

-- ----------------------------
-- Table structure for index_task
-- ----------------------------
DROP TABLE IF EXISTS index_task;
CREATE TABLE index_task (
  ID varchar(32) NOT NULL,
  OPERATION varchar(255) NOT NULL COMMENT '操作类型',
  DOCUMENT_ID varchar(255) NOT NULL COMMENT '关联文档ID',
  DOCUMENT_TYPE varchar(100) NOT NULL COMMENT '文档类型',
  CREATE_TIME datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of index_task
-- ----------------------------

-- ----------------------------
-- Table structure for sys_lock
-- ----------------------------
DROP TABLE IF EXISTS sys_lock;
CREATE TABLE sys_lock (
  LOCK_KEY varchar(200) NOT NULL COMMENT '锁的key',
  LOCK_STATUS varchar(10) DEFAULT NULL COMMENT '当前锁状态，lock，unlock',
  CREATE_TIME datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (LOCK_KEY)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_lock
-- ----------------------------
INSERT INTO sys_lock VALUES ('lock_index_flag', 'unlock', '2013-10-02 21:48:40');
INSERT INTO sys_lock VALUES ('Mykey1', 'unlock', '2013-10-08 10:15:15');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS sys_log;
CREATE TABLE sys_log (
  LOG_ID bigint NOT NULL auto_increment COMMENT '键主',
  USER_ID varchar(20) DEFAULT NULL COMMENT '用户ID',
  USER_NAME varchar(20) DEFAULT NULL COMMENT '用户名称',
  IP_ADDRESS varchar(100) DEFAULT NULL COMMENT 'IP地址',
  LOG_TIME datetime DEFAULT NULL COMMENT '日志时间',
  MODULE_NAME varchar(255) DEFAULT NULL COMMENT '模块名称',
  OPERATE_NAME varchar(20) DEFAULT NULL COMMENT '操作名称',
  EVENT_PARAM varchar(2000) DEFAULT NULL COMMENT '事件关联ID',
  EVENT_OBJECT varchar(4000) DEFAULT NULL COMMENT '事件对象',
  EVENT_RESULT varchar(20) DEFAULT NULL COMMENT '事件结果',
  PRIMARY KEY (LOG_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sys_organ
-- ----------------------------
DROP TABLE IF EXISTS sys_organ;
CREATE TABLE sys_organ (
  ORGAN_ID varchar(255) NOT NULL,
  ORGAN_NAME varchar(255) DEFAULT NULL,
  ORGAN_CODE varchar(255) DEFAULT NULL,
  ORGAN_LEVEL int(11) NOT NULL,
  ORGAN_SORT int(11) NOT NULL,
  ORGAN_DESC varchar(255) DEFAULT NULL,
  PRIMARY KEY (ORGAN_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_organ
-- ----------------------------
INSERT INTO sys_organ VALUES ('26c3f4f5', '客户部', null, '1', '2', '客户部');
INSERT INTO sys_organ VALUES ('4c5f23b8', '财务部', null, '1', '4', '财务部');
INSERT INTO sys_organ VALUES ('c827ed5a', '售后部', null, '1', '6', '售后部');
INSERT INTO sys_organ VALUES ('d0238732', '市场部', null, '1', '3', '市场部');
INSERT INTO sys_organ VALUES ('f2019315', '设计部', null, '1', '5', '设计部');
INSERT INTO sys_organ VALUES ('f4c01bec', '行政部', null, '1', '1', '行政部');

-- ----------------------------
-- Table structure for sys_rbac_mod2op
-- ----------------------------
DROP TABLE IF EXISTS sys_rbac_mod2op;
CREATE TABLE sys_rbac_mod2op (
  MODULE_ID varchar(255) NOT NULL,
  OPERATE_ID varchar(255) NOT NULL,
  ORDERNO int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rbac_mod2op
-- ----------------------------
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0001', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0001', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0001', 'delete', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0001', 'edit', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0002', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0002', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0002', 'delete', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0002', 'edit', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0003', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0003', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0003', 'delete', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0003', 'edit', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0004', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0004', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0004', 'edit', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0004', 'delete', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0005', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0005', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0005', 'edit', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0005', 'delete', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0006', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0006', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0006', 'edit', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0006', 'delete', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0006', 'export', '5');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0006', 'import', '6');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0006', 'report', '7');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0007', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0008', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0009', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0009', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0009', 'edit', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0009', 'delete', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0009', 'export', '5');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0009', 'import', '6');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0009', 'report', '7');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0010', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0010', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0010', 'edit', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0010', 'delete', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0010', 'export', '5');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0010', 'import', '6');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0010', 'report', '7');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0011', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0011', 'add', '2');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0011', 'edit', '3');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0011', 'delete', '4');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0011', 'export', '5');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0011', 'import', '6');
INSERT INTO sys_rbac_mod2op VALUES ('SYS-0011', 'report', '7');
INSERT INTO sys_rbac_mod2op VALUES ('USERINFO-0001', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('USERINFO-0001', 'edit', '2');
INSERT INTO sys_rbac_mod2op VALUES ('USERINFO-0002', 'view', '1');
INSERT INTO sys_rbac_mod2op VALUES ('USERINFO-0002', 'edit', '2');

-- ----------------------------
-- Table structure for sys_rbac_modright
-- ----------------------------
DROP TABLE IF EXISTS sys_rbac_modright;
CREATE TABLE sys_rbac_modright (
  MODULE_ID varchar(255) NOT NULL,
  ROLE_ID varchar(32) NOT NULL,
  RIGHT_VALUE int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rbac_modright
-- ----------------------------
INSERT INTO sys_rbac_modright VALUES ('SYS-0001', 'AXQcpi7n3kW', '15');
INSERT INTO sys_rbac_modright VALUES ('SYS-0002', 'AXQcpi7n3kW', '15');
INSERT INTO sys_rbac_modright VALUES ('SYS-0003', 'AXQcpi7n3kW', '15');
INSERT INTO sys_rbac_modright VALUES ('SYS-0004', 'AXQcpi7n3kW', '15');
INSERT INTO sys_rbac_modright VALUES ('SYS-0005', 'AXQcpi7n3kW', '15');
INSERT INTO sys_rbac_modright VALUES ('SYS-0006', 'AXQcpi7n3kW', '127');
INSERT INTO sys_rbac_modright VALUES ('SYS-0007', 'AXQcpi7n3kW', '1');
INSERT INTO sys_rbac_modright VALUES ('SYS-0008', 'AXQcpi7n3kW', '1');
INSERT INTO sys_rbac_modright VALUES ('SYS-0009', 'AXQcpi7n3kW', '127');
INSERT INTO sys_rbac_modright VALUES ('SYS-0010', 'AXQcpi7n3kW', '127');
INSERT INTO sys_rbac_modright VALUES ('SYS-0011', 'AXQcpi7n3kW', '127');
INSERT INTO sys_rbac_modright VALUES ('USERINFO-0001', 'AXQcpi7n3kW', '5');
INSERT INTO sys_rbac_modright VALUES ('USERINFO-0002', 'AXQcpi7n3kW', '5');

-- ----------------------------
-- Table structure for sys_rbac_module
-- ----------------------------
DROP TABLE IF EXISTS sys_rbac_module;
CREATE TABLE sys_rbac_module (
  MODULE_ID varchar(255) NOT NULL,
  MODULE_NAME varchar(255) DEFAULT NULL,
  MODULE_ICON varchar(255) DEFAULT NULL,
  MODULE_LEVEL int(11) DEFAULT NULL,
  MODULE_SORT int(11) DEFAULT NULL,
  MODULE_DESC varchar(255) DEFAULT NULL,
  PRIMARY KEY (MODULE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rbac_module
-- ----------------------------
INSERT INTO sys_rbac_module VALUES ('SYS', '系统维护', NULL, '1', '1', null);
INSERT INTO sys_rbac_module VALUES ('SYS-0001', '系统模块', NULL, '2', '1', 'manage/sys/module/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0002', '系统操作', NULL, '2', '2', 'manage/sys/operate/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0003', '用户管理', NULL, '2', '3', 'manage/sys/user/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0004', '角色管理', NULL, '2', '4', 'manage/sys/role/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0005', '组织机构', NULL, '2', '5', 'manage/sys/organ/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0006', '系统日志', NULL, '2', '6', 'manage/sys/sysLog/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0007', '系统信息', NULL, '2', '7', 'manage/sys/sysInfo/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0008', '系统缓存', NULL, '2', '8', 'manage/sys/sysCache/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0009', '索引信息', NULL, '2', '9', 'manage/sys/indexInfo/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0010', '索引任务', NULL, '2', '10', 'manage/sys/indexTask/view.do');
INSERT INTO sys_rbac_module VALUES ('SYS-0011', '系统锁', NULL, '2', '11', 'manage/sys/sysLock/view.do');
INSERT INTO sys_rbac_module VALUES ('USERINFO', '个人信息', NULL, '1', '2', null);
INSERT INTO sys_rbac_module VALUES ('USERINFO-0001', '资料维护', NULL, '2', '1', 'manage/sys/userInfo/view.do');
INSERT INTO sys_rbac_module VALUES ('USERINFO-0002', '修改密码', NULL, '2', '2', 'manage/sys/userPassword/view.do');

-- ----------------------------
-- Table structure for sys_rbac_operate
-- ----------------------------
DROP TABLE IF EXISTS sys_rbac_operate;
CREATE TABLE sys_rbac_operate (
  OPERATE_ID varchar(32) NOT NULL,
  OPERATE_NAME varchar(255) DEFAULT NULL,
  OPERATE_VALUE bigint(20) DEFAULT NULL,
  PRIMARY KEY (OPERATE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rbac_operate
-- ----------------------------
INSERT INTO sys_rbac_operate VALUES ('view', '浏览', '1');
INSERT INTO sys_rbac_operate VALUES ('add', '新增', '2');
INSERT INTO sys_rbac_operate VALUES ('edit', '修改', '4');
INSERT INTO sys_rbac_operate VALUES ('delete', '删除', '8');
INSERT INTO sys_rbac_operate VALUES ('export', '导出', '16');
INSERT INTO sys_rbac_operate VALUES ('import', '导入', '32');
INSERT INTO sys_rbac_operate VALUES ('report', '报表', '64');

-- ----------------------------
-- Table structure for sys_rbac_role
-- ----------------------------
DROP TABLE IF EXISTS sys_rbac_role;
CREATE TABLE sys_rbac_role (
  ROLE_ID varchar(32) NOT NULL,
  ROLE_NAME varchar(255) DEFAULT NULL,
  ROLE_ORDERNO int(11) DEFAULT NULL,
  ROLE_DESC varchar(255) DEFAULT NULL,
  CREATE_TIME datetime DEFAULT NULL,
  CREATE_USER varchar(255) DEFAULT NULL,
  PRIMARY KEY (ROLE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rbac_role
-- ----------------------------
INSERT INTO sys_rbac_role VALUES ('AXQcpi7n3kW', '系统管理员', 1, '系统管理员', null, null);

-- ----------------------------
-- Table structure for sys_rbac_user
-- ----------------------------
DROP TABLE IF EXISTS sys_rbac_user;
CREATE TABLE sys_rbac_user (
  USER_ID varchar(32) NOT NULL,
  USER_NAME varchar(255) DEFAULT NULL,
  USER_PASS varchar(255) DEFAULT NULL,
  USER_CODE varchar(255) DEFAULT NULL,
  USER_DEPT_ID varchar(255) DEFAULT NULL,
  USER_DUTY varchar(255) DEFAULT NULL,
  USER_CERT_TYPE varchar(255) DEFAULT NULL,
  USER_CERT_CODE varchar(255) DEFAULT NULL,
  USER_DATE datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  USER_NATION varchar(255) DEFAULT NULL,
  USER_SEX varchar(255) DEFAULT NULL,
  USER_PHONE varchar(255) DEFAULT NULL,
  USER_MOBILE varchar(255) DEFAULT NULL,
  USER_MAIL varchar(255) DEFAULT NULL,
  USER_ADDRESS varchar(255) DEFAULT NULL,
  USER_POST_CODE varchar(255) DEFAULT NULL,
  USER_REMARK varchar(255) DEFAULT NULL,
  CREATE_TIME datetime DEFAULT NULL,
  CREATE_USER varchar(255) DEFAULT NULL,
  LOGIN_TIME datetime DEFAULT NULL,
  USER_STATUS varchar(1) DEFAULT NULL,
  PRIMARY KEY (USER_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rbac_user
-- ----------------------------
INSERT INTO sys_rbac_user(USER_ID,USER_NAME,USER_PASS,USER_MOBILE,USER_MAIL,CREATE_TIME) 
VALUES ('admin', '系统管理员', 'e10adc3949ba59abbe56e057f20f883e', '18965188560', 'qqwwee_0@sina.com', '2013-07-27 21:43:39');

-- ----------------------------
-- Table structure for sys_rbac_usertorole
-- ----------------------------
DROP TABLE IF EXISTS sys_rbac_usertorole;
CREATE TABLE sys_rbac_usertorole (
  USER_ID varchar(255) NOT NULL,
  ROLE_ID varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rbac_usertorole
-- ----------------------------
INSERT INTO sys_rbac_usertorole VALUES ('admin', 'AXQcpi7n3kW');