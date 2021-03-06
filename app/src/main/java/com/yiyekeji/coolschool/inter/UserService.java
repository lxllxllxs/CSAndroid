package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2016/12/25.
 */
public interface UserService {

    @POST("user/appRegister")
    Call<ResponseBean> register(@Body UserInfo user);
    @POST("user/appLogin")
    Call<ResponseBody> login(@Body UserInfo user);

    // TODO: 2017/5/9/009 Android自己登录
    @POST("user/androidLogin")
    Call<ResponseBody> androidLogin(@Body UserInfo user);

    // TODO: 2017/5/9/0010 配合Android 登录 插入用户
     @POST("user/insertUser")
    Call<ResponseBody> insertUser(@Body UserInfo user);

    @POST("user/appGetUserInfo")
    Call<ResponseBody> getUserInfo(@Body UserInfo user);

    @POST("user/appUpdateUserInfo")
    Call<ResponseBody> appUpdateUserInfo(@Body UserInfo user);

    /**
     *
     参数名称	参数类型	说明	备注	是否可以为空
     id	int	用户ID
     否
     tokenId	String	用户凭据
     否
     oldPsw	String	用户原来密码
     否
     newPsw	String	用户新密码
     否
     */
    @POST("user/appUpdatePsw")
    Call<ResponseBody> appUpdatePsw(@Body Map<String, Object> params);

    @POST("user/appFindPsw")
    Call<ResponseBody> appFindPsw(@Body Map<String, Object> params);


    @POST("user/checkDuplicateName")
    Call<ResponseBody> checkDuplicateName(@Body Map<String, Object> params);
}
