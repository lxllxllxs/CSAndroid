package com.yiyekeji.coolschool.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.utils.SPUtils;
import com.yiyekeji.coolschool.widget.CButton;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.zhy.autolayout.utils.ScreenUtils;

import java.util.HashMap;
import java.util.Map;

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
    final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=0x123;
    final int READ_PHONE_STATE_REQUEST_CODE=0x122;

    private final String LOGIN_NAME = "loginName";
    private final String PWD = "pwd";
    @InjectView(R.id.tv_feedBack)
    TextView tvFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initView();
        upLoadPhoneModel();
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
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                doAfterGranted();
            } else {
                LogUtil.d("noGranted");
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
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
    @OnClick({R.id.cb_login, R.id.tv_register, R.id.tv_findPwd,R.id.tv_feedBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_login:
                login(ledtLoginName.getEditText(), ledtPwd.getEditText());
                break;
            case R.id.tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_findPwd:
                startActivity(FindPwdActivity.class);
                break;
            case R.id.tv_feedBack:
                startActivity(FeedBackActivity.class);
                break;
        }
    }

    UserService userService;
    UserInfo  user = new UserInfo();
    private void login(String name, String pwd) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            showShortToast("账号和密码不能为空！");
            return;
        }
        user.setUserNum(name);
        user.setPassword(pwd);
        userService = RetrofitUtil.create(UserService.class);
        Call<ResponseBody> call = userService.login(user);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                UserInfo userInfo = GsonUtil.fromJSon(jsonString, UserInfo.class, "userInfo");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (userInfo != null) {
//                    showShortToast("成功登录！");
                    savaLoginInfo();
//                    userInfo.setPassword("");//清除密码 不清了
                    App.userInfo = userInfo;
                    Intent intent = new Intent(LoginActivity.this, MainViewpagerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                showShortToast(t.toString());
            }
        });
    }

    /**
     * 没有权限直接不保存
     */
    private void savaLoginInfo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            return;
        }
        SPUtils.put(LoginActivity.this, LOGIN_NAME, ledtLoginName.getEditText());
        SPUtils.put(LoginActivity.this, PWD, ledtPwd.getEditText());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            ledtLoginName.setEditText("");
//            ledtPwd.setEditText("");
//        }
    }


    /**
     * 直接上传型号信息了
     */
    private void upLoadPhoneModel() {
        Map<String, Object> params = new HashMap<>();
        params.put("pType", Build.MODEL);
        int[] screenSize = ScreenUtils.getScreenSize(this, true);
        String Size = screenSize[0] + "×" + screenSize[1];
        params.put("pSize", Size);
        LogUtil.d("upLoadPhoneModel", params.toString());
        CommonService commonService = RetrofitUtil.create(CommonService.class);
        Call<ResponseBody> call = commonService.appUploadPhoneModel(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

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
