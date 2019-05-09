package teclan.springboot.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * @ClassName: ActiveMQConfig
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 10:25
 **/
@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${activemq.broker-url}")
    private String brokerUrl;
    @Value("${activemq.user}")
    private String user;
    @Value("${activemq.password}")
    private String password;


    @Autowired
    ActiveMQErrorHandler activeMQErrorHandler;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(user, password, brokerUrl);
        return activeMQConnectionFactory;
    }

    // topic模式的ListenerContainer
    @Bean(name = "topicListenerContainer")
    public JmsListenerContainerFactory<?> getTopicJmsListenerContainer(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }

    // queue模式的ListenerContainer
    @Bean(name = "queueListenerContainer")
    public JmsListenerContainerFactory  getQueueJmsListenerContainer(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(activeMQConnectionFactory);
        bean.setErrorHandler(activeMQErrorHandler);
        bean.setPubSubDomain(false);
        return bean;
    }


    @Bean
    public JmsMessagingTemplate getJmsMessagingTemplate(ActiveMQConnectionFactory connectionFactory) {

        JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate(connectionFactory);
        jmsMessagingTemplate.getJmsTemplate().setDeliveryPersistent(true);
        return jmsMessagingTemplate;
    }

    @Bean
    public JmsTemplate getJmsTemplate(ActiveMQConnectionFactory connectionFactory) {
        return new JmsTemplate (connectionFactory);
    }

}
