package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2016/12/25.
 */
public interface LoginService {
    @POST("user/appLogin")
    Call<ResponseBody> login(@Body UserInfo user);

    @POST("user/appGetUserInfo")
    Call<ResponseBody> getUserInfo(@Body UserInfo user);
}
