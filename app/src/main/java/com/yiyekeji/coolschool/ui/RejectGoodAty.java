package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CancelOrder;
import com.yiyekeji.coolschool.bean.CourseAbsenceInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.adapter.AbsenceListAdapter;
import com.yiyekeji.coolschool.ui.adapter.CancelOrderAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
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
 * 缺勤记录 （缺勤列表）
 * Created by lxl on 2017/1/14.
 */
public class RejectGoodAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    CancelOrderAdapter mAdapter;
    List<CancelOrder> orderList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycleview);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void getCancleOrderList(){
        Map<String, Object> params = new HashMap<>();
        params.put("userNo", App.getUserInfo().getUserNum());
        RollCallService service = RetrofitUtil.create(RollCallService.class);
        showLoadDialog("");
        Call<ResponseBody> call=service.getCutClassCount(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                orderList= GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CancelOrder>>() {}.getType(),"cancelOrderList") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (orderList!=null) {
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
    private void initView() {
        titleBar.initView(this);

        mAdapter=new CancelOrderAdapter(this,orderList);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickLitener(new CancelOrderAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(RejectGoodAty.this, AbsenceRecordAty.class);
            }
        });
    }
}
