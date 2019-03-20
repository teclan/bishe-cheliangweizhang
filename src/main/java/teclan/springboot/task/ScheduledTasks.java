package teclan.springboot.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
	
	
	// fixedDelay : 方法执行结束后，等待指定时间后执行下一次
	// fixedRate : 两个方法开始执行的时间间隔>=指定时间，
	// 如果方法体执行耗时小于指定时间，则等待到指定时间再执行下一次，
	// 如果方法体执行耗时大于等于指定时间，则立即执行下一次
	
	@Scheduled(fixedRate=5000)
	public void reportCurrentSystem() {
		LOGGER.info("\n===={} ",new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date()));
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

}
