package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.adapter.HaveNameAdapter;
import com.yiyekeji.coolschool.inter.HaveName;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.RippleView;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lxl on 2017/1/11.
 */
public class DynamicSignInActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;

    List<HaveName> haveNameList = new ArrayList<>();
    HaveNameAdapter mAdapter;
    @InjectView(R.id.rippleView)
    RippleView rippleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_sign_in);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initView() {
        titleBar.initView(this);
        rippleView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rippleView.stop();
    }

    private void initData() {

    }
}
