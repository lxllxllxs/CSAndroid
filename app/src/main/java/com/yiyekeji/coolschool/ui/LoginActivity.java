package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.LoginService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.utils.SPUtils;
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


    private final String LOGIN_NAME="loginName";
    private final String PWD="pwd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        if (SPUtils.contains(this,LOGIN_NAME)){
            ledtLoginName.setEditText(SPUtils.getString(this,LOGIN_NAME));
        }
        if (SPUtils.contains(this,PWD)){
            ledtPwd.setEditText(SPUtils.getString(this,PWD));
        }
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
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
        }
    }

    LoginService loginService;
    private void login() {
        String name = ledtLoginName.getEditText();
        String pwd = ledtPwd.getEditText();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            showShortToast("账号或密码不能为空！");
            return ;
        }
        savaLoginInfo();
        UserInfo user = new UserInfo();
        user.setUserNum(name);
        user.setPassword(pwd);
        loginService= RetrofitUtil.create(LoginService.class);
        Call<ResponseBody> call=loginService.login(user);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                UserInfo userInfo=null;
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                    userInfo = GsonUtil.fromJSon(response,UserInfo.class) ;
                ResponseBean rb = GsonUtil.fromJSon(response, ResponseBean.class);
                if (userInfo!=null) {
                    showShortToast("成功登录！");
//                    getUserInfo();
                } else {

                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(t.toString());
            }
        });
    }

    private void savaLoginInfo(){
        SPUtils.put(LoginActivity.this, LOGIN_NAME, ledtLoginName.getEditText());
        SPUtils.put(LoginActivity.this,PWD,ledtPwd.getEditText());
    }
    private void getUserInfo() {
        Call<ResponseBody> call=loginService.getUserInfo(App.userInfo);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                App.userInfo = GsonUtil.fromJSon(response,UserInfo.class) ;
                if (App.userInfo != null) {
                    startActivity(MainViewpagerActivity.class);
                } else {
                    ResponseBean rb = GsonUtil.fromJSon(response, ResponseBean.class);
                    showShortToast(rb.getMessage());
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
