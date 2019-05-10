package teclan.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.flywaydb.core.internal.util.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.utils.*;

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
    public JSONObject create(ServletRequest servletRequest, ServletResponse servletResponse,
                             @RequestParam(value = "license_plate", required = true) String licensePlate,
                             @RequestParam(value = "type", required = true) String type,
                             @RequestParam(value = "zone", required = true) String zone,
                             @RequestParam(value = "cause", required = false) String cause,
                             @RequestParam(value = "deduction_score", required = false ,defaultValue = "0") Double deductionScore,
                             @RequestParam(value = "deduction_amount", required = false ,defaultValue = "0.0") Double deductionAmount,
                             @RequestParam(value = "detention_day", required = false,defaultValue = "0") Integer detentionDay,
                             @RequestParam(value = "police", required = false) String police) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if (StringUtils.isNullOrEmpty(licensePlate)) {
//            httpServletResponse.setStatus(500);
            return ResultUtils.get(500,"添加失败，车牌号为空", null);
        }

        String id=IdUtils.get();
        jdbcTemplate.update("insert into violation (id,license_plate,type,zone,cause,deduction_score,deduction_amount,detention_day,police,create_time) values (?,?,?,?,?,?,?,?,?,?)", id, licensePlate,type,zone,cause,deductionScore,deductionAmount,detentionDay,police,new Date());

        return ResultUtils.get("添加成功", id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(ServletRequest servletRequest, ServletResponse servletResponse,
                             @RequestParam(value = "id", required = true)String id,
                             @RequestParam(value = "license_plate", required = true) String licensePlate,
                             @RequestParam(value = "type", required = true) String type,
                             @RequestParam(value = "zone", required = true) String zone,
                             @RequestParam(value = "cause", required = false) String cause,
                             @RequestParam(value = "deduction_score", required = true ,defaultValue = "0") Double deductionScore,
                             @RequestParam(value = "deduction_amount", required = true ,defaultValue = "0.0") Double deductionAmount,
                             @RequestParam(value = "detention_day", required = true,defaultValue = "0") Integer detentionDay,
                             @RequestParam(value = "police", required = true) String police,
                             @RequestParam(value = "punisher", required = true) String punisher) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if (StringUtils.isNullOrEmpty(id)) {
//            httpServletResponse.setStatus(500);
            return ResultUtils.get(500,"修改失败，指定的记录id无效", id);
        }

        if (StringUtils.isNullOrEmpty(licensePlate)) {
//            httpServletResponse.setStatus(500);
            return ResultUtils.get(500,"修改失败，车牌号为空", null);
        }

        jdbcTemplate.update("update violation set license_plate=?,type=?,zone=?,cause=?,deduction_score=?,deduction_amount=?,detention_day=?,police=?,punisher=?,update_at=?" +
                " where id=?", licensePlate,type,zone,cause,deductionScore,deductionAmount,detentionDay,police,punisher,new Date(),id);


        return ResultUtils.get("操作成功", id);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JSONObject delete(ServletRequest servletRequest, ServletResponse servletResponse,  @RequestParam(value = "id", required = true)String id) {
        jdbcTemplate.update("delete from violation where id=?", id);
        return ResultUtils.get("删除成功", id);
    }


    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JSONObject page(ServletRequest servletRequest, ServletResponse servletResponse, String licensePlate, String type, String zone, String cause,String owner,@RequestParam(value = "orderBy" ,required = true,defaultValue = "create_time") String orderBy, @RequestParam(value = "sort" ,required = true,defaultValue = "DESC")String sort, @RequestParam(value = "currentPage" ,required = true,defaultValue = "1")int currentPage, @RequestParam(value = "pageSize" ,required = true,defaultValue = "20")int pageSize) {
        String countSql = "SELECT count(*) FROM violation a " +
                "left join violation_type b ON a.type=b.id " +
                "left join vehicle_info c on a.license_plate=c.license_plate " +
                "left join user_info d on c.owner=d.id  WHERE ";
        String querySql = "SELECT a.*,b.type_name,c.engine_no,c.frame,c.qualified_no,c.vehicle_license,d.name,d.phone FROM violation a " +
                "left join violation_type b ON a.type=b.id " +
                "left join vehicle_info c on a.license_plate=c.license_plate " +
                "left join user_info d on c.owner=d.id WHERE ";

        StringBuilder sb = new StringBuilder(" 1=1");

        if (!StringUtils.isNullOrEmpty(owner)) {
            sb.append(" and c.owner= "+owner);
        }

        if (!StringUtils.isNullOrEmpty(licensePlate)) {
            sb.append(" and a.license_plate LIKE '%"+licensePlate+"%' ");
        }
        if (!StringUtils.isNullOrEmpty(type)) {
            sb.append(" a.type idCard '%"+type+"%' ");
        }
        if (!StringUtils.isNullOrEmpty(zone)) {
            sb.append(" and a.zone LIKE '%"+zone+"%' ");
        }
        if (!StringUtils.isNullOrEmpty(cause)) {
            sb.append(" and a.cause LIKE '%"+cause+"%' ");
        }

        int totals = jdbcTemplate.queryForObject(countSql+sb.toString(), Integer.class);

        if (totals < 1) {
            return ResultUtils.get("查询成功", new ArrayList<>(), PagesUtils.getPageInfo(currentPage, pageSize, totals));
        } else {
            List<Map<String, Object>> datas = jdbcTemplate.queryForList(querySql+sb.toString()+" order by a."+orderBy+" "+sort +" limit "+ PagesUtils.getOffset(currentPage, pageSize)+","+pageSize);
            return ResultUtils.get("查询成功", datas, PagesUtils.getPageInfo(currentPage, pageSize, totals));
        }
    }

    @RequestMapping(value = "/analyse", method = RequestMethod.POST)
    public JSONObject analyse(ServletRequest servletRequest, ServletResponse servletResponse, String licensePlate,String starTime,String endTime) {

        String sql = "select violation.type,violation_type.type_name,count(*) as times from violation left join violation_type on violation.type=violation_type.id  where 1=1 ";
        List<Map<String, Object>> datas = new ArrayList<>();

        if (StringUtils.isNullOrEmpty(starTime)){
            starTime="1970";
        }
        sql+=" and create_time>'"+starTime+"' ";

        if (StringUtils.isNullOrEmpty(endTime)){
            endTime= DateUtils.formatDateAsIsoString(new Date());
        }
        sql+=" and create_time<'"+endTime+"' ";

        if (!StringUtils.isNullOrEmpty(licensePlate)) {
            sql+=" and license_plate like '%"+licensePlate+"%' ";
        }

        datas = jdbcTemplate.queryForList(sql + " group by type");
        return ResultUtils.get("查询成功", datas);
    }
}
