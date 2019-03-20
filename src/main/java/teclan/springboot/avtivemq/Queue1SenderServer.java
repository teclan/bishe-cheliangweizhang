package teclan.springboot.avtivemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @ClassName: Queue1SenderServer
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 10:31
 **/
@Component
public class Queue1SenderServer extends AbstractActiveMQProDuceService  {

    private static final String QUEUE_NAME="queue1";

    private static Queue queue = new ActiveMQQueue(QUEUE_NAME);

    @Override
    protected  String getQueueName(){
        return QUEUE_NAME;
    }

    @Override
    protected Queue getQueue() {
        return queue;
    }

}
