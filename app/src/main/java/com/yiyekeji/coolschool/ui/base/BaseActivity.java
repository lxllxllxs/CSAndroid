package com.yiyekeji.coolschool.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.widget.LoadDialog;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by Administrator on 2016/10/23.
 */
public  class BaseActivity extends AutoLayoutActivity implements View.OnClickListener {

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
            mdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
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

    protected void showShortToast(CharSequence text) {
        if(shortToast==null){
            shortToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }else{
            shortToast.setText(text);
        }
        shortToast.show();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.removeActivity(this);
    }
}
