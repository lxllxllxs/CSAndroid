package com.yiyekeji.coolschool.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.zhy.autolayout.AutoRelativeLayout;


/**
 * 左返回 中间标题
 * Created by lxl on 16/08/16.
 */
public class TitleBar extends AutoRelativeLayout {

    private Context context;
    private RelativeLayout containerLayout;
    private TextView bigTitleTv;
    private ImageView iv_back,iv_right;
    private Activity activity;

    public TitleBar(Context context) {
        super(context);

    }
    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 传activity设置标题
     * @param activity
     */
    public void initView(final Activity activity) {
        context=activity;
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this);
        containerLayout = (RelativeLayout) findViewById(R.id.rl_title_bar_container);
        bigTitleTv = (TextView) findViewById(R.id.tv_title_bar_big_title);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_right=(ImageView)findViewById(R.id.iv_right_menu);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        setTitleText(activity.getTitle());
    }

    /**
     * 传context 手动设置标题
     * @param context
     */
    public void initView(final Context context,String title) {
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this);
        containerLayout = (RelativeLayout) findViewById(R.id.rl_title_bar_container);
        bigTitleTv = (TextView) findViewById(R.id.tv_title_bar_big_title);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).finish();
            }
        });
        bigTitleTv.setText(title);
    }

    public void setLeftBackGone(){
        iv_back.setVisibility(View.GONE);
    }

    public void setBarBackgroundColor(int color) {
        containerLayout.setBackgroundColor(color);
    }

    public void setTitleText(CharSequence title) {
        bigTitleTv.setText(title);
    }

    public void setLeftImageview(int resid){
        iv_back.setImageResource(resid);
    }

    public void setRightImageView(int resid){
        iv_right.setImageResource(resid);
    }

    public void setRightMenuListener(OnClickListener ck){
        iv_right.setOnClickListener(ck);
    }
}
