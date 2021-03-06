create table violation(
  `id` varchar(64) not null primary key comment 'ID',
  `license_plate` varchar(8) not null comment '车牌号',
  `type` varchar(20) not null comment '类型',
  `zone` varchar(200) not null comment '区域',
  `cause` varchar(2000) not null comment '原因',
  `deduction_score` int(11) null comment '扣除分数',
  `deduction_amount` DECIMAL(12,2) null comment '罚款金额',
  `detention_day`  int(11) default 0 comment '拘留天数',
  `police` varchar(32) comment '现场警员编号',
  `punisher` varchar(32) comment '处罚人编号',
  `update_at` datetime comment '修改时间',
  `create_time` datetime comment '创建时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '违章记录';
