package teclan.springboot.filter;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import teclan.springboot.constant.Constants;
import teclan.springboot.utils.TokenUtisl;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class SessionFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFilter.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.getAttribute(Constants.TOKEN);
        HttpServletRequest httpServletRequest =(HttpServletRequest)servletRequest;
        String user = httpServletRequest.getHeader("user");
        String token =httpServletRequest.getHeader("token");

        String realToken = jdbcTemplate.queryForObject(String.format("select token from user_info where code='%s'",user),String.class);

        HttpServletResponse httpServletResponse =(HttpServletResponse) servletResponse;
        if ( !"/user/login".equals(httpServletRequest.getRequestURI()) && (token == null || "".equals(token.toString()) || !token.equals(realToken) )) {
            LOGGER.error("未认证");
            httpServletResponse.setStatus(401);
            jdbcTemplate.update("update user_info set token=?,last_time=? where code=?",null,new Date(),user);
        }else {
            jdbcTemplate.update("update user_info set last_time=? where code=?",new Date(),user);
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}

