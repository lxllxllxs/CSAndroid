package com.yiyekeji.coolschool.ui.base;

import android.content.Intent;
import android.os.Bundle;
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
    /**
     * 获取dialog对象
     *
     * @return
     */
    protected LoadDialog getLoadDialog() {
        return BaseActivity.mdDialog;
    }
    /**
     * 创建loadDialog
     *mdialog静态持有引用被finsh掉的activity 导致android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.Bind。。
     * @param msg
     */
    public  void showLoadDialog(String msg) {
        if (mdDialog == null) {
            mdDialog = new LoadDialog(App.getContext(), R.layout.layout_load_dialog,
                    R.style.DialogLogin);
            mdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        if (BaseActivity.mdDialog.isShowing()) {
            return;
        }
        initLoadDialog(msg);
        // 默认不能按屏幕取消dialog
        BaseActivity.mdDialog.setCanceledOnTouchOutside(false);
        BaseActivity.mdDialog.show();
    }


    /**
     * 载入loadDialog控件
     *
     * @param msg
     * @param
     */
    protected  void initLoadDialog(String msg) {

        View view = BaseActivity.mdDialog.getEntryView();
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
