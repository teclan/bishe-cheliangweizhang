package teclan.springboot.avtivemq;

import teclan.springboot.exception.UnImplementException;

/**
 * @ClassName: ActiveMQProduceService
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 10:32
 **/
public interface ActiveMQProduceService {

    /**
     * 发送消息
     * @param message
     */
    public boolean sendQueueMsg(String message);

    /**
     * 发送消息
     * @param message
     */
    public boolean sendQueueMsg(Object message);


    /**
     * 接收消息
     * @param message
     */
    public void receiveQueueMsg(String message) ;

    /**
     * 接收消息
     * @param message
     */
    public void receiveQueueMsg(Object message) ;


    /**
     * 发送消息
     * @param message
     */
    public boolean sendTopicMsg(String message);

    /**
     * 接收消息
     * @param message
     */
    public void receiveTopicMsg(String message)  ;
}
