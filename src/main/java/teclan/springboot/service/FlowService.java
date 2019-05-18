package teclan.springboot.service;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Resource
public class FlowService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public String getNextRole(String currentRole){
        Map<String,Object> map = jdbcTemplate.queryForMap("select top 1 role from flow_info where order>(select order from flow_info where role = ?)",currentRole);

        return map==null||map.get("role")==null?"":map.get("role").toString();
    }
}
