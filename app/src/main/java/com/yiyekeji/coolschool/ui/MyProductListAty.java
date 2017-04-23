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
import com.yiyekeji.coolschool.bean.ProductInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.MyProductListAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.PullToRefreshRecycleView;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/23.
 */
public class MyProductListAty extends BaseActivity {
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

    private void initData() {
        getMyProductList();
    }

    private void initView() {
        titleBar.initView(this);
        mAdapter = new MyProductListAdapter(this, productInfoList);

        recyclerView=prrvPullRefreshView.getRefreshableView();

        prrvPullRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        prrvPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                refresh();
            }
            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickLitener(new MyProductListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MyProductListAty.this, ProductDetailAty.class);
                intent.putExtra("pId", productInfoList.get(position).getPid());
                startActivity(intent);
            }
        });

        mAdapter.setmDelClickLitener(new MyProductListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductInfo info = productInfoList.get(position);
                putUpOrOffProduct(info);
            }
        });

    }

    private void refresh() {
        productInfoList.clear();
        getMyProductList();
    }

    private void putUpOrOffProduct(ProductInfo info) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pId", info.getPid());
        params.put("pState", info.getpState() == 1 ? 0 : 1);
        params.put("tokenId", App.geTokenId());
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.putUpOrOffProduct(params);
        showLoadDialog("");
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
                if (rb.getResult().equals("1")) {
                    showShortToast("操作成功！");
                    refresh();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                showShortToast(getString(R.string.response_err));
            }
        });

    }

    MyProductListAdapter mAdapter;
    List<ProductInfo> productInfoList = new ArrayList<>();

    private void getMyProductList() {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("tokenId", App.geTokenId());
        parms.put("userNum", App.userInfo.getUserNum());
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.getSupplierProductList(parms);
        showLoadDialog("");
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
                if (rb.getResult().equals("1")) {
                    productInfoList = GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<ProductInfo>>() {
                            }.getType(), "productList");
                    if (productInfoList == null) {
                        return;
                    }
                    mAdapter.notifyDataSetChanged(productInfoList);
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                prrvPullRefreshView.onRefreshComplete();
                showShortToast(getString(R.string.response_err));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            refresh();
        }
    }
}
