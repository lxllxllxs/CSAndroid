package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lxl on 2017/2/4.
 */
public class PullMsgDetailAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_msg_detail);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        String  title=getIntent().getStringExtra("title");
        String  content=getIntent().getStringExtra("content");
        String  date=getIntent().getStringExtra("title");
        tvContent.setText(content);
    }



}
