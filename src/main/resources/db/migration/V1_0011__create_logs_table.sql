CREATE TABLE `logs`(
  `id` varchar(64) NOT NULL comment 'ID',
  `user_id` int(11) not null comment '用户ID',
  `module` int(11) not null comment '模块',
  `description` varchar(2000) DEFAULT NULL comment '描述',
  `status` varchar(8) comment '状态',
  `create_time` datetime comment '创建时间',
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
