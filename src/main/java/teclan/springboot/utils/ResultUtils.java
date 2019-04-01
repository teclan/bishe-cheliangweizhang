package teclan.springboot.utils;

import com.alibaba.fastjson.JSONObject;

public class ResultUtils {

    public static JSONObject get(String message,Object datas) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",message);
        jsonObject.put("datas",datas);

        return jsonObject;
    }

    public static JSONObject get(String message,Object datas,Object pageInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",message);
        jsonObject.put("datas",datas);
        jsonObject.put("pageInfo",pageInfo);

        return jsonObject;
    }
}
