create table flow_info
(
 `role` varchar(32) comment '角色',
 `order` int comment '顺序',
 primary key (`order`)
)comment '流程顺序表';

insert into flow_info values ('police',1);
insert into flow_info values ('admin',2);
insert into flow_info values ('captain',3);
insert into flow_info values ('general',4);
