package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductOrder;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.BuyerProductOrderAdapter;
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
 * Created by lxl on 2017/1/26.
 */
public class BuyerProductOrderListAty extends BaseActivity {
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

    BuyerProductOrderAdapter mAdapter;
    List<ProductOrder> orderList = new ArrayList<>();
    private void initView() {
        titleBar.initView(this);
        mAdapter=new BuyerProductOrderAdapter(this,orderList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickLitener(new BuyerProductOrderAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //买家不可点击 没有详情界面
               /* Intent intent = new Intent(BuyerProductOrderListAty.this, SellProductOrderDetailAty.class);
                intent.putExtra("pOrderId",orderList.get(position).getPoId());
                startActivity(intent);*/
            }
        });
    }

    private void initData() {
        getMyProductOrderList();
    }


    private void getMyProductOrderList() {
        showLoadDialog("");
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        if (params.get("tokenId") == null) {
            return;
        }
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call=service.getMyProductOrderList(params);
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
                        new TypeToken<List<ProductOrder>>() {}.getType(),"orderList") ;
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

}
