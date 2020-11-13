package com.fanhao.businessplatform.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Gson反序列化相关工具
 * @author 范昊
 */
public class GsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GsonUtils.class);

    private static final Gson gson = new Gson();

    /**
     * 将JSON转为List
     * @param t
     * @param json
     * @param <T>
     * @return
     */
    public static <T> List<T> parseStringToList(final Class<T> t, final String json) {
        try {
            List<T> result = gson.fromJson(json, new TypeToken<ArrayList<T>>() {
            }.getType());
            return result;
        }catch (Exception e){
            LOGGER.error("GsonUtils.parseStringToList() —— List反序列化失败");
        }
        return null;
    }

    /**
     * 快速获取json
     * @return
     */
    public static Gson getGson() {
        return gson;
    }


}
