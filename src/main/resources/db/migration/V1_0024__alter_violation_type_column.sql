ALTER TABLE violation_type CHANGE  `value` `deduction_score` int comment '默认分值';
ALTER TABLE violation_type add  `deduction_amount` int default 0 comment '默认罚款';
ALTER TABLE violation_type add  `detention_day` int default 0  comment '默认拘留天数';