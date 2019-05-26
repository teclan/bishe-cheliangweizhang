ALTER TABLE violation_type CHANGE  `deduction_score` `deduction_score` varchar(16) comment '默认分值';
ALTER TABLE violation_type CHANGE  `deduction_amount` `deduction_amount` varchar(16) comment '默认罚款';
ALTER TABLE violation_type CHANGE  `detention_day` `detention_day` varchar(16)  comment '默认拘留天数';