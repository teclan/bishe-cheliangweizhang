CREATE TABLE `vehicle_info`
(
  `id`            int(11) NOT NULL auto_increment primary key comment 'ID',
  `engine_no`   varchar(64) comment '发动机编号',
  `frame`  varchar(64) comment '车架号',
  `qualified_no`  varchar(64) comment '合格证',
  `vehicle_license` varchar(64) comment '行驶证',
  `license_plate` varchar(8) not null comment '车牌号',
  `register_at`  datetime comment '登记时间',
  `update_at` datetime comment '登记修改时间',
  `owner`         int(11) comment '用户ID'
) comment '车辆信息';
