/*
 Navicat MySQL Data Transfer

 Source Server         : 6
 Source Server Version : 50626
 Source Host           : 192.168.1.6
 Source Database       : config

 Target Server Version : 50626
 File Encoding         : utf-8

 Date: 04/14/2017 10:11:35 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `app`
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `creator` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `config`
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `app` varchar(50) NOT NULL,
  `env` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `content` varchar(10000) NOT NULL,
  `status` varchar(50) NOT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(50) NOT NULL,
  `editor` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_app_env_name` (`app`,`env`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `config_history`
-- ----------------------------
DROP TABLE IF EXISTS `config_history`;
CREATE TABLE `config_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `config_id` bigint(20) NOT NULL,
  `app` varchar(50) NOT NULL,
  `env` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `content` varchar(10000) NOT NULL,
  `status` varchar(50) NOT NULL,
  `operator` varchar(50) NOT NULL COMMENT '操作人',
  `version` int(11) NOT NULL,
  `operate_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_config_id` (`config_id`) USING BTREE,
  KEY `idx_app_env_name` (`app`,`env`,`name`) USING BTREE,
  KEY `idx_operator` (`operator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `env`
-- ----------------------------
DROP TABLE IF EXISTS `env`;
CREATE TABLE `env` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `creator` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
