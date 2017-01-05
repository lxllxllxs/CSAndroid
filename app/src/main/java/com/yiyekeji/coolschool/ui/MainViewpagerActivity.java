package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.view.Window;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;

public class MainViewpagerActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_viewpager);
		initView();
		initViewPager();
	}
	private void initViewPager() {

	}
	private void initView() {
	}

}
