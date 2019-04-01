package teclan.springboot.utils;

import java.util.HashMap;
import java.util.Map;

public class PagesUtils {

    public static Map<String, Object> getPageInfo(int currentPage, int pageSize, int totals) {
        Map<String, Object> map = new HashMap<>();
        map.put("totalPages", Math.ceil(totals * .10 / pageSize));
        map.put("currentPage", currentPage);
        map.put("pageSize", pageSize);
        map.put("totals", totals);
        return map;
    }

    public static int getOffset(int currentPage, int totals) {
        return currentPage>0?(currentPage-1)*totals:0;
    }
}
