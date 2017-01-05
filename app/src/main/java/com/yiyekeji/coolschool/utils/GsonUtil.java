package com.yiyekeji.coolschool.utils;

import com.google.gson.Gson;

import java.io.Reader;

/**
 * Created by lxl on 2017/1/5.
 */
public class GsonUtil {
    private static Gson gson = new Gson();
    public static <T> T fromJSon(Reader json,Class<T> tClass){
        return gson.fromJson(json,tClass);
    }
}
