package teclan.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.utils.IdUtils;
import teclan.springboot.utils.PagesUtils;
import teclan.springboot.utils.ResultUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日志查询
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public JSONObject create(ServletRequest servletRequest, ServletResponse servletResponse,
                             @RequestParam(value = "user_id", required = false) String userId,
                             @RequestParam(value = "module", required = false) String module,
                             @RequestParam(value = "keywork", required = false) String keyword,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "orderBy" ,required = true,defaultValue = "create_time") String orderBy,
                             @RequestParam(value = "sort" ,required = true,defaultValue = "DESC")String sort,
                             @RequestParam(value = "currentPage" ,required = true,defaultValue = "1")int currentPage,
                             @RequestParam(value = "pageSize" ,required = true,defaultValue = "20")int pageSize ) {
        
        String queryForCount="select count(*) from logs where 1=1 %s";
        String querySql = "select a.*,b.name from logs a left join user_info b on a.user_code=b.code where 1=1 %s order by a.%s %s limit %s,%s";
        
        StringBuilder sb =new StringBuilder();
        
        if(!StringUtils.isNullOrEmpty(userId)) {
        	sb.append(String.format(" and user_id = %s ", userId));
        }
        
        if(!StringUtils.isNullOrEmpty(module)) {
        	sb.append(String.format(" and module = %s ", module));
        }
        
        if(!StringUtils.isNullOrEmpty(keyword)) {
        	sb.append(" and keyword like '%").append(keyword).append("%' ");
        }
        
        int totals = jdbcTemplate.queryForObject(String.format(queryForCount, sb.toString()),Integer.class);
        
        List<Map<String,Object>> maps = jdbcTemplate.queryForList(String.format(querySql, sb.toString(),orderBy,sort,PagesUtils.getOffset(currentPage, pageSize),pageSize));


        return ResultUtils.get("查询成功", maps, PagesUtils.getPageInfo(currentPage, pageSize, totals));
    }
}
