package teclan.springboot.avtivemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import teclan.springboot.exception.UnImplementException;

import javax.annotation.Resource;
import javax.jms.Queue;

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


    /**
     * 接收消息
     *
     * @param message
     */
    @JmsListener(destination = QUEUE_NAME, containerFactory = "queueListenerContainer")
    @Override
    public void receiveQueueMsg(Object message)   {
        try {
             super.receiveQueueMsg( message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
