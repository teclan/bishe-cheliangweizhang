CREATE TABLE `dev_info` (
  `id` int(11) NOT NULL COMMENT '设备ID',
  `name` varchar(32) DEFAULT NULL COMMENT '设备名称',
  `description` VARCHAR(256) COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

