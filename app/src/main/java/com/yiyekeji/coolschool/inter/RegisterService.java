package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2017/1/5.
 */
public interface RegisterService {
    @POST("user/appRegister")
    Call<ResponseBean> register(@Body UserInfo user);
}
