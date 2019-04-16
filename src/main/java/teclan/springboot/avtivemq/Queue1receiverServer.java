package teclan.springboot.avtivemq;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import teclan.springboot.exception.UnImplementException;
import com.alibaba.fastjson.JSONObject;
import teclan.springboot.utils.IdUtils;
import teclan.springboot.utils.ResultUtils;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.util.Date;

/**
 * @ClassName: Queue1SenderServer
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 10:31
 **/
@Component
public class Queue1receiverServer extends AbstractActiveMQProDuceService {

    private static final String QUEUE_NAME = "queue1";

    private Queue queue = new ActiveMQQueue(QUEUE_NAME);

    @Override
    protected String getQueueName() {
        return QUEUE_NAME;
    }

    @Override
    protected Queue getQueue() {
        return queue;
    }

    @Resource
    private JdbcTemplate jdbcTemplate;


    /**
     * 接收消息
     *
     * @param message
     */
    @JmsListener(destination = QUEUE_NAME, containerFactory = "queueListenerContainer")
    @Override
    public void receiveQueueMsg(Object message) {
        try {
            super.receiveQueueMsg(message);

            if (message instanceof ActiveMQTextMessage) {

                String msg = ((ActiveMQTextMessage) message).getText();

                //队列的格式：
                // {
                // "license_plate":"车牌号",
                // "type":违章类型,
                //  "zone":"区域名称",
                // }
                JSONObject jsonObject = JSON.parseObject(msg);
                String id = IdUtils.get();
                jdbcTemplate.update("insert into violation (id,license_plate,type,zone,cause,create_time) values (?,?,?,?,?,?)", id, jsonObject.getString("license_plate"), jsonObject.getString("type"), jsonObject.getString("zone"), "",new Date());
                LOGGER.info("违章插入成功，{}", message);
            } else {
                LOGGER.error("违章信息插入失败，报警信息缺失车牌信息：{}", message);
            }
        } catch (
                Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
