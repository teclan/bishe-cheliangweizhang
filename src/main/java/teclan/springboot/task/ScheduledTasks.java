package teclan.springboot.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import teclan.springboot.constant.Constants;

import javax.annotation.Resource;


@Component
public class ScheduledTasks {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	// fixedDelay : 方法执行结束后，等待指定时间后执行下一次
	// fixedRate : 两个方法开始执行的时间间隔>=指定时间，
	// 如果方法体执行耗时小于指定时间，则等待到指定时间再执行下一次，
	// 如果方法体执行耗时大于等于指定时间，则立即执行下一次
	
	@Scheduled(fixedRate=3600000)
	public void reportCurrentSystem() {
		LOGGER.info("检查token是否过期{} ",new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date()));

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -30);

		try {
			jdbcTemplate.update("update user_info set token=?,last_time=? where last_time<?",null,null, Constants.SDF.format(calendar.getTime()));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

}
