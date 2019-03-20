package teclan.springboot.avtivemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @ClassName: Queue1SenderServer
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 10:31
 **/
@Component
public class Topic1SenderServer extends AbstractActiveMQProDuceService  {

    private static final String TOPIC_NAME="topic1";

    private static Topic topic = new ActiveMQTopic(TOPIC_NAME);

    @Override
    protected  String getTopicName(){
        return TOPIC_NAME;
    }

    @Override
    protected Topic getTopic() {
        return topic;
    }

}
