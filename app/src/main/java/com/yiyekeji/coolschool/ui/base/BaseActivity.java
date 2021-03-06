package com.yiyekeji.coolschool.ui.base;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.LoginActivity;
import com.yiyekeji.coolschool.widget.LoadDialog;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by Administrator on 2016/10/23.
 */
public  class BaseActivity extends AutoLayoutActivity implements View.OnClickListener {
    private final static String RELOGIN_MESSAGE = "账号已在其它设备登录,请重新登录";
    public static LoadDialog mdDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.addActivity(this);
    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setEnterTransition(new Slide().setDuration(2000));
            getWindow().setExitTransition(new Slide().setDuration(2000));
        }*/

  /*      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
//        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    final int CANCLE_DIALOG=0x123;
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CANCLE_DIALOG:
                    disMis();
                    break;
            }
        }
    };
    final String XIAOMI="Xiaomi";
    private void disMis(){
        if (mdDialog != null) {
            mdDialog.dismiss();
        }
    }
    /**
     * 获取dialog对象
     * @return
     */
    protected void dismissDialog() {
        if (Build.MANUFACTURER.equals(XIAOMI)) {
            handler.sendEmptyMessage(CANCLE_DIALOG);
        } else {
            disMis();
        }
    }
    /**
     * 创建loadDialog
     *mdialog静态持有引用被finsh掉的activity 导致android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.Bind。。
     * @param msg
     */
    public  void showLoadDialog(String msg) {
        // FIXME: 2017/5/3/003 魅蓝不能正确跳转 移动到第一页做 检查 只检查一次
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this,"请允许该应用在其他应用上层显示",Toast.LENGTH_LONG).show();
              *//*  Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);*//*
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
            }
        }*/
        if (mdDialog == null) {
            mdDialog = new LoadDialog(App.getContext(), R.layout.layout_load_dialog,
                    R.style.DialogLogin);
            // FIXME: 2017/5/3/003 这里不能用System.Alert 魅蓝不显示
            mdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        if (mdDialog.isShowing()) {
            return;
        }
        initLoadDialog(msg);
        // 默认不能按屏幕取消dialog
        mdDialog.setCanceledOnTouchOutside(false);
        mdDialog.show();
    }


    /**
     * 载入loadDialog控件
     *
     * @param msg
     * @param
     */
    protected  void initLoadDialog(String msg) {

        View view = mdDialog.getEntryView();
        ((TextView) view.findViewById(R.id.CtvInitTip)).setText(msg);
        if (TextUtils.isEmpty(msg)) {
            ((TextView) view.findViewById(R.id.CtvInitTip))
                    .setVisibility(View.GONE);
        }
    }
    private Toast shortToast, longToast ;

    protected void showShortToast(String text) {
        if(shortToast==null){
            shortToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }
        if (TextUtils.isEmpty(text)) {
            return;
        }
        shortToast.setText(text);
        shortToast.show();
        /**
         * 先这样处理
         */
        if (text.equals(RELOGIN_MESSAGE)) {
            startActivity(LoginActivity.class);
        }
    }
    protected void showLongToast(CharSequence text) {
        if(longToast==null){
            longToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        }else{
            longToast.setText(text);
        }
        longToast.show();
    }


    public void startActivity(Class<?extends BaseActivity> activity){
        Intent intent =new Intent(this,activity);
        startActivity(intent);
    }

    public void startActivityWithAnima(Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }
    public void startActivityWithAnima(Class<?extends BaseActivity> activity){
        Intent intent =new Intent(this,activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onClick(View v) {
    }


    // 点击HOME键时程序进入后台运行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        // 按下HOME键
        if(keyCode == KeyEvent.KEYCODE_HOME){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mdDialog != null&&mdDialog.isShowing()) {
            mdDialog.dismiss();
        }
        App.removeActivity(this);
    }
}
