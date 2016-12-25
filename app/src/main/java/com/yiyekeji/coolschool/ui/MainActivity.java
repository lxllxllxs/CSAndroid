package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseFactory;
import com.yiyekeji.coolschool.bean.UserInfoFactory;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.SoapUtils;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    private void  test(){
    /*    Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(ProtoConverterFactory.create())
                .build();*/

        for (int i=0;i<15;i++) {
            UserInfoFactory.UserInfo.Builder builder = UserInfoFactory.UserInfo.newBuilder();

            builder.setUsername("lxl"+i)
                    .setPassword("123");
            final UserInfoFactory.UserInfo userInfo=builder.build();
            SoapUtils.call(userInfo, new SoapUtils.CallBack() {
                @Override
                public void onSuccess(InputStream inputStream) throws IOException {
                    ResponseFactory.Response response = ResponseFactory.Response.parseFrom(inputStream);
                    if (response.getStatus().equals("1")) {
                        LogUtil.d("login", userInfo.getUsername() + "登录成功！");
                    } else {
                        LogUtil.d("login", userInfo.getUsername() + "登录失败！");
                    }
                }

                @Override
                public void onFailure(int errorCode) {
                    showShortToast("网络错误:"+errorCode);

                }
            });
        }
     /*   Call call = service.listRepos(builder.build());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showShortToast(response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });*/
    }
}
