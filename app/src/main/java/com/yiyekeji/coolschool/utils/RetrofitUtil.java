package com.yiyekeji.coolschool.utils;

import com.yiyekeji.coolschool.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lxl on 2017/1/5.
 */
public class RetrofitUtil {
    private    static   Retrofit retrofit;
    private static final int DEFAULT_TIMEOUT=60;
    public  static <T> T create(final Class<T> service){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .client( new OkHttpClient.Builder()
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .build())
                    .addConverterFactory(GsonConverterFactory.create())   //增加返回值为Gson的支持(以实体类返回)
                    .build();
        }
        return retrofit.create(service);
    }
}
