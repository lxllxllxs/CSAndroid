package com.yiyekeji.coolschool.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.BitmapCompressor;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.utils.SPUtils;
import com.zhy.autolayout.utils.ScreenUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
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
    final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0x123;
    final int READ_PHONE_STATE_REQUEST_CODE = 0x122;
    @InjectView(R.id.iv_background)
    ImageView ivBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);
        initView();
    }
    /**
     * 跳转到魅族的权限管理系统
     */
    private void gotoMeizuPermission() {
        Toast.makeText(this,"请在该应用的权限管理中打开桌面悬浮窗权限",Toast.LENGTH_LONG).show();
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                if("Meizu".equals(android.os.Build.MANUFACTURER)){
                    gotoMeizuPermission();
                    // FIXME: 2017/5/3/003 这里小米可以正常
                }else {
                    Toast.makeText(this,"请允许该应用在其他应用上层显示",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
              /*  Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);*/
            }
        }

        int[] size = ScreenUtils.getScreenSize(this, false);
        int width=size[0];
        int height=size[1];
        LogUtil.d("屏幕像素为：" + width + "==" + height);
        ivBackground.setImageBitmap(
                BitmapCompressor.decodeSampledBitmapFromResource(getResources(),R.mipmap.bg_start,width,height));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            }, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            return;
        }
        doAfterGranted();
    }

    private void doAfterGranted() {
        LogUtil.d("已获得读写权限");
        setEditView();
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        user.setImei(szImei);
        login();
    }

    private void setEditView() {
        final String LOGIN_NAME = "loginName";
        final String PWD = "pwd";
        if (SPUtils.contains(this, LOGIN_NAME)) {
            user.setUserNum(SPUtils.getString(this, LOGIN_NAME));
        }
        if (SPUtils.contains(this, PWD)) {
            user.setPassword(SPUtils.getString(this, PWD));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (grantResults.length!=2){
            showShortToast("请允许权限请求！");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                }, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }
            return;
        }
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                doAfterGranted();
            } else {
                startActivity(LoginActivity.class);
            }
        }
    }

    private void login() {
        if (TextUtils.isEmpty(user.getUserNum()) || TextUtils.isEmpty(user.getPassword())) {
            startActivity(LoginActivity.class);
            return;
        }
        userService = RetrofitUtil.create(UserService.class);
        Call<ResponseBody> call = userService.login(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    startActivity(LoginActivity.class);
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                UserInfo userInfo = GsonUtil.fromJSon(jsonString, UserInfo.class, "userInfo");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (userInfo != null) {
//                    showShortToast("成功登录！");
//                    userInfo.setPassword("");//清除密码  不清了
                    App.userInfo = userInfo;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(MainViewpagerActivity.class);
                            finish();
                        }
                    }, 2 * 1000);
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
