package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.ui.adapter.ProductModelAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.CButton;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/20.
 */
public class ModifyProductModelAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.cb_addProduct)
    CButton cbAddProduct;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    ProductModelAdapter modelAdapter;
    ArrayList<ProductModel> modelList = new ArrayList<>();
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
        recyclerView.setAdapter(modelAdapter);
    }

    @OnClick(R.id.cb_addProduct)
    public void onClick() {
        if (check()) {
            addModel();
        }
    }

    private boolean check() {
        for (ProductModel model : modelList) {
            if (TextUtils.isEmpty(model.getPmTitle())){
                showShortToast("标题不能为空！");
                return false;
            }
            if (Float.valueOf(model.getPmPrice())<=0){
                showShortToast("价格不能小于等于0！");
                return false;
            }
            if (model.getPmBalance()<=0){
                showShortToast("库存不能小于等于0！");
                return false;
            }
            if (TextUtils.isEmpty(model.getPmTitle())){
                showShortToast("标题不能为空！");
                return false;
            }
        }
        return  true;
    }

    private void addModel() {
        modelList.add(new ProductModel());
        modelAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void finish() {
        Intent intent=new Intent();
        intent.putParcelableArrayListExtra("modelList", modelList);
        setResult(RESULT_OK,intent);
        super.finish();

    }
}
