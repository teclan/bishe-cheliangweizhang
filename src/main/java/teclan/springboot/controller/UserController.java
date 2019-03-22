package teclan.springboot.controller;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.utils.Objects;
import teclan.springboot.utils.ResultUtils;
import teclan.springboot.utils.SqlUtils;
import teclan.springboot.utils.TokenUtisl;

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
        if (password.equals(map.get("password").toString())) { // 登录成功
            String token = TokenUtisl.get();
            // 更新token
            jdbcTemplate.update("update user_info set token=?,last_time=? where code=?", token, new Date(), code);
            map.put("tokent", token);
            map.put("code", code);
            return ResultUtils.get("登录成功", map);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(401);
            return ResultUtils.get("登录失败,密码错误", null);
        }
    }


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JSONObject logout(ServletRequest servletRequest, String code) {
        jdbcTemplate.update("update user_info set token=null,last_time=null where code=?", code);
        return ResultUtils.get("退出成功", null);
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JSONObject register(ServletRequest servletRequest, ServletResponse servletResponse, String code, String name, String idCard, String phone, String password) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setStatus(401);

        int count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where code='%s", code), Integer.class);
        if (count > 0) {
            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，用户标志已被占用", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where id_card='%s", idCard), Integer.class);
        if (count > 0) {
            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，身份证已经被注册", null);
        }

        count = jdbcTemplate.queryForObject(String.format("select count(*) from user_info where phone='%s", phone), Integer.class);
        if (count > 0) {
            httpServletResponse.setStatus(403);
            return ResultUtils.get("注册失败，手机号已经被注册", null);
        }
        jdbcTemplate.update("insert into user_info (`code`,`name`,`id_card`,``password`,`role`) values (?,?,?,?,?);", code, name, idCard, password, "general");
        return ResultUtils.get("注册成功", null);
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JSONObject create(ServletRequest servletRequest, ServletResponse servletResponse, String code, String name, String idCard, String phone, String password) {
        return register(servletRequest,servletResponse,code,name,idCard,phone,password);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JSONObject delete(ServletRequest servletRequest, ServletResponse servletResponse, String code) {
        jdbcTemplate.update("delete from user_info where code=?", code);
        return ResultUtils.get("删除成功", null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(ServletRequest servletRequest, ServletResponse servletResponse,String code, Map<String,Object> data) {
        Object[] values = SqlUtils.getValuesForUpdate(data);
        values = Objects.merge(values,new Object[]{code});
        jdbcTemplate.update(String.format("update user_info set %s where code=?"),values);
        return ResultUtils.get("修改成功", null);
    }

}
