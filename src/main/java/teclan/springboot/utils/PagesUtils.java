package teclan.springboot.utils;

import java.util.HashMap;
import java.util.Map;

public class PagesUtils {

    public static Map<String, Object> getPageInfo(int currentPage, int pageSize, int totals) {
        Map<String, Object> map = new HashMap<>();
        map.put("totalPages", Math.ceil(totals * 1.0 / pageSize));
        map.put("currentPage", currentPage);
        map.put("pageSize", pageSize);
        map.put("totals", totals);
        map.put("isFirst", currentPage==1); // 是否第一页
        map.put("isLast", currentPage==Math.ceil(totals * 1.0 / pageSize)); // 是否最后一页

        return map;
    }

    public static int getOffset(int currentPage, int totals) {
        return currentPage>0?(currentPage-1)*totals:0;
    }
}
