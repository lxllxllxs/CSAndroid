package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.CommonUtils;
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
//                login("1111111111", "qqqqqq");
                login(ledtLoginName.getEditText(), ledtPwd.getEditText());
                break;
            case R.id.tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_findPwd:
                startActivity(FindPwdActivity.class);
                break;
        }
    }

    UserService userService;
    private void login(String name,String pwd) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            showShortToast("账号或密码不能为空！");
            return ;
        }
        final UserInfo user = new UserInfo();
        user.setUserNum(name);
        user.setPassword(pwd);

        user.setImei(CommonUtils.getIMEI());
        userService = RetrofitUtil.create(UserService.class);
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
                    showShortToast("成功登录！");
                    savaLoginInfo();
                    userInfo.setPassword("");//清除密码
                    App.userInfo=userInfo;
                    Intent intent = new Intent(LoginActivity.this, MainViewpagerActivity.class);
                    startActivity(intent);

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


    /**
     * 连续点击两次 关闭
     */
    long[] mHits = new long[2];
    @Override
    public void onBackPressed() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于500，即双击
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        showShortToast("再次点击退出应用");
        if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
            App.removeAllActivity();
        }
    }
}
