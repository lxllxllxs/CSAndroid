package com.yiyekeji.coolschool.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductImage;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.SelectImageAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerGridItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/21.
 */
public class SelectProductImageAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycleview);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {
        ShopService service = RetrofitUtil.create(ShopService.class);
        Map<String, Object> params = new HashMap<>();
        params.put("userNo", App.getUserInfo().getUserNum());
        Call<ResponseBody> call = service.getProductImages(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    imageList=GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<ProductImage>>() {
                            }.getType(), "productImages");
                    if (imageList == null) {
                        return;
                    }
                    mAdapter.notifyDataSetChanged(imageList);
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
            }
        });

    }

    private void  delImage(final  ProductImage image){
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.delImage(image);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    //需要移除 被删的
                    imageList.remove(image);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
            }
        });

    }


    boolean isEdit = false;
    SelectImageAdapter mAdapter;
    List<ProductImage> imageList = new ArrayList<>();
    private void initView() {
        titleBar.initView(this);

        titleBar.setTvRight("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    isEdit = false;
                    mAdapter.setDelVisiable(false);
                    titleBar.setTvRightText("编辑");
                } else {
                    isEdit = true;
                    titleBar.setTvRightText("完成");
                    mAdapter.setDelVisiable(true);
                }

            }
        });

        mAdapter=new SelectImageAdapter(this,imageList);
        //删除
        mAdapter.setDelOnClickListener(new SelectImageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductImage image =imageList.get(position);
                showDialog(image);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mAdapter.setOnItemClickLitener(new SelectImageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductImage image= (ProductImage) imageList.get(position);
                Intent intent=new Intent();
                intent.putExtra("image",image);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }


    public void showDialog(final ProductImage image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定删除该图片吗？");//设置标题内容
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                delImage(image);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        final AlertDialog dlg = builder.create();
        dlg.show();
    }
}
