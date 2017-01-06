package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxl on 2017/1/6.
 */
public class FakeTabHost extends RelativeLayout {

    private Context context;
    private LinearLayout llParent;
    private List<TextView> textViews = new ArrayList<>();
    private List<ImageView> imgViews = new ArrayList<>();
    private List<LinearLayout> linearLayouts = new ArrayList<>();

    public FakeTabHost(Context context) {
        super(context);
    }

    public FakeTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_fake_tab_host, this);
        LinearLayout ll1,ll2,ll3,ll4,ll5;
        TextView tv1,tv2,tv3,tv4,tv5;
        ImageView iv1,iv2,iv3,iv4,iv5;

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

        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);

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

    private int[] resId;
    private int[] noResId;
    public void setTabHost(String[] tabString, int[] resId,int[] noResId,ViewPager viewPager) {
        this.vp = viewPager;
        this.resId = resId;
        this.noResId = noResId;

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
            imgViews.get(position).setBackgroundColor(resId[position]);
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
        for (int i=0;i<noResId.length;i++) {
            imgViews.get(i).setImageResource(noResId[i]);
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
