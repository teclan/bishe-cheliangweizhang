package teclan.springboot.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;

import teclan.springboot.constant.Constants;
import teclan.springboot.log.LogModule;
import teclan.springboot.log.LogService;
import teclan.springboot.log.LogStatus;
import teclan.springboot.model.User;
import teclan.springboot.service.RoleService;
import teclan.springboot.utils.Objects;
import teclan.springboot.utils.PagesUtils;
import teclan.springboot.utils.ResultUtils;
import teclan.springboot.utils.SqlUtils;
import teclan.springboot.utils.TokenUtils;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private LogService logService;
    @Resource
    private RoleService roleService;


    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Map<String, Object> findById(Long id) {
        Map<String, Object> datas = jdbcTemplate.queryForMap("select * from user_info where id=?", id);
        return ResultUtils.get("查询成功", datas);
    }

    @RequestMapping(value = "/findByCode", method = RequestMethod.GET)
    public JSONObject findByCode(String code) {
        Map<String, Object> datas = jdbcTemplate.queryForMap("select * from user_info where code=?", code);
        return ResultUtils.get("查询成功", datas);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JSONObject login(ServletRequest servletRequest, ServletResponse servletResponse, String code, String password) {
        Map<String, Object> map = findByCode(code);

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if(map.get("datas")==null||((Map)map.get("datas")).get("password")==null){
//            httpServletResponse.setStatus(403);
            return ResultUtils.get(403,"用户不存在或密码未设置", null);
        }

        if (password.equals(((Map)map.get("datas")).get("password").toString())) { // 登录成功
            String token = TokenUtils.get();
            // 更新token
            jdbcTemplate.update("update user_info set token=?,last_time=? where code=?", token, Constants.SDF.format(new Date()), code);

            @SuppressWarnings("unchecked")
			Map<String,Object> datas =  ( Map<String,Object>)findByCode(code).get("datas");
            datas.put("code", code);
            
            logService.add(LogModule.userManage, datas.get("code").toString(), "登录系统", LogStatus.success);
            
            return ResultUtils.get("登录成功",datas);
        } else {
//            httpServletResponse.setStatus(401);
        	 logService.add(LogModule.userManage, map.get("code").toString(), "登录系统", LogStatus.fail);
            return ResultUtils.get(403,"登录失败,密码错误", null);
        }
    }


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JSONObject logout(ServletRequest servletRequest, String code) {
        jdbcTemplate.update("update user_info set token=null,last_time=null where code=?", code);
        Map<String, Object> map = findByCode(code);
        logService.add(LogModule.userManage, ((Map<Object, Object>)map.get("datas")).get("code").toString(), "退出系统", LogStatus.success);
        return ResultUtils.get("退出成功", null);
    }


    // 默认注册普通账号
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JSONObject register(ServletRequest servletRequest, ServletResponse servletResponse, String code, String name, @RequestParam("id_card") String idCard, String phone, String password) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        int count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where code='%s'", code), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
            return ResultUtils.get(403,"注册失败，用户标志已被占用", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where id_card='%s'", idCard), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，身份证已经被注册", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where phone='%s'", phone), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，手机号已经被注册", null);
        }
        jdbcTemplate.update("insert into user_info (code,name,id_card,phone,password,role,create_time) values (?,?,?,?,?,?,?)", code, name, idCard, phone,password, "general",Constants.SDF.format(new Date()));
        
        Map<String, Object> map = findByCode(code);
        logService.add(LogModule.userManage, map.get("id").toString(), "注册成为新用户", LogStatus.success);
        
        return ResultUtils.get("注册成功", null);
    }


    // 超级管理员创建用户
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JSONObject create(HttpServletRequest httpServletRequest, ServletResponse servletResponse, String code, String name, @RequestParam("id_card") String idCard, String phone, String password,String role) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String user = httpServletRequest.getHeader("user");

       String currentRole =  roleService.getRoleInfo(user);
       if(!"superadmin".equals(currentRole)){
           return ResultUtils.get(403,"创建失败", "你不是超级管理员,没有权限创建用户");
       }


        int count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where code='%s'", code), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
            return ResultUtils.get(403,"注册失败，用户标志已被占用", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where id_card='%s'", idCard), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，身份证已经被注册", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where phone='%s'", phone), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，手机号已经被注册", null);
        }
        jdbcTemplate.update("insert into user_info (code,name,id_card,phone,password,role,create_time) values (?,?,?,?,?,?,?)", code, name, idCard, phone,password, role,Constants.SDF.format(new Date()));
        
        Map<String, Object> map = findByCode(code);

        
        logService.add(LogModule.userManage, user, String.format("创建用户 id:%s,code:%s,name:%s，role:%s", map.get("id"),map.get("code"),map.get("name"),map.get("role")), LogStatus.success);
        
        return ResultUtils.get("创建成功", null);
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JSONObject delete(HttpServletRequest httpServletRequest, ServletResponse servletResponse, String id) {
    	
    	String user = httpServletRequest.getHeader("user");

        String currentRole =  roleService.getRoleInfo(user);
        if(!"superadmin".equals(currentRole)){
            return ResultUtils.get(403,"删除失败", "你不是超级管理员,没有权限删除用户");
        }


        Map<String, Object> map =  (Map<String, Object>)findById(Long.valueOf(id)).get("datas");
    	 
        jdbcTemplate.update("delete from user_info where id=?", id);
       
        logService.add(LogModule.userManage, user, String.format("删除用户 id:%s,code:%s,name:%s", map.get("id"),map.get("code"),map.get("name"),map.get("role")), LogStatus.success);
        
        return ResultUtils.get("删除成功", null);
    }

    @RequestMapping(value = "/update/{code}", method = RequestMethod.POST)
    public JSONObject update(HttpServletRequest httpServletRequest, ServletResponse servletResponse,@PathVariable("code")  String code, @ModelAttribute User data) {
        Object[] values = SqlUtils.getValues(JSON.parseObject(JSON.toJSONString(data)));
        values = Objects.merge(values,new Object[]{code});
        jdbcTemplate.update(String.format("update user_info set %s where code=?",SqlUtils.getSqlForUpdate(JSON.parseObject(JSON.toJSONString(data)))),values);
       
        String user = httpServletRequest.getHeader("user");
        logService.add(LogModule.userManage, user, String.format("修改用户 id:%s,code:%s,name:%s", data.getId(),data.getCode(),data.getName(),data.getRole()), LogStatus.success);
        
        return ResultUtils.get("修改成功", null);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JSONObject page(ServletRequest servletRequest, ServletResponse servletResponse,String name,@RequestParam(value = "id_card" ,required = false)String idCard,String phone, @RequestParam("orderBy")String orderBy,@RequestParam("sort")String sort,@RequestParam("currentPage")int currentPage,@RequestParam("pageSize")int pageSize) {

        String countSql="SELECT count(*) FROM user_info where %s";
        String querySql = "SELECT * FROM user_info where %s ORDER BY %s %s limit %s,%s";

        StringBuilder sb =new StringBuilder(" 1=1");

        if(!StringUtils.isNullOrEmpty(name)){
            sb.append(" and name LIKE '%"+name+"%' ");
        }
        if(!StringUtils.isNullOrEmpty(idCard)){
            sb.append(" and idCard LIKE '%"+idCard+"%' ");
        }
        if(!StringUtils.isNullOrEmpty(phone)){
            sb.append(" and phone LIKE '%'"+phone+"%' ");
        }

        int totals = jdbcTemplate.queryForObject(String.format(countSql,sb.toString()),Integer.class);

        if(totals<1){
            return ResultUtils.get("查询成功", new ArrayList<>(), PagesUtils.getPageInfo(currentPage,pageSize,totals));
        }else {
            List<Map<String,Object>> datas = jdbcTemplate.queryForList(String.format(querySql,sb.toString(),orderBy,sort, PagesUtils.getOffset(currentPage,pageSize),pageSize));
            return ResultUtils.get("查询成功", datas, PagesUtils.getPageInfo(currentPage,pageSize,totals));
        }
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public JSONObject changePassword(ServletRequest servletRequest, ServletResponse servletResponse, String code,String password) {

        jdbcTemplate.update("update user_info set password=? where code=?",password,code);

        return ResultUtils.get("修改成功", null);
    }

}
