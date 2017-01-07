package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.ui.fragment.HomeFragment;
import com.yiyekeji.coolschool.widget.FakeTabHost;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainViewpagerActivity extends BaseActivity {

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.tabHost)
    FakeTabHost tabHost;
    private List<Fragment> fragmentList = new ArrayList<>();


    private String[] titles={"首页","商场","我的"};
    private int[] resId={R.mipmap.ic_home,R.mipmap.ic_market,R.mipmap.ic_my};
    private int[] resIdNo={R.mipmap.ic_home_no,R.mipmap.ic_market_no,R.mipmap.ic_my_no};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_viewpager);
        ButterKnife.inject(this);
        initView();
        initViewPager();
    }

    private void initViewPager() {
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
        tabHost.setTabHost(fragmentList,titles,resId,resIdNo,viewpager);
    }

    private void initView() {
    }

}
