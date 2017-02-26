package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.utils.SPUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/8.
 */
public class StartActivity extends BaseActivity {
    UserInfo user = new UserInfo();
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
        LogUtil.d("StartActivity:"+ CommonUtils.getCertificateSHA1Fingerprint(this));
        login();
    }

    private void initView() {
        final String LOGIN_NAME="loginName";
        final String PWD="pwd";
        if (SPUtils.contains(this,LOGIN_NAME)){
            user.setUserNum(SPUtils.getString(this,LOGIN_NAME));
        }
        if (SPUtils.contains(this,PWD)){
            user.setPassword(SPUtils.getString(this,PWD));
        }
        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        user.setImei(szImei);
    }
    private void login() {
        if (TextUtils.isEmpty(user.getUserNum())||  TextUtils.isEmpty(user.getPassword())) {
            startActivity(LoginActivity.class);
            return;
        }
        userService = RetrofitUtil.create(UserService.class);
        Call<ResponseBody> call= userService.login(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    startActivity(LoginActivity.class);
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                UserInfo  userInfo= GsonUtil.fromJSon(jsonString,UserInfo.class,"userInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (userInfo!=null) {
//                    showShortToast("成功登录！");
//                    userInfo.setPassword("");//清除密码  不清了
                    App.userInfo=userInfo;
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            startActivity(MainViewpagerActivity.class);
                            finish();
                        }
                    }, 2*1000);
                } else {
                    showShortToast(rb.getMessage());
                    startActivity(LoginActivity.class);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showShortToast(t.toString());
                startActivity(LoginActivity.class);
            }
        });
    }
}
