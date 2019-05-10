package teclan.springboot.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import teclan.springboot.constant.Constants;
import teclan.springboot.model.User;
import teclan.springboot.utils.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Map<String, Object> get(Long id) {
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

            Map<String,Object> datas =  ( Map<String,Object>)findByCode(code).get("datas");
            datas.put("code", code);
            return ResultUtils.get("登录成功",datas);
        } else {
//            httpServletResponse.setStatus(401);
            return ResultUtils.get(403,"登录失败,密码错误", null);
        }
    }


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JSONObject logout(ServletRequest servletRequest, String code) {
        jdbcTemplate.update("update user_info set token=null,last_time=null where code=?", code);
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
            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，身份证已经被注册", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where phone='%s'", phone), Integer.class);
        if (count > 0) {
            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，手机号已经被注册", null);
        }
        jdbcTemplate.update("insert into user_info (code,name,id_card,phone,password,role,create_time) values (?,?,?,?,?,?,?)", code, name, idCard, phone,password, "general",Constants.SDF.format(new Date()));
        return ResultUtils.get("注册成功", null);
    }


    // 默认创建管理员
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JSONObject create(ServletRequest servletRequest, ServletResponse servletResponse, String code, String name, @RequestParam("id_card") String idCard, String phone, String password) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        int count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where code='%s'", code), Integer.class);
        if (count > 0) {
//            httpServletResponse.setStatus(403);
            return ResultUtils.get(403,"注册失败，用户标志已被占用", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where id_card='%s'", idCard), Integer.class);
        if (count > 0) {
            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，身份证已经被注册", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where phone='%s'", phone), Integer.class);
        if (count > 0) {
            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，手机号已经被注册", null);
        }
        jdbcTemplate.update("insert into user_info (code,name,id_card,phone,password,role,create_time) values (?,?,?,?,?,?,?)", code, name, idCard, phone,password, "general",Constants.SDF.format(new Date()));
        return ResultUtils.get("注册成功", null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JSONObject delete(ServletRequest servletRequest, ServletResponse servletResponse, String code) {
        jdbcTemplate.update("delete from user_info where code=?", code);
        return ResultUtils.get("删除成功", null);
    }

    @RequestMapping(value = "/update/{code}", method = RequestMethod.POST)
    public JSONObject update(ServletRequest servletRequest, ServletResponse servletResponse,@PathVariable("code")  String code, @ModelAttribute User data) {
        Object[] values = SqlUtils.getValues(JSON.parseObject(JSON.toJSONString(data)));
        values = Objects.merge(values,new Object[]{code});
        jdbcTemplate.update(String.format("update user_info set %s where code=?",SqlUtils.getSqlForUpdate(JSON.parseObject(JSON.toJSONString(data)))),values);
        return ResultUtils.get("修改成功", null);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JSONObject page(ServletRequest servletRequest, ServletResponse servletResponse,String name,@RequestParam(value = "id_card" ,required = false)String idCard,String phone, @RequestParam("orderBy")String orderBy,@RequestParam("sort")String sort,@RequestParam("currentPage")int currentPage,@RequestParam("pageSize")int pageSize) {

        String countSql="SELECT count(*) FROM user_info where %s";
        String querySql = "SELECT * FROM user_info where %s ORDER BY %s %s limit %s,%s";

        StringBuilder sb =new StringBuilder(" 1=1");

        if(!StringUtils.isNullOrEmpty(name)){
            sb.append(" and name LIKE '%"+name+"%') ");
        }
        if(!StringUtils.isNullOrEmpty(idCard)){
            sb.append(" and idCard LIKE '%"+idCard+"%' ");
        }
        if(!StringUtils.isNullOrEmpty(phone)){
            sb.append(" and phone LIKE '%'"+phone+"%') ");
        }

        int totals = jdbcTemplate.queryForObject(String.format(countSql,sb.toString()),Integer.class);

        if(totals<1){
            return ResultUtils.get("查询成功", new ArrayList<>(), PagesUtils.getPageInfo(currentPage,pageSize,totals));
        }else {
            List<Map<String,Object>> datas = jdbcTemplate.queryForList(String.format(querySql,sb.toString(),orderBy,sort, PagesUtils.getOffset(currentPage,totals),pageSize));
            return ResultUtils.get("查询成功", datas, PagesUtils.getPageInfo(currentPage,pageSize,totals));
        }
    }

    @RequestMapping(value = "/changePassword/{code}", method = RequestMethod.POST)
    public JSONObject changePassword(ServletRequest servletRequest, ServletResponse servletResponse,@PathVariable("code")  String code,String password) {

        jdbcTemplate.update(String.format("update user_info set password=? where code=?",password,code);

        return ResultUtils.get("修改成功", null);
    }

}
