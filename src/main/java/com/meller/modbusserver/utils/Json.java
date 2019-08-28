package com.meller.modbusserver.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenleijun
 * @date 2017年8月22日
 * @remark Json工具类
 */
public final class Json {

    /**
     * jackson map
     */
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 设置JSON时间格式
     */
    private static SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        mapper.setDateFormat(myDateFormat);
        //json串中有key为A，但指定转换的mybean中未定义属性A，会抛异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private Json() {
    }

    /**
     * 将bean转换成json
     *
     * @param obj bean对象
     * @return json
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            LogFactory.getLog(Json.class).error("序列化对象失败!");
            return null;
        }
    }

    /**
     * 把json字符串转换为相应的JavaBean对象
     *
     * @param json json数据
     * @param type bean 类型
     * @param <T>  泛型
     * @return bean
     */
    public static <T> T toBean(String json, Class<T> type) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log(json);
            return null;
        }
    }

    /**
     * 将json转换成指定类型的集合
     *
     * @param json
     * @param elementClasses
     * @param <T>
     * @return List
     */
    public static <T> T toList(String json, Class<?>... elementClasses) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(List.class, elementClasses));
        } catch (IOException e) {
            log(json);
            return null;
        }
    }

    /**
     * 将json字符串转换为HashMap
     *
     * @param json json
     * @return hashmap
     */
    public static Map toMap(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return mapper.readValue(json, HashMap.class);
        } catch (IOException e) {
            log(json);
            return null;
        }
    }

    /**
     * 输出错误的JSON字符串到日志
     *
     * @param str JSON字符串
     */
    private static void log(String str) {
        LogFactory.getLog(Json.class).error("非法的JSON字符串：" + str);
    }
}
