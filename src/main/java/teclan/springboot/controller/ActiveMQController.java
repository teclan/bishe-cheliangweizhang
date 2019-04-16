package teclan.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.avtivemq.Queue1SenderServer;
import teclan.springboot.avtivemq.Topic1SenderServer;
import teclan.springboot.utils.HttpTool;
import teclan.springboot.utils.ResultUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        public JSONObject sendQueue1(ServletRequest servletRequest) {
       String message = HttpTool.readJSONString((HttpServletRequest)servletRequest);
        queue1SenderServer.sendQueueMsg(message);
        return ResultUtils.get("推送成功", message);
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
