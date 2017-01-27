package com.yiyekeji.coolschool.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GlideCacheUtil;
import com.yiyekeji.coolschool.widget.CButton;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/8.
 */
public class SettingActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.ll_modifyPwd)
    LinearLayout llModifyPwd;
    @InjectView(R.id.tv_CacheSize)
    TextView tvCacheSize;
    @InjectView(R.id.ll_clearCache)
    LinearLayout llClearCache;
    @InjectView(R.id.ll_aboutUs)
    LinearLayout llAboutUs;
    @InjectView(R.id.cb_signOut)
    CButton cbSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        titleBar.initView(this);
        tvCacheSize.setText(GlideCacheUtil.getInstance().getCacheSize(this));

    }

    @OnClick({R.id.ll_modifyPwd, R.id.ll_clearCache, R.id.ll_aboutUs,R.id.cb_signOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_modifyPwd:
                startActivity(ModifyPwdActivity.class);
                break;
            case R.id.ll_clearCache: {
                AlertDialog.Builder buidler = new AlertDialog.Builder(this);
                buidler.setMessage("确定清除所有缓存吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showLoadDialog("");
                                GlideCacheUtil.getInstance().clearImageAllCache(SettingActivity.this);
                                GlideCacheUtil.getInstance().clearImageDiskCache(SettingActivity.this);
                                getLoadDialog().dismiss();
                                refresh();
                            }
                        })
                        .show();
            }
                break;
            case R.id.ll_aboutUs:
                break;
            case R.id.cb_signOut: {
                AlertDialog.Builder buidler = new AlertDialog.Builder(this);
                buidler.setMessage("确定退出登录吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.userInfo = null;
                                startActivity(LoginActivity.class);
                            }
                        })
                        .show();
            }
                break;

        }
    }

    private void refresh() {
        tvCacheSize.setText(GlideCacheUtil.getInstance().getCacheSize(this));
    }
}
