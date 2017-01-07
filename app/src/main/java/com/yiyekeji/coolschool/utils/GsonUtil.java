package com.yiyekeji.coolschool.utils;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/5.
 */
public class GsonUtil {
    private static Gson gson = new Gson();
    public static <T> T fromJSon(Response<ResponseBody> json, Class<T> tClass){
        try {
            return gson.fromJson(json.body().string(),tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static <T> T fromJSon(String jsonString,Class<T> tClass){
        return gson.fromJson(jsonString,tClass);
    }
}
