package teclan.springboot.avtivemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import teclan.springboot.exception.UnImplementException;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @ClassName: Queue1SenderServer
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 10:31
 **/
@Component
public class Topic1ReceiverServer extends AbstractActiveMQProDuceService  {

    private static final String TOPIC_NAME = "topic1";

    private Topic topic = new ActiveMQTopic(TOPIC_NAME);

    @Override
    protected String getTopicName() {
        return TOPIC_NAME;
    }

    @Override
    protected Topic getTopic() {
        return topic;
    }


    /**
     * 接收消息
     *
     * @param message
     */
    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicListenerContainer")
    @Override
    public void receiveTopicMsg(String message)  {
        try {
            super.receiveTopicMsg( message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
