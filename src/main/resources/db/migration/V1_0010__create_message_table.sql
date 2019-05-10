CREATE TABLE `message`(
  `id` varchar(64) NOT NULL comment 'ID',
  `user_id` int(11) not null comment '用户ID',
  `description` varchar(2000) DEFAULT NULL comment '描述',
  `create_time` datetime comment '创建时间',
  `read` int comment '是否已读 0/1',
  `read_time` datetime comment '阅读时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



