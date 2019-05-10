package teclan.springboot.utils;

import com.alibaba.fastjson.JSONObject;

public class ResultUtils {

    public static JSONObject get(String message,Object datas) {

        return get(200,message,datas);
    }

    public static JSONObject get(int code,String message,Object datas) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",message);
        jsonObject.put("datas",datas);
        jsonObject.put("code",code);
        return jsonObject;
    }

    public static JSONObject get(String message,Object datas,Object pageInfo) {
        return get(200,message,datas,pageInfo);
    }

    public static JSONObject get(int code,String message,Object datas,Object pageInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",message);
        jsonObject.put("code",code);
        jsonObject.put("datas",datas);
        jsonObject.put("pageInfo",pageInfo);

        return jsonObject;
    }
}
