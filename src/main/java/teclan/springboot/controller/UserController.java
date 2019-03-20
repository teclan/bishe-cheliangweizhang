package teclan.springboot.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.constant.Constants;
import teclan.springboot.dao.UserInfoMapper;
import teclan.springboot.entity.Users1;
import teclan.springboot.utils.SessionUtils;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Map<String, Object> get(Long id) {
        return jdbcTemplate.queryForMap("select * from user_info where id=?", id);
    }

    @RequestMapping(value = "/findByCode", method = RequestMethod.GET)
    public Map<String, Object> findByCode(String code) {
        return jdbcTemplate.queryForMap("select * from user_info where code=?", code);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(ServletRequest servletRequest, String code, String password) {

        Map<String, Object> map = findByCode(code);
        if (password.equals(map.get("password").toString())) { // 登录成功
            SessionUtils.put(code,"12345");
            return map;
        }

        return null;
    }


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Map<String, Object> logout(ServletRequest servletRequest, String code) {
        SessionUtils.remove(code);
        return null;
    }


}
