package teclan.springboot.utils;

import java.util.Map;

public class SqlUtils {

    public static String getSqlForUpdate( Map<String,Object> data){
         return Objects.joiner("=?",data.keySet());
    }

    public static String getSqlForInsert( Map<String,Object> data){
        return Objects.joiner(",",data.keySet());
    }

    public static String getFillString( Map<String,Object> data,String symbol){
        String[] fill = new String[data.size()];

        for(int i=0;i<fill.length;i++){
            fill[i]=symbol;
        }
        return Objects.joiner(",",fill);
    }

    public static Object[] getValues(Map<String,Object> data){
        StringBuilder sb1 = new StringBuilder();
        Object[] values = new Object[data.size()];
        int index=0;
        for(String key:data.keySet()){
            values[index++]=data.get(key);
        }
        return values;
    }
}
