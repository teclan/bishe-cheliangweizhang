create table violation_type(
  `id` int(11) not null primary key comment 'ID',
  `type_name` varchar(64) not null comment '类型名称'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '违章类型';
