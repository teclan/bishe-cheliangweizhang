package teclan.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.flywaydb.core.internal.util.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import teclan.springboot.log.LogModule;
import teclan.springboot.log.LogService;
import teclan.springboot.log.LogStatus;
import teclan.springboot.msg.MessageService;
import teclan.springboot.utils.*;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
    @Resource
    private LogService logService;
    @Resource
    private MessageService messageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JSONObject create(HttpServletRequest httpServletRequest, ServletResponse servletResponse,
                             @RequestParam(value = "license_plate", required = true) String licensePlate,
                             @RequestParam(value = "type", required = true) String type,
                             @RequestParam(value = "zone", required = true) String zone,
                             @RequestParam(value = "cause", required = false) String cause,
                             @RequestParam(value = "deduction_score", required = false ,defaultValue = "0") Double deductionScore,
                             @RequestParam(value = "deduction_amount", required = false ,defaultValue = "0.0") Double deductionAmount,
                             @RequestParam(value = "detention_day", required = false,defaultValue = "0") Integer detentionDay,
                             @RequestParam(value = "police", required = false) String police,
                             @RequestParam(value = "url", required = false) String url
                             ) {

        String user = httpServletRequest.getHeader("user");
        
        if (StringUtils.isNullOrEmpty(licensePlate)) {
//            httpServletResponse.setStatus(500);
        	logService.add(LogModule.violationManage, user, String.format("创建违章 车牌:%s",licensePlate), LogStatus.fail);
            return ResultUtils.get(500,"添加失败，车牌号为空", null);
        }

        String id=IdUtils.get();
        jdbcTemplate.update("insert into violation (id,license_plate,type,zone,cause,deduction_score,deduction_amount,detention_day,police,create_time,status,pay,appeal,url) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", id, licensePlate,type,zone,cause,deductionScore,deductionAmount,detentionDay,police,new Date(),0,0,0,url);

        logService.add(LogModule.violationManage, user, String.format("创建违章 车牌:%s",licensePlate), LogStatus.success);
        
        return ResultUtils.get("添加成功", id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(HttpServletRequest httpServletRequest, ServletResponse servletResponse,
                             @RequestParam(value = "id", required = true)String id,
                             @RequestParam(value = "license_plate", required = true) String licensePlate,
                             @RequestParam(value = "type", required = true) String type,
                             @RequestParam(value = "zone", required = true) String zone,
                             @RequestParam(value = "cause", required = false) String cause,
                             @RequestParam(value = "deduction_score", required = true ,defaultValue = "0") Double deductionScore,
                             @RequestParam(value = "deduction_amount", required = true ,defaultValue = "0.0") Double deductionAmount,
                             @RequestParam(value = "detention_day", required = true,defaultValue = "0") Integer detentionDay,
                             @RequestParam(value = "police", required = true) String police,
                             @RequestParam(value = "punisher", required = true) String punisher,
                             @RequestParam(value = "pay", required = true) String pay,
                             @RequestParam(value = "appeal", required = true) String appeal) {
    	String user = httpServletRequest.getHeader("user");

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

        logService.add(LogModule.violationManage, user, String.format("处理违章 车牌:%s",licensePlate), LogStatus.success);
        
        return ResultUtils.get("操作成功", id);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JSONObject delete(HttpServletRequest httpServletRequest, ServletResponse servletResponse,  @RequestParam(value = "id", required = true)String id) {
    	String user = httpServletRequest.getHeader("user");
    	Map<String,Object> map = jdbcTemplate.queryForMap("select * from violation where id=?",id);
    	
    	jdbcTemplate.update("delete from violation where id=?", id);
        
        logService.add(LogModule.violationManage, user, String.format("删除违章 车牌:%s",map.get("license_plate")), LogStatus.success);
        
        return ResultUtils.get("删除成功", id);
    }
    
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JSONObject findById(ServletRequest servletRequest, ServletResponse servletResponse,  @RequestParam(value = "id", required = true)String id) {
    	  String querySql = "SELECT a.*,b.type_name,c.engine_no,c.frame,c.qualified_no,c.vehicle_license,d.name,d.phone FROM violation a " +
                  "left join violation_type b ON a.type=b.id " +
                  "left join vehicle_info c on a.license_plate=c.license_plate " +
                  "left join user_info d on c.owner=d.id WHERE a.id=?";
    	  
    	 Map<String,Object> map = jdbcTemplate.queryForMap(querySql,id);
    	  
        return ResultUtils.get("查询成功", map);
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public JSONObject confirm(HttpServletRequest httpServletRequest, ServletResponse servletResponse,  @RequestParam(value = "id", required = true)String id) {
    	String user = httpServletRequest.getHeader("user");
    	
        Map<String,Object> violation = jdbcTemplate.queryForMap("select * from violation where id=?",id);
        String licensePlate = violation.get("license_plate").toString();
        String deductionScore =violation.get("deduction_score")==null?"0": violation.get("deduction_score").toString();
        String deductionAmount =violation.get("deduction_amount")==null?"0": violation.get("deduction_amount").toString();
        String detentionDay =violation.get("detention_day")==null?"0": violation.get("detention_day").toString();

        Map<String,Object> vehicleInfo = jdbcTemplate.queryForMap("select * from vehicle_info where license_plate=?",licensePlate);
        String owner=vehicleInfo.get("owner").toString();

        if(!"0".equals(deductionScore)){
            // 扣除用户分数
            jdbcTemplate.update(String.format("update user_info set surplus=if(surplus is null,0-%s,surplus-%s)  WHERE id =%s",deductionScore,deductionScore,owner));
        }

        logService.add(LogModule.violationManage, user, String.format("确认处理违章结果 车牌:%s",licensePlate), LogStatus.success);

        Map data = jdbcTemplate.queryForMap(
				"select a.*,b.code,b.name,b.phone,b.id from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where license_plate=?",
				licensePlate);

		if (data.get("code") != null) {
			String msg=String.format("您的爱车%s违章编号%s处罚结果已生效，处罚结果: 扣除分数：%s，罚款： %s元，拘留天数：%s 天，请及时到交警中心处理，您也可以登录 交管12123 查看详情 ", licensePlate,id,deductionScore,deductionAmount,detentionDay);
			messageService.add(user,msg );

		}
		// 状态改为确认，确认状态 0/1/2:未确认/确认/误报
		jdbcTemplate.update("update violation set `status`=? where id=?",1,id);
		
        return ResultUtils.get("确认成功", id);
    }

    /**
     * 上诉
     * @param httpServletRequest
     * @param servletResponse
     * @param id
     * @return
     */
    @RequestMapping(value = "/appeal", method = RequestMethod.POST)
    public JSONObject appeal(HttpServletRequest httpServletRequest, ServletResponse servletResponse,  @RequestParam(value = "id", required = true)String id) {
        String user = httpServletRequest.getHeader("user");

        Map<String,Object> violation = jdbcTemplate.queryForMap("select * from violation where id=?",id);
        String licensePlate = violation.get("license_plate").toString();
        String deductionScore =violation.get("deduction_score")==null?"0": violation.get("deduction_score").toString();
        String deductionAmount =violation.get("deduction_amount")==null?"0": violation.get("deduction_amount").toString();
        String detentionDay =violation.get("detention_day")==null?"0": violation.get("detention_day").toString();

        Map<String,Object> vehicleInfo = jdbcTemplate.queryForMap("select * from vehicle_info where license_plate=?",licensePlate);
        String owner=vehicleInfo.get("owner").toString();

        logService.add(LogModule.violationManage, user, String.format("确认处理违章结果 车牌:%s",licensePlate), LogStatus.success);

        Map data = jdbcTemplate.queryForMap(
                "select a.*,b.code,b.name,b.phone,b.id from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where license_plate=?",
                licensePlate);

        if (data.get("code") != null) {
            messageService.add(data.get("code").toString(),
                    String.format("您的爱车 %s 违章编号:%s处罚已被撤销，原处罚结果：扣除分数:%s，罚款:%s元，拘留天数：%s 天，您也可以登录 交管12123 查看详情", licensePlate, id,
                            deductionScore, deductionAmount, detentionDay));
        }

        // 上诉状态改成 上诉中，上诉状态 0/1/2/3: 未发起/上诉中/通过/驳回
        jdbcTemplate.update("update violation set `appeal`=? where id=?",1,id);

        return ResultUtils.get("上诉成功", id);
    }

    /**
     * 驳回
     * @param httpServletRequest
     * @param servletResponse
     * @param id
     * @return
     */
    @RequestMapping(value = "/turn", method = RequestMethod.POST)
    public JSONObject turn(HttpServletRequest httpServletRequest, ServletResponse servletResponse,  @RequestParam(value = "id", required = true)String id) {
        String user = httpServletRequest.getHeader("user");

        Map<String,Object> violation = jdbcTemplate.queryForMap("select * from violation where id=?",id);
        String licensePlate = violation.get("license_plate").toString();
        String deductionScore =violation.get("deduction_score")==null?"0": violation.get("deduction_score").toString();
        String deductionAmount =violation.get("deduction_amount")==null?"0": violation.get("deduction_amount").toString();
        String detentionDay =violation.get("detention_day")==null?"0": violation.get("detention_day").toString();

        Map<String,Object> vehicleInfo = jdbcTemplate.queryForMap("select * from vehicle_info where license_plate=?",licensePlate);
        String owner=vehicleInfo.get("owner").toString();

        if(!"0".equals(deductionScore)){
            // 扣除用户分数
            jdbcTemplate.update(String.format("update user_info set surplus=if(surplus is null,0+%s,surplus+%s)  WHERE id =%s",deductionScore,deductionScore,owner));
        }

        logService.add(LogModule.violationManage, user, String.format("确认处理违章结果 车牌:%s",licensePlate), LogStatus.success);

        Map data = jdbcTemplate.queryForMap(
                "select a.*,b.code,b.name,b.phone,b.id from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where license_plate=?",
                licensePlate);

        if (data.get("code") != null) {
            messageService.add(data.get("code").toString(),
                    String.format("您的爱车 %s 违章编号:%s处罚上诉上诉成功，原处罚结果：扣除分数:%s，罚款:%s元，拘留天数：%s 天，您也可以登录 交管12123 查看详情", licensePlate, id,
                            deductionScore, deductionAmount, detentionDay));
        }

        // 如果之前是 申诉中的状态，将申诉状态改成 通过，上诉状态 0/1/2/3: 未发起/上诉中/通过/驳回
        jdbcTemplate.update("update violation set `appeal`=? where id=?",3,id);

        return ResultUtils.get("确认成功", id);
    }

    /**
     * 撤销处罚，用户上诉或管理员自行撤销
     * @param httpServletRequest
     * @param servletResponse
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancle", method = RequestMethod.POST)
    public JSONObject cancle(HttpServletRequest httpServletRequest, ServletResponse servletResponse,  @RequestParam(value = "id", required = true)String id) {
    	String user = httpServletRequest.getHeader("user");
    	
        Map<String,Object> violation = jdbcTemplate.queryForMap("select * from violation where id=?",id);
        String licensePlate = violation.get("license_plate").toString();
        String deductionScore =violation.get("deduction_score")==null?"0": violation.get("deduction_score").toString();
        String deductionAmount =violation.get("deduction_amount")==null?"0": violation.get("deduction_amount").toString();
        String detentionDay =violation.get("detention_day")==null?"0": violation.get("detention_day").toString();
        
        Map<String,Object> vehicleInfo = jdbcTemplate.queryForMap("select * from vehicle_info where license_plate=?",licensePlate);
        String owner=vehicleInfo.get("owner").toString();

        if(!"0".equals(deductionScore)){
            // 扣除用户分数
            jdbcTemplate.update(String.format("update user_info set surplus=if(surplus is null,0+%s,surplus+%s)  WHERE id =%s",deductionScore,deductionScore,owner));
        }

        logService.add(LogModule.violationManage, user, String.format("确认处理违章结果 车牌:%s",licensePlate), LogStatus.success);
        
		Map data = jdbcTemplate.queryForMap(
				"select a.*,b.code,b.name,b.phone,b.id from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where license_plate=?",
				licensePlate);

		if (data.get("code") != null) {
			messageService.add(data.get("code").toString(),
					String.format("您的爱车 %s 违章编号:%s处罚已被撤销，原处罚结果：扣除分数:%s，罚款:%s元，拘留天数：%s 天，您也可以登录 交管12123 查看详情", licensePlate, id,
							deductionScore, deductionAmount, detentionDay));
		}

		// 撤销时，状态改为 误报，确认状态 0/1/2:未确认/确认/误报
        jdbcTemplate.update("update violation set `status`=? where id=?",2,id);
		// 如果之前是 申诉中的状态，将申诉状态改成 通过，上诉状态 0/1/2/3: 未发起/上诉中/通过/驳回
        jdbcTemplate.update("update violation set `appeal`=? where id=?",2,id);

        return ResultUtils.get("确认成功", id);
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
