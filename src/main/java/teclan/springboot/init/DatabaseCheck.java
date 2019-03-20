package teclan.springboot.init;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @ClassName: DatabaseInit
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/9 15:52
 **/
@Component
public class DatabaseCheck {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseCheck.class);

    @Autowired
    private DataSource dataSource;

    public  void run (){
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            LOGGER.error(
                    "数据库迁移检验失败,表结构可能已经修改,建议迁移之前将 migrateClean 设为 true,如果是生产环境请联系DBA确认后重试 !");
            throw e;
        }
    }
}
