CREATE TABLE `user_info` (
  `id` int(11) NOT NULL auto_increment primary key comment 'ID',
  `code` varchar(50) not null comment '用户标识',
  `name` varchar(32) DEFAULT NULL comment '名字',
  `age` int comment '年龄',
  `phone` varchar(16) comment '电话',
  `role` varchar(32) comment '角色，superadmin/admin/general:超级管理员/管理员/普通用户'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




