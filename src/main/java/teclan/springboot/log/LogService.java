package teclan.springboot.log;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import teclan.springboot.utils.IdUtils;

@Service
public class LogService {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public void add(String logModule,String userId,String description,LogStatus logStatus) {
		jdbcTemplate.update("insert into logs (id,user_code,module,description,status,create_time) values (?,?,?,?,?,?)",IdUtils.get(),userId,logModule,description,logStatus.getValue(),new Date());
	}

}
