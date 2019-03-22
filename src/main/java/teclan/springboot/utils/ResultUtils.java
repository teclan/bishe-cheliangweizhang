package teclan.springboot.utils;

import com.alibaba.fastjson.JSONObject;

public class ResultUtils {

    public static JSONObject get(String message,Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",message);
        jsonObject.put("datas",data);

        return jsonObject;
    }
}
