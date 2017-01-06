package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 最多支持6个tab
 * Created by lxl on 2016/12/27.
 */
public class FakeTabLayout extends AutoRelativeLayout {

    private Context context;
    private LinearLayout llParent;
    private List<TextView> textViews = new ArrayList<>();
    private List<View> Views = new ArrayList<>();
    private List<LinearLayout> linearLayouts = new ArrayList<>();
    public FakeTabLayout(Context context) {
        super(context);
        this.context = context;
        initView();

    }
    public FakeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }
    public void initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_fake_tablayout, this);
        LinearLayout ll1,ll2,ll3,ll4,ll5,ll6;
        TextView tv1,tv2,tv3,tv4,tv5,tv6;
        View v1,v2,v3,v4,v5,v6;

        llParent=(LinearLayout)findViewById(R.id.ll_parent);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        ll4 = (LinearLayout) findViewById(R.id.ll4);
        ll5 = (LinearLayout) findViewById(R.id.ll5);
        ll6 = (LinearLayout) findViewById(R.id.ll6);

        linearLayouts.add(ll1);
        linearLayouts.add(ll2);
        linearLayouts.add(ll3);
        linearLayouts.add(ll4);
        linearLayouts.add(ll5);
        linearLayouts.add(ll6);

        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);
        tv6=(TextView)findViewById(R.id.tv6);

        textViews.add(tv1);
        textViews.add(tv2);
        textViews.add(tv3);
        textViews.add(tv4);
        textViews.add(tv5);
        textViews.add(tv6);

        v1 =  findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 =  findViewById(R.id.v4);
        v5 =  findViewById(R.id.v5);
        v6 =  findViewById(R.id.v6);

        Views.add(v1);
        Views.add(v2);
        Views.add(v3);
        Views.add(v4);
        Views.add(v5);
        Views.add(v6);

        this.baseColor = ContextCompat.getColor(context, R.color.white);
        this.selectColor = ContextCompat.getColor(context, R.color.theme_blue);
    }
    private ViewPager vp;
    private int baseColor=Color.GRAY;
    private int selectColor=Color.BLUE;
    private ExtraPageChangeListener exListener;
    public void setColor(int baseColor,int selectColor){
        this.baseColor=baseColor;
        this.selectColor = selectColor;
    }


    public void setTabLayout(String[] tabString, ViewPager viewPager) {
        this.vp = viewPager;
        for (int i=0;i<tabString.length;i++) {
            linearLayouts.get(i).setVisibility(VISIBLE);
            textViews.get(i).setText(tabString[i]);
            textViews.get(i).setOnClickListener(ck);
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
                case R.id.tv1:
                    vp.setCurrentItem(0);
                    break;
                case R.id.tv2:
                    vp.setCurrentItem(1);
                    break;
                case R.id.tv3:
                    vp.setCurrentItem(2);
                    break;
                case R.id.tv4:
                    vp.setCurrentItem(3);
                    break;
                case R.id.tv5:
                    vp.setCurrentItem(4);
                    break;
                case R.id.tv6:
                    vp.setCurrentItem(5);
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
            Views.get(position).setBackgroundColor(selectColor);
            if (exListener != null) {
                exListener.doExtra(position);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    String textColor = "#333333";
    private void revertAllStatus(){
        for (View view : Views) {
            view.setBackgroundColor(baseColor);
        }
        //字体颜色应该黑色 text_black
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

    public void setCurrentItem(int index){
        vp.setCurrentItem(index);
    }
}