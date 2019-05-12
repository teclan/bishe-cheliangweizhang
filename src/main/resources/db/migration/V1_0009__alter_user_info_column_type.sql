ALTER TABLE user_info MODIFY COLUMN last_time varchar(32) NULL COMMENT '最后操作时间';
ALTER TABLE user_info MODIFY COLUMN create_time varchar(32) NULL COMMENT '创建时间';