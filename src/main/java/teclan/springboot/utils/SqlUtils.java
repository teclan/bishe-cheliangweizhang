package teclan.springboot.utils;

import java.util.Map;

public class SqlUtils {

    public static String getSqlForUpdate( Map<String,Object> data){
         return Objects.Joiner("=?",data.keySet());
    }

    public static Object[] getValuesForUpdate( Map<String,Object> data){
        StringBuilder sb1 = new StringBuilder();
        Object[] values = new Object[data.size()];
        int index=0;
        for(String key:data.keySet()){
            values[index++]=data.get(key);
        }
        return values;
    }
}
