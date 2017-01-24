package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductOrderItem;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/24.
 */
public class CreateOrderAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.iv_product)
    ImageView ivProduct;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_model)
    TextView tvModel;
    @InjectView(R.id.tv_price)
    TextView tvPrice;
    @InjectView(R.id.tv_num)
    TextView tvNum;
    @InjectView(R.id.tv_totalPrice)
    TextView tvTotalPrice;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    List<ProductOrderItem> itemList;

    private void initView() {
        titleBar.initView(this);
        if (itemList == null || itemList.isEmpty()) {
            return;
        }
        ProductOrderItem item = itemList.get(0);
        GlideUtil.setImageToView(item.getImgPath(), ivProduct);
        tvModel.setText(item.getmTitle());
        tvName.setText(item.getProductName());
        tvNum.setText(getString(R.string.multiply)
                .concat(String.valueOf(item.getPmCount())
                .concat(item.getUnit())));
        tvPrice.setText(getString(R.string.yuan)
                .concat(item.getPrice()));

        StringBuilder sb = new StringBuilder();
        sb.append("总计：").append(getString(R.string.yuan)).append(item.getSubTotal());
        tvTotalPrice.setText(sb.toString());
    }

    private void initData() {
        itemList = getIntent().getParcelableArrayListExtra("itemList");
    }

    private void setDrawerFragment() {
        SearChProductFragment spf = new SearChProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("productSubTypeList", productSubTypeList);
        spf.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_drawerlayout, spf).commit();
    }

    @OnClick({R.id.iv_product, R.id.tv_name,R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_product:
                break;
            case R.id.tv_name:
                break;
            case R.id.tv_confirm:
                break;
        }
    }
}
