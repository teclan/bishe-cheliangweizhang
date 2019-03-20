package teclan.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.avtivemq.Queue1SenderServer;
import teclan.springboot.avtivemq.Topic1SenderServer;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ClassName: ActiveMQController
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 11:12
 **/
@RestController
public class ActiveMQController {
    private  static  final Logger LOGGER = LoggerFactory.getLogger(ActiveMQController.class);

    @Resource
    Queue1SenderServer queue1SenderServer;
    @Resource
    Topic1SenderServer topic1SenderServer;

    @RequestMapping("/send/queue1")
    public String sendQueue1() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","teclan");
        jsonObject.put("type","queue");
        queue1SenderServer.sendQueueMsg(JSON.toJSONString(jsonObject).getBytes());
        return "200";
    }

    @RequestMapping("/send/file")
    public String sendFile() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","teclan");
        jsonObject.put("type","queue");
        try {
            queue1SenderServer.sendQueueMsg(Files.readAllBytes(new File(getClass().getClassLoader().getResource("log4j2.properties").getPath()).toPath()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return "200";
    }

    @RequestMapping("/send/topic1")
    public String sendTopic1() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","teclan");
        jsonObject.put("type","topic");
        topic1SenderServer.sendTopicMsg(JSON.toJSONString(jsonObject));
        return "200";
    }
}
