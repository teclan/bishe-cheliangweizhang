package teclan.springboot.msg;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import teclan.springboot.utils.IdUtils;

@Service
public class MessageService {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public void add(String userCode,String description) {
		jdbcTemplate.update("insert into message (id,user_code,description,create_time,read) values (?,?,?,?,?)",IdUtils.get(),userCode,description,new Date(),0);
		
	}

}
