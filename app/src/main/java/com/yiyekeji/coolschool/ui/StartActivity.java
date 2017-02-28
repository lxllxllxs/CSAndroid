package com.yiyekeji.coolschool.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
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
    final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=0x123;
    final int READ_PHONE_STATE_REQUEST_CODE=0x122;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
    }

    private void initView() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            },
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            return;
        }
        doAfterGranted();
    }

    private void doAfterGranted(){
        LogUtil.d("已获得读写权限");
        setEditView();
        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        user.setImei(szImei);
        login();
    }

    private void setEditView() {
        final String LOGIN_NAME="loginName";
        final String PWD="pwd";
        if (SPUtils.contains(this,LOGIN_NAME)){
            user.setUserNum(SPUtils.getString(this,LOGIN_NAME));
        }
        if (SPUtils.contains(this,PWD)){
            user.setPassword(SPUtils.getString(this,PWD));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                doAfterGranted();
            } else {
                startActivity(LoginActivity.class);
            }
        }
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
                finish();
            }
        });
    }
}
