package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.google.gson.Gson;
import com.yiyekeji.coolschool.Config;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.LogUtil;

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

        UserService userService = retrofit.create(UserService.class);
        UserInfo userInfo = new UserInfo();
        userInfo.setName("lxl");
        StringBuilder sb = new StringBuilder();
        sb .append("\"userInfo\"")
                .append(":")
                .append("{")
                .append("\"name\"")
                .append(":")
                .append("\"lxl\"")
                .append(",")
                .append("\"password\"")
                .append(":")
                .append("\"123\"")
                .append("}");
        LogUtil.d("jsonString",sb.toString());
        Gson gson = new Gson();
        UserInfo info=gson.fromJson(sb.toString(),UserInfo.class);
        LogUtil.d("GSOn",info.toString());


    }
}