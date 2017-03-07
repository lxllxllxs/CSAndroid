package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CategoryInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.HaveName;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.HaveNameAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/21.
 */
public class SelectCategoryAty extends BaseActivity {
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
        Call<ResponseBody> call = service.getShopCategoryList();
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
                    catagoryList=GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<CategoryInfo>>() {
                            }.getType(), "categoryInfo");
                    if (catagoryList == null) {
                        return;
                    }
                    mAdapter.notifyDataSetChanged(catagoryList);
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

    HaveNameAdapter mAdapter;
    List<HaveName> catagoryList = new ArrayList<>();
    private void initView() {
        titleBar.initView(this);
        mAdapter=new HaveNameAdapter(this,catagoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickLitener(new HaveNameAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                CategoryInfo info= (CategoryInfo) catagoryList.get(position);
                Intent intent=new Intent();
                intent.putExtra("category",info);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
