package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

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
	
	private class MyPagerAdapter extends PagerAdapter {

		List<View> list = new ArrayList<View>();

		private MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		public int getCount() {
			return list.size();
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		public Parcelable saveState() {
			return null;
		}

	}

}
