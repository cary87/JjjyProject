package com.jiujiu.autosos.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Map集合的工具类
 * author:yangweiquan
 * create:2017/5/17
 */
public class MapUtils {

    /**
     * 删除map集合中value为null的元素
     * @param map
     * @return
     */
    public static <T, V> Map<T, V> removeNullValue(Map<T, V> map) {
        List<Object> nullValueKeys = new ArrayList<>();
        for (Object keyObj : map.keySet()) {
            if (null == map.get(keyObj)) {
                nullValueKeys.add(keyObj);
            }
        }
        for (Object keyObj : nullValueKeys) {
            map.remove(keyObj);
        }

        return map;
    }
}
