package com.yiyekeji.coolschool.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

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
    ArrayList<ProductModel> modelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_model);
        ButterKnife.inject(this);
        modelList=getIntent().getParcelableArrayListExtra("modelList");
        initView();
    }

    private void initView() {
        modelAdapter=new ProductModelAdapter(this,modelList);
        titleBar.initView(this);
        titleBar.setTvRight("添加", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(modelAdapter);
    }

    AlertDialog dlg;
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_modify_model,null);//获取自定义布局

       final EditText edtModel=(EditText) view.findViewById(R.id.edt_model);
        final  EditText edtPrice=(EditText) view.findViewById(R.id.edt_price);
        final EditText edtBalance=(EditText) view.findViewById(R.id.edt_balance);

        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (TextUtils.isEmpty(edtModel.getText())) {
                    showShortToast("型号不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(edtPrice.getText())) {
                    showShortToast("价格不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(edtBalance.getText())) {
                    showShortToast("库存不能为空！");
                    return;
                }
                ProductModel productModel=new ProductModel();
                productModel.setPmBalance(Integer.valueOf(edtBalance.getText().toString()));
                productModel.setPmPrice(edtPrice.getText().toString());
                productModel.setPmTitle(edtModel.getText().toString());
                modelList.add(productModel);
                modelAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.setView(view);
        dlg = builder.create();
        Window window = dlg.getWindow();
        window.setWindowAnimations(R.style.dialog_anim_style);
        dlg.show();

    }


    @OnClick(R.id.cb_addProduct)
    public void onClick() {
      /*  for (ProductModel model : modelList) {
            if (TextUtils.isEmpty(model.getPmTitle())||
                            TextUtils.isEmpty(model.getPmPrice())||
                            model.getPmBalance()<=0||
                            Double.valueOf(model.getPmPrice())<=0){
                modelList.remove(model);
                break;
            }
        }*/
        Intent intent=new Intent();
        intent.putParcelableArrayListExtra("modelList", modelList);
        setResult(RESULT_OK,intent);
        finish();
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
        }
        return  true;
    }

    private void addModel() {
        modelList.add(new ProductModel());
        modelAdapter.notifyDataSetChanged();
    }
}
