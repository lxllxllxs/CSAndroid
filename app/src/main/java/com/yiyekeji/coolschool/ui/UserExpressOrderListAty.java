package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.DateComParator;
import com.yiyekeji.coolschool.bean.OtherOrder;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ExpressService;
import com.yiyekeji.coolschool.ui.adapter.UserExpressOrderAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerGridItemDecoration;
import com.yiyekeji.coolschool.widget.PullToRefreshRecycleView;
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
public class UserExpressOrderListAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;

    RecyclerView recyclerView;
    @InjectView(R.id.prrv_pull_refresh_view)
    PullToRefreshRecycleView prrvPullRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_pulltorefresh_recycleview);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    UserExpressOrderAdapter mAdapter;
    List<OtherOrder> orderList = new ArrayList<>();

    private void initView() {
        titleBar.initView(this);
        mAdapter = new UserExpressOrderAdapter(this, orderList);

        recyclerView = prrvPullRefreshView.getRefreshableView();
        prrvPullRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        prrvPullRefreshView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<RecyclerView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<RecyclerView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
            }
        });
        prrvPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                refreshData();
            }
            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickLitener(new UserExpressOrderAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(UserExpressOrderListAty.this, OtherOrderDetailAty.class);
                intent.putExtra("otherOrder", orderList.get(position));
                startActivity(intent);
            }
        });
    }

    //// TODO: 2017/4/17/017  这里要改成 可以处理 分页加载的方法
    private void refreshData(){
        getUserOtherOrderList();
    }

    private void initData() {
        getUserOtherOrderList();
    }


    private void getUserOtherOrderList() {
        showLoadDialog("");
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("userNo", App.userInfo.getUserNum());
        if (params.get("tokenId") == null) {
            return;
        }
        ExpressService service = RetrofitUtil.create(ExpressService.class);
        Call<ResponseBody> call = service.getUserOtherOrderList(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                prrvPullRefreshView.onRefreshComplete();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (!rb.getResult().equals("1")) {
                    showShortToast("暂无订单");
                    return;
                }
                List<OtherOrder>  temp = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<OtherOrder>>() {
                        }.getType(), "otherOrderList");
                if (temp != null) {
                    // TODO: 2017/5/3/003 这里要根据日期进行排序
                    orderList=temp;
                    Collections.sort(orderList, new DateComParator());
                    mAdapter.notifyDataSetChanged(orderList);
                } else {
                    showShortToast("暂无订单");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                prrvPullRefreshView.onRefreshComplete();
            }
        });
    }

}
