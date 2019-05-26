package teclan.springboot.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ViolationTypeService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public  Map<String,Object> get(String id){
        Map<String,Object> map = jdbcTemplate.queryForMap("select deduction_score,deduction_amount,detention_day from violation_type where id=?",id);
        return map;
    }
}
