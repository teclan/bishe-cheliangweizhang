package teclan.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

/**
 * @ClassName: ActiveMQErrorHandler
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 11:50
 **/
@Service
public class ActiveMQErrorHandler implements ErrorHandler {
private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQErrorHandler.class);

    /**
     * Handle the given error, possibly rethrowing it as a fatal exception.
     *
     * @param t
     */
    @Override
    public void handleError(Throwable t) {
        LOGGER.error(t.getMessage(),t);
    }
}