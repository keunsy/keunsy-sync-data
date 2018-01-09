/*此文件为记录更改sql*/
/*文件格式为UTF-8 利于赋值粘贴*/
/*按照倒序来进行，新内容请置于最前面*/
/*sql说明格式 ：sql描述 、版本 及时间 、执行服务器 */

/* 
1.描述：添加或更改更新时间字段为要求格式
2.版本：1.0.0
3.时间:2015年9月28日16:01:34
4.服务器:super_plate
*/
alter table local_cluster_user_info modify  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';
alter table local_cluster_sign_info modify  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';
alter table local_cluster_td_info  add  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';


/*super_plate 通道签名 网关及集群公用  新建表前 需将原表数据进行转移  或直接复制集群库表  然后进行时间字段修改*/

注意：需满足下列先提条件
1.user_check_type 增加 sn 主键
2.相同表sn需要保持一致

CREATE TABLE `td_sign_info` (
  `sn` int(11) NOT NULL AUTO_INCREMENT COMMENT '表主键',
  `td_code` varchar(255) NOT NULL COMMENT '通道代码',
  `ext_code` varchar(25) NOT NULL DEFAULT '' COMMENT '通道扩展号码，父通道为空串',
  `is_ext` tinyint(4) DEFAULT NULL COMMENT '是否为通道扩展[0=否 1=是]',
  `sign_chs` varchar(50) DEFAULT '' COMMENT '中文签名',
  `sign_eng` varchar(50) DEFAULT '' COMMENT '英文签名',
  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作人',
  `insert_time` varchar(20) DEFAULT NULL COMMENT '插入时间',
  `charge_term_id` varchar(50) DEFAULT '' COMMENT '计费代码与扩展号唯一对应',
  PRIMARY KEY (`sn`),
  UNIQUE KEY `sp_number` (`td_code`,`ext_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
或复制后
alter table td_sign_info modify  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';


CREATE TABLE `thread_controller` (
  `sn` int(11) NOT NULL AUTO_INCREMENT,
  `server_ip` varchar(30) NOT NULL COMMENT '线程在哪台服务器上启动',
  `thread_name` varchar(255) NOT NULL COMMENT '线程名',
  `action` int(11) NOT NULL DEFAULT '0' COMMENT '0启动  1关闭',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0未操作  1操作完成',
  `thread_param` varchar(500) NOT NULL COMMENT '参数信息',
  `thread_type` int(11) NOT NULL COMMENT '0：开启，1：关闭',
  `group_id` varchar(50) NOT NULL COMMENT '通道代码',
  `app_name` varchar(50) NOT NULL COMMENT '应用程序',
  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`sn`)
) ENGINE=MyISAM AUTO_INCREMENT=752 DEFAULT CHARSET=utf8;
或复制后
alter table thread_controller  add  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';

alter table local_cluster_user_check_type  add  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';
alter table local_cluster_user_service_info modify  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';

CREATE TABLE `user_service_param` (
  `sn` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户业务主键，自动递增',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户唯一标示,关联账户表',
  `param_key` varchar(100) DEFAULT NULL COMMENT '参数字段名',
  `param_value` varchar(100) DEFAULT NULL COMMENT '参数字段值',
  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`sn`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;
或复制后
alter table user_service_param  add  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';


CREATE TABLE `user_country_phone_code_price` (
  `sn` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `country_en` varchar(64) NOT NULL COMMENT '国家英文名称',
  `country_cn` varchar(64) NOT NULL COMMENT '国家中文名称',
  `short_code` varchar(16) NOT NULL COMMENT '国家简称',
  `phone_pre` varchar(16) NOT NULL COMMENT '国际区号',
  `price` double(20,5) NOT NULL DEFAULT '0.00000',
  `user_id` varchar(32) NOT NULL,
  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`sn`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
或复制后
alter table user_country_phone_code_price  add  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';

/*cluster_server 进行时间字段修改或添加*/
alter table user_info modify  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';
alter table sign_info modify  `update_time` timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';





