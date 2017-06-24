package com.cielo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * Created by 63289 on 2017/6/19.
 */
public class JSONUtils {
    public static String toJSON(Object o) {
        return JSON.toJSONString(o);
    }

    public static Map<String, Object> json2Map(String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty()) return null;
        Map<String, Object> map = JSON.parseObject(jsonStr, Map.class);
        return map;
    }
    public static void main(String[] args){
        String jsonStr="{\"a\":\"b\",\"c\":\"d\"}";
        Map map=json2Map(jsonStr);
        System.out.println(map.toString());
        map.put("e","f");
        System.out.println(map.toString());
    }
}