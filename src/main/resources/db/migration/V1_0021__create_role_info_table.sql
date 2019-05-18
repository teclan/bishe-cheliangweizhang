create table role_info
(
 role varchar(32) comment '角色',
 role_name varchar(32),
 primary key (role)
);

insert into role_info values ('superadmin','超级管理员');
insert into role_info values ('police','普通警察');
insert into role_info values ('admin','管理员');
insert into role_info values ('captain','交警队长');
insert into role_info values ('general','普通用户');
