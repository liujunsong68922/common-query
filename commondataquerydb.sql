/*
Navicat MySQL Data Transfer

Source Server         : localhost-mysql
Source Server Version : 50703
Source Host           : localhost:3306
Source Database       : commondataquerydb

Target Server Type    : MYSQL
Target Server Version : 50703
File Encoding         : 65001

Date: 2020-10-11 17:52:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for common_query_sql
-- ----------------------------
DROP TABLE IF EXISTS `common_query_sql`;
CREATE TABLE `common_query_sql` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `memo` varchar(255) DEFAULT NULL,
  `queryargs` varchar(255) DEFAULT NULL,
  `queryname` varchar(255) DEFAULT NULL,
  `querysql` varchar(255) DEFAULT NULL,
  `dataprivilegeflag` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of common_query_sql
-- ----------------------------
INSERT INTO `common_query_sql` VALUES ('1', '1', 'qwhere', 'qtest', 'select * from t_test where 1=1 AND @qwhere', '1');

-- ----------------------------
-- Table structure for t_funccondition
-- ----------------------------
DROP TABLE IF EXISTS `t_funccondition`;
CREATE TABLE `t_funccondition` (
  `conditionid` varchar(50) NOT NULL,
  `conditionexpression` varchar(200) NOT NULL,
  PRIMARY KEY (`conditionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_funccondition
-- ----------------------------
INSERT INTO `t_funccondition` VALUES ('cond01', 'USER_TYPE=\'2\'');

-- ----------------------------
-- Table structure for t_function
-- ----------------------------
DROP TABLE IF EXISTS `t_function`;
CREATE TABLE `t_function` (
  `funcid` varchar(50) NOT NULL,
  `functype` varchar(1) NOT NULL,
  `parentfuncid` varchar(50) DEFAULT NULL,
  `funcqid` varchar(50) DEFAULT NULL,
  `funcconditions` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`funcid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_function
-- ----------------------------
INSERT INTO `t_function` VALUES ('dp01', '3', 'queryid1', null, '@cond01');
INSERT INTO `t_function` VALUES ('func01', '1', null, null, null);
INSERT INTO `t_function` VALUES ('queryid1', '2', 'func01', '1', null);

-- ----------------------------
-- Table structure for t_rolefunction
-- ----------------------------
DROP TABLE IF EXISTS `t_rolefunction`;
CREATE TABLE `t_rolefunction` (
  `roleid` varchar(50) NOT NULL,
  `funcid` varchar(50) NOT NULL,
  PRIMARY KEY (`roleid`,`funcid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_rolefunction
-- ----------------------------
INSERT INTO `t_rolefunction` VALUES ('role01', 'dp01');
INSERT INTO `t_rolefunction` VALUES ('role01', 'func01');
INSERT INTO `t_rolefunction` VALUES ('role01', 'queryid1');

-- ----------------------------
-- Table structure for t_test
-- ----------------------------
DROP TABLE IF EXISTS `t_test`;
CREATE TABLE `t_test` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `user_type` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test
-- ----------------------------
INSERT INTO `t_test` VALUES ('1', '1', '2');
INSERT INTO `t_test` VALUES ('2', '2', '2');
INSERT INTO `t_test` VALUES ('3', '3', '2');
INSERT INTO `t_test` VALUES ('4', '4', '2');
INSERT INTO `t_test` VALUES ('5', '5', '2');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userid` varchar(50) NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('user01');

-- ----------------------------
-- Table structure for t_userrole
-- ----------------------------
DROP TABLE IF EXISTS `t_userrole`;
CREATE TABLE `t_userrole` (
  `userid` varchar(50) NOT NULL,
  `roleid` varchar(50) NOT NULL,
  PRIMARY KEY (`userid`,`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userrole
-- ----------------------------
INSERT INTO `t_userrole` VALUES ('user01', 'role01');

-- ----------------------------
-- Table structure for t_web_tokenuser
-- ----------------------------
DROP TABLE IF EXISTS `t_web_tokenuser`;
CREATE TABLE `t_web_tokenuser` (
  `tokenid` varchar(50) NOT NULL,
  `userid` varchar(50) NOT NULL,
  PRIMARY KEY (`tokenid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_web_tokenuser
-- ----------------------------
INSERT INTO `t_web_tokenuser` VALUES ('0', 'user01');
