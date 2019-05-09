package teclan.springboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import teclan.springboot.filter.SessionFilter;

import javax.annotation.Resource;

//@Configuration
public class FilterConfig {

//    @Resource
//    private SessionFilter sessionFilter;
//
//    @Bean
//    public FilterRegistrationBean registFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(sessionFilter);
//        registration.addUrlPatterns("/*");
//        registration.setName("SessionFilter");
//        registration.setOrder(1);
//        return registration;
//    }

}
