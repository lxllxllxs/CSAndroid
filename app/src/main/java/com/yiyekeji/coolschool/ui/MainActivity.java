package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.yiyekeji.coolschool.Config;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.UserInfoFactory;
import com.yiyekeji.coolschool.inter.LoginService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.protobuf.ProtoConverterFactory;

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
                .addConverterFactory(ProtoConverterFactory.create())
                .build();

        LoginService service = retrofit.create(LoginService.class);
        UserInfoFactory.UserInfo.Builder builder = UserInfoFactory.UserInfo.newBuilder();

        builder.setUsername("lxl10")
                .setPassword("123");

        Call call = service.listRepos(builder.build());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showShortToast(response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });
    }
}
