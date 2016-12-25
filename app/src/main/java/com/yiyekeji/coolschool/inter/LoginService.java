package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.UserInfoFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2016/12/25.
 */
public interface LoginService {
    @POST("login")
    Call<ResponseBody> listRepos(@Body UserInfoFactory.UserInfo user);
}
