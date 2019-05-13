ALTER TABLE violation add `pay` int comment '罚款缴付状态 0/1/2: 无需交付/未缴付/已缴付';
ALTER TABLE violation add `appeal` int comment '上诉状态 0/1/2/3: 未发起/上诉中/通过/驳回';
ALTER TABLE violation exchange  `status` int comment '确认状态 0/1/2:未确认/确认/误报';