package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里一定要注意要用AutoLayout系列
 * Created by lxl on 2017/1/6.
 */
public class FakeTabHost extends AutoRelativeLayout {

    private Context context;
    private LinearLayout llParent;
    private List<TextView> textViews = new ArrayList<>();
    private List<ImageView> imgViews = new ArrayList<>();
    private List<LinearLayout> linearLayouts = new ArrayList<>();
    private ViewPager vp;
    private int baseColor;
    private int selectColor;
    private ExtraPageChangeListener exListener;

    private int[] resId;
    private int[] noResId;
    private List<Fragment> fragmentList;

    public FakeTabHost(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public FakeTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public void initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_fake_tab_host, this);
        LinearLayout ll1,ll2,ll3,ll4,ll5;
        TextView tv1,tv2,tv3,tv4,tv5;
        ImageView iv1,iv2,iv3,iv4,iv5;

        baseColor = ContextCompat.getColor(context, R.color.white);
        selectColor=ContextCompat.getColor(context,R.color.colorPrimary);

        llParent=(LinearLayout)findViewById(R.id.ll_parent);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        ll4 = (LinearLayout) findViewById(R.id.ll4);
        ll5 = (LinearLayout) findViewById(R.id.ll5);

        linearLayouts.add(ll1);
        linearLayouts.add(ll2);
        linearLayouts.add(ll3);
        linearLayouts.add(ll4);
        linearLayouts.add(ll5);

        tv1=(TextView)findViewById(R.id.tab1);
        tv2=(TextView)findViewById(R.id.tab2);
        tv3=(TextView)findViewById(R.id.tab3);
        tv4=(TextView)findViewById(R.id.tab4);
        tv5=(TextView)findViewById(R.id.tab5);

        textViews.add(tv1);
        textViews.add(tv2);
        textViews.add(tv3);
        textViews.add(tv4);
        textViews.add(tv5);

        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 =(ImageView) findViewById(R.id.iv2);
        iv3 =(ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5 = (ImageView) findViewById(R.id.iv5);

        imgViews.add(iv1);
        imgViews.add(iv2);
        imgViews.add(iv3);
        imgViews.add(iv4);
        imgViews.add(iv5);

    }

    public void setColor(int baseColor,int selectColor){
        this.baseColor=baseColor;
        this.selectColor = selectColor;
    }
    public void setTabHost(List<Fragment> fragmentList,String[] tabString, int[] resId,int[] noResId,ViewPager viewPager) {
        this.vp = viewPager;
        this.resId = resId;
        this.noResId = noResId;
        this.fragmentList = fragmentList;

        vp.setOffscreenPageLimit(fragmentList.size());//加载全部 避免重置加载
        vp.setAdapter(new MyFrageStatePagerAdapter(((BaseActivity)context).getSupportFragmentManager()));

        for (int i=0;i<tabString.length;i++) {
            linearLayouts.get(i).setVisibility(VISIBLE);
            linearLayouts.get(i).setOnClickListener(ck);
            textViews.get(i).setText(tabString[i]);
            if (i == 0) {
                imgViews.get(i).setImageResource(resId[0]);
            } else {
                imgViews.get(i).setImageResource(noResId[i]);
            }
        }
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    public void setBackgroundColor(int color){
        llParent.setBackgroundColor(color);
    }
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        this.onPageChangeListener=listener;
    }

    private OnClickListener ck=new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll1:
                    vp.setCurrentItem(0);
                    break;
                case R.id.ll2:
                    vp.setCurrentItem(1);
                    break;
                case R.id.ll3:
                    vp.setCurrentItem(2);
                    break;
                case R.id.ll4:
                    vp.setCurrentItem(3);
                    break;
                case R.id.ll5:
                    vp.setCurrentItem(4);
                    break;
            }
        }
    };


    private ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            revertAllStatus();
            textViews.get(position).setTextColor(selectColor);
            imgViews.get(position).setImageResource(resId[position]);
            if (exListener != null) {
                exListener.doExtra(position);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    String textColor = "#999999";
    private void revertAllStatus(){
        for (int i=0;i<noResId.length;i++) {
            imgViews.get(i).setImageResource(noResId[i]);
        }
        for (TextView view : textViews) {
            view.setTextColor(Color.parseColor(textColor));
        }
    }

    public void setExtraPageChangeListener(ExtraPageChangeListener exListener){
        this.exListener=exListener;
    }
    public interface  ExtraPageChangeListener{
        void doExtra(int position);
    }

    /**
     * ViewPager适配器。
     */
    class MyFrageStatePagerAdapter extends FragmentPagerAdapter {
        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


    public void setCurrentItem(int index){
        vp.setCurrentItem(index);
    }
}
