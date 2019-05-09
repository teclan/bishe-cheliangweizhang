package teclan.springboot.filter;

import com.mysql.cj.util.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import teclan.springboot.constant.Constants;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class SessionFilter implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFilter.class);

    @Resource
    private JdbcTemplate jdbcTemplate;


    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest httpServletRequest, HttpServletResponse servletResponse, Object handler){
        String user = httpServletRequest.getHeader("user");
        String token =httpServletRequest.getHeader("token");
        String realToken="";

        HttpServletResponse httpServletResponse =(HttpServletResponse) servletResponse;

        if(StringUtils.isNullOrEmpty(user)){
            LOGGER.error("\n\n {} , token无效,缺失[user]字段 ...",httpServletRequest.getRequestURI());
            httpServletResponse.setStatus(403);
            return false;
        }else{
            realToken = jdbcTemplate.queryForObject(String.format("select token from user_info where code='%s'",user),String.class);
        }


        if ( !"/user/login".equals(httpServletRequest.getRequestURI()) && (token == null || "".equals(token.toString()) || !token.equals(realToken) )) {
            LOGGER.error("未认证");
            httpServletResponse.setStatus(401);
            jdbcTemplate.update("update user_info set token=?,last_time=? where code=?",null,Constants.SDF.format(new Date()),user);
            return false;
        }else {
            jdbcTemplate.update("update user_info set last_time=? where code=?",new Date(),user);
            return true;
        }
    }

}

