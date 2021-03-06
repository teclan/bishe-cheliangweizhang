package teclan.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import teclan.springboot.log.LogModule;
import teclan.springboot.log.LogService;
import teclan.springboot.log.LogStatus;
import teclan.springboot.utils.Objects;
import teclan.springboot.utils.PagesUtils;
import teclan.springboot.utils.ResultUtils;

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
 * 车辆信息
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private LogService logService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JSONObject create(HttpServletRequest httpServletRequest, ServletResponse servletResponse, @RequestParam("engine_no") String engineNo, @RequestParam("frame") String frame, @RequestParam("qualified_no") String qualifiedNo, @RequestParam("vehicle_license") String vehicleLicense, @RequestParam("license_plate") String licensePlate, @RequestParam(value = "owner", required = false) String owner) {
    	 String user = httpServletRequest.getHeader("user");
    	 
    	int count = jdbcTemplate.queryForObject(String.format("select count(*) from vehicle_info  where license_plate='%s'", licensePlate), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
        	logService.add(LogModule.vehicleManage, user, String.format("创建车辆 车牌:%s",licensePlate), LogStatus.fail);
            return ResultUtils.get(500,"添加失败，车牌号重复", licensePlate);
            
        } else {
            jdbcTemplate.update("insert into vehicle_info (engine_no,frame,qualified_no,vehicle_license,license_plate,owner,register_at ) values (?,?,?,?,?,?,?)", engineNo, frame, qualifiedNo, vehicleLicense, licensePlate, owner, new Date());
           
            logService.add(LogModule.vehicleManage, user, String.format("创建车辆 车牌:%s",licensePlate), LogStatus.success);
            return ResultUtils.get("添加成功", licensePlate);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JSONObject delete(HttpServletRequest httpServletRequest, ServletResponse servletResponse, String id) {
    	 String user = httpServletRequest.getHeader("user");
    	 
    	 List<Map<String,Object>> datas = jdbcTemplate.queryForList("select a.*,b.name,b.phone from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where a.id=?", id);
    	 
        jdbcTemplate.update("delete from vehicle_info where id=?", id);
        
        logService.add(LogModule.vehicleManage, user, String.format("创建车辆 车牌:%s",datas.get(0)!=null?"":datas.get(0).get("license_plate")), LogStatus.success);
        
        return ResultUtils.get("删除成功", id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(HttpServletResponse httpServletRequest, ServletResponse servletResponse, String id, @RequestParam("engine_no") String engineNo, @RequestParam("frame") String frame, @RequestParam("qualified_no") String qualifiedNo, @RequestParam("vehicle_license") String vehicleLicense, @RequestParam("license_plate") String licensePlate, @RequestParam(value = "owner") String owner) {
        String user = httpServletRequest.getHeader("user");
        
        if (Objects.isNull(id)) {
//            httpServletResponse.setStatus(500);
            return ResultUtils.get(500,"未指定记录ID", null);
        }

        int count = jdbcTemplate.queryForObject(String.format("select count(*) from vehicle_info  where license_plate='%s' and id<>%s", licensePlate, id), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
            return ResultUtils.get(403,"修改失败，车牌号重复", licensePlate);
        }
        
        List<Map<String,Object>> datas = jdbcTemplate.queryForList("select a.*,b.name,b.phone from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where a.id=?", id);
    	
        jdbcTemplate.update("update   vehicle_info set engine_no=?,frame=?,qualified_no=?,vehicle_license=?,license_plate=?,owner=?,update_at=? where id=?", engineNo, frame, qualifiedNo, vehicleLicense, licensePlate, owner, new Date(), id);

        logService.add(LogModule.vehicleManage, user, String.format("修改车辆 车牌:%s",datas.get(0)!=null?"":datas.get(0).get("license_plate")), LogStatus.success);
        
        return ResultUtils.get("修改成功", id);
    }

    @RequestMapping(value = "/findByLicensePlate", method = RequestMethod.POST)
    public JSONObject findByLicensePlate(ServletRequest servletRequest, ServletResponse servletResponse, String licensePlate) {
        Map data = jdbcTemplate.queryForMap("select a.*,b.code,b.name,b.phone,b.id from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where license_plate=?", licensePlate);
        return ResultUtils.get("查询成功", data);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public JSONObject findById(ServletRequest servletRequest, ServletResponse servletResponse, String id) {
        List<Map<String,Object>> datas = jdbcTemplate.queryForList("select a.*,b.name,b.phone from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where a.id=?", id);
        return ResultUtils.get("查询成功", datas.isEmpty()||datas.size()==0?new ArrayList<>():datas.get(0));
    }

    @RequestMapping(value = "/findByUserId", method = RequestMethod.POST)
    public JSONObject findByUserId(ServletRequest servletRequest, ServletResponse servletResponse, String id) {
        List<Map<String,Object>> datas = jdbcTemplate.queryForList("select a.*,b.name,b.phone from vehicle_info a LEFT JOIN user_info b on a.owner=b.id where b.id=?", id);
        return ResultUtils.get("查询成功", datas);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JSONObject page(ServletRequest servletRequest, ServletResponse servletResponse, String keyword, @RequestParam(value = "orderBy" ,required = true,defaultValue = "register_at") String orderBy, @RequestParam(value = "sort" ,required = true,defaultValue = "DESC")String sort, @RequestParam(value = "currentPage" ,required = true,defaultValue = "1")int currentPage, @RequestParam(value = "pageSize" ,required = true,defaultValue = "20")int pageSize) {
        String countSql = "SELECT count(*) FROM vehicle_info a LEFT JOIN user_info b ON a.owner=b.id WHERE 1=1";
        String querySql = "SELECT a.*,b.code,b.name,b.id_card,b.`role`,b.phone FROM vehicle_info a LEFT JOIN user_info b ON a.owner=b.id WHERE 1=1 ";
        if (!StringUtils.isNullOrEmpty(keyword)) {
            countSql = "SELECT count(*) FROM vehicle_info a LEFT JOIN user_info b ON a.owner=b.id WHERE a.license_plate LIKE '%" + keyword + "%'";
            querySql = "SELECT a.*,b.code,b.name,b.id_card,b.`role`,b.phone FROM vehicle_info a LEFT JOIN user_info b ON a.owner=b.id WHERE a.license_plate LIKE '%" + keyword + "%' ";

        }
        int totals = jdbcTemplate.queryForObject(countSql, Integer.class);

        if (totals < 1) {
            return ResultUtils.get("查询成功", new ArrayList<>(), PagesUtils.getPageInfo(currentPage, pageSize, totals));
        } else {
            List<Map<String, Object>> datas = jdbcTemplate.queryForList(querySql+" order by "+orderBy+ " "+sort +" limit "+PagesUtils.getOffset(currentPage, pageSize)+","+ pageSize);
            return ResultUtils.get("查询成功", datas, PagesUtils.getPageInfo(currentPage, pageSize, totals));
        }
    }

}
