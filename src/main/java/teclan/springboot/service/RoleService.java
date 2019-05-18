package teclan.springboot.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RoleService {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public String getRoleInfo(String userCode){
        Map<String,Object> map = jdbcTemplate.queryForMap("select role from user_info where code=?",userCode);
        return map.get("role").toString();
    }
}
