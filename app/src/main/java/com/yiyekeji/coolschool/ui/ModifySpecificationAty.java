package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.ui.adapter.ProductModelAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.CButton;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/20.
 */
public class ModifySpecificationAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.cb_addProduct)
    CButton cbAddProduct;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    ProductModelAdapter modelAdapter;
    List<ProductModel> modelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_model);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        modelAdapter=new ProductModelAdapter(this,modelList);
        titleBar.initView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter();
    }

    @OnClick(R.id.cb_addProduct)
    public void onClick() {

    }
}
