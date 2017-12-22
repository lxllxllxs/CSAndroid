package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.DateComParator;
import com.yiyekeji.coolschool.bean.OtherOrder;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ExpressService;
import com.yiyekeji.coolschool.ui.adapter.OtherOrderAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerGridItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.Collections;
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
 * Created by lxl on 2017/1/26.
 */
public class OtherOrderListAty extends BaseActivity {
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

    OtherOrderAdapter mAdapter;
    List<OtherOrder> orderList = new ArrayList<>();
    private void initView() {
        titleBar.initView(this);
        mAdapter=new OtherOrderAdapter(this,orderList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickLitener(new OtherOrderAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(OtherOrderListAty.this, OtherOrderDetailAty.class);
                intent.putExtra("otherOrder", orderList.get(position));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        getOtherOrderList();
    }


    private void getOtherOrderList() {
        showLoadDialog("");
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("supplierNum", App.userInfo.getUserNum());
        if (params.get("tokenId") == null) {
            return;
        }
        ExpressService service = RetrofitUtil.create(ExpressService.class);
        Call<ResponseBody> call=service.getOtherOrderList(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if(!rb.getResult().equals("1")){
                    showShortToast(rb.getMessage());
                    return;
                }
                orderList= GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<OtherOrder>>() {}.getType(),"otherOrderList") ;
                if (orderList!=null) {
                    // TODO: 2017/5/3/003 这里要根据日期进行排序
                    // 2017-12-22 排序交给后台
//                    Collections.sort(orderList, new DateComParator());
                    mAdapter.notifyDataSetChanged(orderList);
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

}
