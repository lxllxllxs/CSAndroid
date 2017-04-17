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
import com.yiyekeji.coolschool.bean.CancelOrder;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.CancelOrderAdapter;
import com.yiyekeji.coolschool.ui.adapter.TuCaoAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.PullToRefreshRecycleView;
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
 *
 */
public class CancelOrderListAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;

    RecyclerView recyclerView;
    CancelOrderAdapter mAdapter;
    List<CancelOrder> orderList = new ArrayList<>();
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

    private void initData() {
        getCancleOrderList();
    }

    // TODO: 2017/4/17/017  这里以后要改成分页加载 上拉加载
    private void getCancleOrderList() {
        Map<String, Object> params = new HashMap<>();
        params.put("userNo", App.getUserInfo().getUserNum());
        ShopService service = RetrofitUtil.create(ShopService.class);
        showLoadDialog("");
        Call<ResponseBody> call = service.getCancelOrderList(params);
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
                orderList = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CancelOrder>>() {
                        }.getType(), "cancelOrderList");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (orderList != null) {
                    mAdapter.notifyDataSetChanged(orderList);
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                prrvPullRefreshView.onRefreshComplete();
            }
        });
    }

    private void initView() {
        titleBar.initView(this);

        mAdapter = new CancelOrderAdapter(this, orderList);

        //使其支持上下拉事件
        prrvPullRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        recyclerView = prrvPullRefreshView.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prrvPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getCancleOrderList();
            }
            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickLitener(new CancelOrderAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CancelOrderListAty.this, CancleOrderDetailAty.class);
                intent.putExtra("cancelOrder", orderList.get(position));
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getCancleOrderList();
        }
    }
}
