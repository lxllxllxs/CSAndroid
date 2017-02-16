package com.yiyekeji.coolschool.utils;

import com.yiyekeji.coolschool.Config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
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
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(new LoggingInterceptor())
                            .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .build())
                    .addConverterFactory(GsonConverterFactory.create())   //增加返回值为Gson的支持(以实体类返回)
                    .build();
        }
        return retrofit.create(service);
    }




    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (Config.DEBUG) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                String requestBody = buffer.readUtf8();
                System.out.println("RetrofitUtils:"+String.format("请求报文：%s",request.url()+requestBody));
            }
            /*  //检测是否过期
            if (App.userInfo!=null&&App.geTokenId().equals("false")){
                UserService service=create(UserService.class);
                UserInfo userInfo=new UserInfo();
                userInfo.setPassword(App.userInfo.getPassword());
                userInfo.setUserNum(App.userInfo.getUserNum());
                Call call = service.login(userInfo);
                chain.proceed(call.request());
            }*/
            return chain.proceed(request);
        }
    }
}
