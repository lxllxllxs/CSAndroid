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
    private TextView bigTitleTv,tvRight;
    private ImageView iv_back;
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
        tvRight=(TextView) findViewById(R.id.tv_right);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extraListener != null) {
                    extraListener.doExtra();
                }
                activity.finish();
            }
        });
        setTitleText(activity.getTitle());
    }

    public void  setTvRight(String text,OnClickListener clickListener){
        tvRight.setVisibility(VISIBLE);
        tvRight.setText(text);
        tvRight.setOnClickListener(clickListener);
    }

    public void  setTvRightText(String text){
        tvRight.setText(text);
    }


    public void setBackOnClickListener(OnClickListener ck){
        iv_back.setOnClickListener(ck);
    }

    public void setExtraListener(ExtraListener extraListener){
        this.extraListener = extraListener;
    }

    public interface ExtraListener{
        void doExtra();
    }

    private ExtraListener extraListener;

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

}
