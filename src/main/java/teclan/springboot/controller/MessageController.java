package teclan.springboot.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;

import teclan.springboot.utils.PagesUtils;
import teclan.springboot.utils.ResultUtils;

/**
 * 消息查询
 */
@RestController
@RequestMapping("/message")
public class MessageController {
	
	 @Resource
	    private JdbcTemplate jdbcTemplate;

	    @RequestMapping(value = "/get", method = RequestMethod.GET)
	    public JSONObject create(ServletRequest servletRequest, ServletResponse servletResponse,
	                             @RequestParam(value = "user_id", required = false) String userId,
	                             @RequestParam(value = "keywork", required = false) String keyword,
	                             @RequestParam(value = "read", required = false) String read,
	                             @RequestParam(value = "orderBy" ,required = true,defaultValue = "create_time") String orderBy,
	                             @RequestParam(value = "sort" ,required = true,defaultValue = "DESC")String sort,
	                             @RequestParam(value = "currentPage" ,required = true,defaultValue = "1")int currentPage,
	                             @RequestParam(value = "pageSize" ,required = true,defaultValue = "20")int pageSize ) {
	        
	        String queryForCount="select count(*) from message where 1=1 %s";
	        String querySql = "select * from message where 1=1 %s order by %s %s limit %s,%s";
	        
	        StringBuilder sb =new StringBuilder();
	        
	        if(!StringUtils.isNullOrEmpty(userId)) {
	        	sb.append(String.format(" and user_id = %s ", userId));
	        }
	        
	        if(!StringUtils.isNullOrEmpty(keyword)) {
	        	sb.append(" and keyword like '%").append(keyword).append("%' ");
	        }
	        
	        if(!StringUtils.isNullOrEmpty(read)) {
	        	sb.append(String.format(" and read = %s ", read));
	        }
	        
	        
	        int totals = jdbcTemplate.queryForObject(String.format(queryForCount, sb.toString()),Integer.class);
	        
	        List<Map<String,Object>> maps = jdbcTemplate.queryForList(String.format(querySql, sb.toString(),orderBy,sort,PagesUtils.getOffset(currentPage, pageSize),pageSize));

	        return ResultUtils.get("查询成功", maps, PagesUtils.getPageInfo(currentPage, pageSize, totals));
	    }
	    

	    // 查看消息详细信息，触发已读
	    @RequestMapping(value = "/findById", method = RequestMethod.GET)
	    public JSONObject findById(ServletRequest servletRequest, ServletResponse servletResponse,
	                             @RequestParam(value = "id", required = true) String id) {
	        String querySql = "select * from message where id=?";
	        
	        Map<String,Object> map = jdbcTemplate.queryForMap(querySql,id);
	        
	        jdbcTemplate.update("update message set read=? where id=?",1,id);

	        return ResultUtils.get("查询成功", map);
	    }


}
