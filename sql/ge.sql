# Host: 127.0.0.1  (Version 5.7.11-log)
# Date: 2019-06-21 18:33:02
# Generator: MySQL-Front 6.1  (Build 1.26)


#
# Structure for table "ge"
#

DROP TABLE IF EXISTS `ge`;
CREATE TABLE `ge` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键id，int对应Integer',
  `char` char(50) DEFAULT NULL COMMENT 'char字段对应String',
  `varchar` varchar(255) DEFAULT NULL COMMENT 'varchar字段对应String',
  `bit` bit(1) DEFAULT NULL COMMENT 'bit字段Byte',
  `bigint` bigint(20) DEFAULT NULL COMMENT 'bigint字段BigInteger',
  `float` float DEFAULT NULL COMMENT 'float字段对应Float',
  `double` double DEFAULT NULL COMMENT 'double字段对应Double',
  `decimal` decimal(10,2) DEFAULT NULL COMMENT 'decimal对应BigDecimal',
  `datatime` datetime DEFAULT NULL COMMENT 'datatime对应Date',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ge表';

#
# Data for table "ge"
#

