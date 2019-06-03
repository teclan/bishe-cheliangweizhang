ALTER TABLE violation CHANGE  `deduction_score` `deduction_score` varchar(16) comment '扣除分数';
ALTER TABLE violation CHANGE  `deduction_amount` `deduction_amount` varchar(16) comment '罚款金额';
ALTER TABLE violation CHANGE  `detention_day` `detention_day` varchar(16)  comment '拘留天数';