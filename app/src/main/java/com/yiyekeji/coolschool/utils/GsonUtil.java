package com.yiyekeji.coolschool.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/5.
 */
public class GsonUtil {
    private static Gson gson = new Gson();


    /**
     * 第一层解析 不需要标签
     * @param jsonString
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T fromJSon(String jsonString,Class<T> tClass){
        if (TextUtils.isEmpty(jsonString)) {
            try {
                return tClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return gson.fromJson(jsonString,tClass);
    }
    public static <T> T fromJSon(Response<ResponseBody> response,Class<T> tClass){
        String jsonString="";
        try {
             jsonString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fromJSon(jsonString, tClass);
    }

    /**
     * 第二层 当作是内部类 需要标签
     * @param jsonString
     * @param tClass
     * @param modelTag
     * @param <T>
     * @return
     */
    public static <T> T fromJSon(String jsonString,Class<T> tClass,String modelTag){
        try {
            JSONObject object=new JSONObject(jsonString);
            return gson.fromJson(object.getString(modelTag),tClass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将ResponseBody的内容提出来 否则刷一次就空了
     * @param response
     * @return
     */
    @NonNull
    public static String toJsonString(Response<ResponseBody> response){
        if (response.body() == null) {
            return "";
        }
        try {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
