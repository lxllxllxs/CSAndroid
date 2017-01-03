package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.google.gson.Gson;
import com.yiyekeji.coolschool.Config;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.LoginService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.LogUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    private void  test(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginService loginService = retrofit.create(LoginService.class);
        UserInfo userInfo = new UserInfo();
        userInfo.setPwd("123");
        userInfo.setName("lxl");
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"name\"")
                .append(":")
                .append("\"lxl\"")
                .append(",")
                .append("\"pwd\"")
                .append(":")
                .append("\"123\"")
                .append("}");
        LogUtil.d("jsonString",sb.toString());
        Gson gson = new Gson();
        UserInfo info=gson.fromJson(sb.toString(),UserInfo.class);
        LogUtil.d("GSOn",info.toString());
        LogUtil.d("UserInfo123", info.getName() + "==" + info.getPwd());
        Call<ResponseBody> call = loginService.listRepos(userInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }

        });


    }
}