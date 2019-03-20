package teclan.springboot.avtivemq;

import com.google.common.io.Files;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import teclan.springboot.exception.UnImplementException;

import javax.annotation.Resource;
import javax.jms.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @ClassName: AbstractActiveMQProDuceService
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 10:41
 **/
public class AbstractActiveMQProDuceService implements ActiveMQProduceService {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    protected Queue getQueue() {
        return null;
    }

    protected String getQueueName() {
        return null;
    }

    protected Topic getTopic() {
        return null;
    }

    protected String getTopicName() {
        return null;
    }

    protected JmsMessagingTemplate getJmsMessagingTemplate() {
        return jmsMessagingTemplate;
    }


    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public boolean sendTopicMsg(String message) {
        try {
            getJmsMessagingTemplate().convertAndSend(getTopic(), message);
            LOGGER.info("\nActiveMQ({}:{})发送消息{}", "队列",getTopicName(), message);
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public boolean sendQueueMsg(String message) {
        try {
            getJmsMessagingTemplate().convertAndSend(getQueue(), message);
            LOGGER.info("\nActiveMQ({}:{})发送消息{}", "队列",getQueueName(), message);
            return true;
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public boolean sendQueueMsg(Object message) {
        try {
            getJmsMessagingTemplate().convertAndSend(getQueue(), message);
            LOGGER.info("\nActiveMQ({}:{})发送消息{}", "队列",getQueueName(), message);
            return true;
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 接收消息
     *
     * @param message
     */
    @Override
    public void receiveQueueMsg(String message) {
        receiveMsg("队列", getQueueName(), message);
    }

    /**
     * 接收消息
     *
     * @param message
     */
    @Override
    public void receiveQueueMsg(Object message) {
        receiveMsg("队列", getQueueName(), message);
    }



    /**
     * 接收消息
     *
     * @param message
     */
    @Override
    public void receiveTopicMsg(String message)  {
        receiveMsg("订阅",getTopicName(),message);
    }


    protected boolean receiveMsg(String type,String name, String message) {
        LOGGER.info("\n接收到ActiveMQ（{}:{}）消息：{}", type,name, message);
        return true;
    }


    protected boolean receiveMsg(String type,String name, Object message) {
        LOGGER.info("\n接收到ActiveMQ（{}:{}）消息：{}", type,name, message);

        if(message instanceof ActiveMQBytesMessage){
            LOGGER.info("\n接收到ActiveMQ（{}:{}）消息：{}", type,name, "二进制");

            ActiveMQBytesMessage msg = (ActiveMQBytesMessage)message;
            try {
                int length =(int) msg.getBodyLength();
                byte[] body = new byte[length];
                msg.readBytes(body);
                Files.write(body,new File("to.txt"));
            } catch (JMSException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            }

        }else if(message instanceof String){
            receiveMsg(type,name,(String) message);
        }

        return true;
    }

}
