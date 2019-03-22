package teclan.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.utils.ResultUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 违章类型查询
 */
@RestController
@RequestMapping("/violationType")
public class ViolationTypeController {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/get")
    public JSONObject get() {
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from  violation_type");
        return ResultUtils.get("查询成功", mapList);
    }
}
