package teclan.springboot.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtils {

    public static Map<String,Object> TOKENS = new ConcurrentHashMap<String,Object>();

    public static void put(String user,Object token){
        TOKENS.put(user,token);
    }

    public static void remove(String user){
        TOKENS.remove(user);
    }

    public static Object get(String user){
       return  TOKENS.get(user);
    }
}
