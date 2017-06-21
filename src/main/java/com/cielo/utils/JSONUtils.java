package com.cielo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 63289 on 2017/6/19.
 */
public class JSONUtils {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSON(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public static <T> T fromJSON(String jsonStr, Class<T> pojoClass) throws IOException {
        return objectMapper.readValue(jsonStr, pojoClass);
    }

    public static Map<String, Object> parseMap(String jsonStr) throws IOException {
        return (jsonStr==null||jsonStr.isEmpty())? null: objectMapper.readValue(jsonStr, Map.class);
    }
}