package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/2/7.
 */
public class TransparentLoginAty extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login();
    }

    private void login() {
        final UserInfo user = new UserInfo();
        user.setUserNum(App.userInfo.getUserNum());
        user.setPassword(App.userInfo.getPassword());
        user.setImei(CommonUtils.getIMEI());
        UserService userService = RetrofitUtil.create(UserService.class);
        Call<ResponseBody> call= userService.login(user);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                UserInfo  userInfo= GsonUtil.fromJSon(jsonString,UserInfo.class,"userInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (userInfo!=null) {
                    App.userInfo=userInfo;
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showShortToast(rb.getMessage());
                    startActivity(LoginActivity.class);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(t.toString());
            }
        });
    }
}
