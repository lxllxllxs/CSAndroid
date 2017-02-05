package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/2/4.
 */
public class FeedBackActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.edt_feedback)
    EditText edtFeedback;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        titleBar.initView(this);
    }

    @OnClick(R.id.tv_confirm)
    public void onClick() {

    }
}
