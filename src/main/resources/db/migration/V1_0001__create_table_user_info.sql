CREATE TABLE `user_info` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `name` varchar(32) DEFAULT NULL COMMENT '名字',
  `age` int COMMENT '年龄',
  `phone` VARCHAR(16) COMMENT '电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




