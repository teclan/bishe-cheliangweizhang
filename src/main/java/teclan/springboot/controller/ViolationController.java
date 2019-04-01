package teclan.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.utils.Objects;
import teclan.springboot.utils.PagesUtils;
import teclan.springboot.utils.ResultUtils;
import teclan.springboot.utils.SqlUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 车辆信息
 */
@RestController
@RequestMapping("/violation")
public class ViolationController {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JSONObject create(ServletRequest servletRequest, ServletResponse servletResponse, Map<String, Object> data) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String licensePlate = data.containsKey("license_plate") ? data.get("license_plate").toString() : "";
        int count = jdbcTemplate.queryForObject(String.format("select count(*) from vehicle_info  where license_plate='%s'", licensePlate), Integer.class);
        if (count > 0) {
            httpServletResponse.setStatus(403);
            return ResultUtils.get("添加失败，车牌号重复", data);
        } else {
            Object[] values = SqlUtils.getValues(data);
            jdbcTemplate.update(String.format("insert into vehicle_info (%s) values (%s)", SqlUtils.getSqlForInsert(data), SqlUtils.getFillString(data, "?")), values);
            return ResultUtils.get("添加成功", data);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JSONObject delete(ServletRequest servletRequest, ServletResponse servletResponse, String id) {
        jdbcTemplate.update("delete from vehicle_info where id=?", id);
        return ResultUtils.get("删除成功", null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(ServletRequest servletRequest, ServletResponse servletResponse, String id, Map<String, Object> data) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String licensePlate = data.containsKey("license_plate") ? data.get("license_plate").toString() : "";
        int count = jdbcTemplate.queryForObject(String.format("select count(*) from vehicle_info  where license_plate='%s' and id<>%s", licensePlate, id), Integer.class);
        if (count > 0) {
            httpServletResponse.setStatus(403);
            return ResultUtils.get("修改失败，车牌号重复", data);
        }
        Object[] values = SqlUtils.getValues(data);
        values = Objects.merge(values, new Object[]{id});
        jdbcTemplate.update(String.format("update vehicle_info set %s where id=?"), values);
        return ResultUtils.get("修改成功", null);
    }

    @RequestMapping(value = "/findByLicensePlate", method = RequestMethod.POST)
    public JSONObject findByLicensePlate(ServletRequest servletRequest, ServletResponse servletResponse, String licensePlate) {
        Map data = jdbcTemplate.queryForMap("select a.*,b.name,b.phone from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where license_plate=?", licensePlate);
        return ResultUtils.get("查询成功", data);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public JSONObject findById(ServletRequest servletRequest, ServletResponse servletResponse, String id) {
        Map data = jdbcTemplate.queryForMap("select a.*,b.name,b.phone from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where id=?", id);
        return ResultUtils.get("查询成功", data);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JSONObject page(ServletRequest servletRequest, ServletResponse servletResponse, String keyword,String orderBy,String sort,int currentPage,int pageSize) {
        String countSql="SELECT count(*) FROM vehicle_info a LEFT JOIN user_info b ON a.owner=b.id WHERE a.license_plate LIKE CONCAT('%',%s,'%')";
        String querySql = "SELECT * FROM vehicle_info a LEFT JOIN user_info b ON a.owner=b.id WHERE a.license_plate LIKE CONCAT('%',%s,'%') ORDER BY %s %s limit %s,%s";
        int totals = jdbcTemplate.queryForObject(String.format(countSql,keyword),Integer.class);

        if(totals<1){
            return ResultUtils.get("查询成功", new ArrayList<>(), PagesUtils.getPageInfo(currentPage,pageSize,totals));
        }else {
            List<Map<String,Object>> datas = jdbcTemplate.queryForList(String.format(querySql,keyword,orderBy,sort,PagesUtils.getOffset(currentPage,totals),pageSize));
            return ResultUtils.get("查询成功", datas, PagesUtils.getPageInfo(currentPage,pageSize,totals));
        }
    }

}
