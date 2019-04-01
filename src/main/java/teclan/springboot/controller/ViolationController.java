package teclan.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 违章信息
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

        if(StringUtils.isNullOrEmpty(licensePlate)){
            httpServletResponse.setStatus(500);
            return ResultUtils.get("添加失败，车牌号为空", data);
        }
        data.put("create_time",new Date());
        Object[] values = SqlUtils.getValues(data);
        jdbcTemplate.update(String.format("insert into violation (%s) values (%s)", SqlUtils.getSqlForInsert(data), SqlUtils.getFillString(data, "?")), values);
        return ResultUtils.get("添加成功", data);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JSONObject delete(ServletRequest servletRequest, ServletResponse servletResponse, String id) {
        jdbcTemplate.update("delete from violation where id=?", id);
        return ResultUtils.get("删除成功", null);
    }


    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JSONObject page(ServletRequest servletRequest, ServletResponse servletResponse,String licensePlate, String type,String zone,String cause,String orderBy,String sort,int currentPage,int pageSize) {
        String countSql="SELECT count(*) FROM violation a LEFT JOIN violation_type b ON a.type=b.id WHERE %s";
        String querySql = "SELECT * FROM violation a a LEFT JOIN violation_type b ON a.type=b.id WHERE %s ORDER BY %s %s limit %s,%s";

        StringBuilder sb =new StringBuilder(" 1=1");

        if(StringUtils.isNullOrEmpty(licensePlate)){
            sb.append(String.format(" and a.license_plate LIKE CONCAT('%',%s,'%') ",licensePlate));
        }
        if(StringUtils.isNullOrEmpty(type)){
            sb.append(String.format(" a.type idCard LIKE CONCAT('%',%s,'%') ",type));
        }
        if(StringUtils.isNullOrEmpty(zone)){
            sb.append(String.format(" and a.zone LIKE CONCAT('%',%s,'%') ",zone));
        }
        if(StringUtils.isNullOrEmpty(cause)){
            sb.append(String.format(" and a.cause LIKE CONCAT('%',%s,'%') ",cause));
        }

        int totals = jdbcTemplate.queryForObject(String.format(countSql,sb.toString()),Integer.class);

        if(totals<1){
            return ResultUtils.get("查询成功", new ArrayList<>(), PagesUtils.getPageInfo(currentPage,pageSize,totals));
        }else {
            List<Map<String,Object>> datas = jdbcTemplate.queryForList(String.format(querySql,sb.toString(),orderBy,sort,PagesUtils.getOffset(currentPage,totals),pageSize));
            return ResultUtils.get("查询成功", datas, PagesUtils.getPageInfo(currentPage,pageSize,totals));
        }
    }

    @RequestMapping(value = "/analyse", method = RequestMethod.POST)
    public JSONObject analyse(ServletRequest servletRequest, ServletResponse servletResponse,  String licensePlate) {

        String sql="select count(*) from violation group by type where 1=1";
        List<Map<String,Object>> datas= new ArrayList<>();

        if(StringUtils.isNullOrEmpty(licensePlate)){
             datas = jdbcTemplate.queryForList(sql);
        }else {
            datas = jdbcTemplate.queryForList(String.format(sql+" and license_plate CONCAT('%',%s,'%')",licensePlate));
        }
        return ResultUtils.get("查询成功", null);
    }
}
