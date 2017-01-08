package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.ui.fragment.AccountFragment;
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
        setContentView(R.layout.activity_main_viewpager);
        ButterKnife.inject(this);
        initView();
        initViewPager();
    }

    private void initViewPager() {
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new AccountFragment());
        tabHost.setTabHost(fragmentList,titles,resId,resIdNo,viewpager);
    }

    private void initView() {
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
