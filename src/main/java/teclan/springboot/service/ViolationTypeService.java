package teclan.springboot.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ViolationTypeService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public String get(String id){
        Map<String,Object> map = jdbcTemplate.queryForMap("select `value` from violation_type where id=?",id);
        return map.get("value").toString();
    }
}
