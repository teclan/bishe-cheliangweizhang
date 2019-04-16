package teclan.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages="teclan.springboot",exclude = DataSourceAutoConfiguration.class)
//@EnableScheduling
//@PropertySource(value = { "application.yml"})// 指定读取配置文件的路径
public class Main {
	public static void main(String[] args) {
		 SpringApplication.run(Main.class, args);
	}
}
