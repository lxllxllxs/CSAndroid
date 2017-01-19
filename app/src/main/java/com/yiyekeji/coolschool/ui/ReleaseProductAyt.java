package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/19.
 */
public class ReleaseProductAyt extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.iv_add)
    ImageView ivAdd;
    @InjectView(R.id.iv_2)
    ImageView iv2;
    @InjectView(R.id.iv_3)
    ImageView iv3;
    @InjectView(R.id.iv_4)
    ImageView iv4;
    @InjectView(R.id.iv_5)
    ImageView iv5;
    @InjectView(R.id.ll_parent)
    LinearLayout llParent;
    @InjectView(R.id.ledt_title)
    LableEditView ledtTitle;
    @InjectView(R.id.ll_category)
    LinearLayout llCategory;
    @InjectView(R.id.ll_price)
    LinearLayout llPrice;
    @InjectView(R.id.ledt_util)
    LableEditView ledtUtil;
    @InjectView(R.id.tv_cancel)
    TextView tvCancel;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_product);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.iv_add, R.id.ll_category, R.id.ll_price, R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                break;
            case R.id.ll_category:
                break;
            case R.id.ll_price:
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
                break;
        }
    }
}
