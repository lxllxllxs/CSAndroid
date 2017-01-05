package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.LoginService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.CButton;
import com.yiyekeji.coolschool.widget.LableEditView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/5.
 */
public class LoginActivity extends BaseActivity {
    @InjectView(R.id.ledt_loginName)
    LableEditView ledtLoginName;
    @InjectView(R.id.ledt_pwd)
    LableEditView ledtPwd;
    @InjectView(R.id.cb_login)
    CButton cbLogin;
    @InjectView(R.id.tv_register)
    TextView tvRegister;
    @InjectView(R.id.tv_findPwd)
    TextView tvFindPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {

    }

    @OnClick({R.id.cb_login, R.id.tv_register, R.id.tv_findPwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_login:
                login();
                break;
            case R.id.tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_findPwd:
                break;
        }
    }

    private void login() {
        String name = ledtLoginName.getEditText();
        String pwd = ledtPwd.getEditText();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            showShortToast("账号或密码不能为空！");
            return ;
        }
        UserInfo user = new UserInfo();
        user.setUserNum(name);
        user.setPassword(pwd);
        LoginService loginService = RetrofitUtil.create(LoginService.class);
        Call<ResponseBody> call=loginService.login(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBean rb = GsonUtil.fromJSon(response.body().charStream(), ResponseBean.class);
                UserInfo userInfo = GsonUtil.fromJSon(response.body().charStream(),UserInfo.class) ;
                if (userInfo != null) {
                    showShortToast("成功登录！");
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showShortToast(t.toString());
            }
        });
    }
}
