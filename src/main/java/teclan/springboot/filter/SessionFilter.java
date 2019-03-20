package teclan.springboot.filter;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teclan.springboot.constant.Constants;
import teclan.springboot.utils.SessionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.getAttribute(Constants.TOKEN);
        HttpServletRequest httpServletRequest =(HttpServletRequest)servletRequest;
        String user = httpServletRequest.getHeader("user");
        Object token = SessionUtils.get(user);
        HttpServletResponse httpServletResponse =(HttpServletResponse) servletResponse;
        if ( !"/user/login".equals(httpServletRequest.getRequestURI()) && (token == null || "".equals(token.toString()))) {
            LOGGER.error("未认证");
            httpServletResponse.setStatus(401);
        }else {

            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}

