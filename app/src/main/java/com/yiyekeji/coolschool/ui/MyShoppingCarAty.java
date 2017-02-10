package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductListBean;
import com.yiyekeji.coolschool.bean.ProductOrderItem;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.ShoppingCarProduct;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.ShoppingCarAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/23.
 */
public class MyShoppingCarAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.rv_product)
    RecyclerView rvProduct;
    @InjectView(R.id.tv_totalPrice)
    TextView tvTotalPrice;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;
    @InjectView(R.id.tv_del)
    TextView tvDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping_car);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {
        getMyCartInfo();
    }

    private boolean isEditModel;

    private void initView() {
        titleBar.initView(this);
      /*  titleBar.setTvRight("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditModel) {
                    titleBar.setTvRightText("完成");
                    isEditModel = true;
                    tvDel.setVisibility(View.VISIBLE);
                } else {
                    titleBar.setTvRightText("编辑");
                    isEditModel = false;
                    tvDel.setVisibility(View.INVISIBLE);
                }
            }
        });*/
        mAdapter = new ShoppingCarAdapter(this, productInfoList);
        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        rvProduct.setAdapter(mAdapter);
        rvProduct.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickLitener(new ShoppingCarAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //这里的方法同样被子Adapter调用
                calTotalPrice();
            }
        });

    }

    private void refresh() {
        productInfoList.clear();
        getMyCartInfo();
    }

    ShoppingCarAdapter mAdapter;
    List<ShoppingCarProduct> productInfoList = new ArrayList<>();

    private void getMyCartInfo() {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("tokenId", App.geTokenId());
        parms.put("userNum", App.userInfo.getUserNum());
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.getMyCartInfo(parms);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    productInfoList = GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<ShoppingCarProduct>>() {
                            }.getType(), "myCartInfoList");
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
                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });
    }



    @OnClick({R.id.tv_del, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_del:
                break;
            case R.id.tv_confirm:
                if (isDiffer()){
                    showShortToast("暂不支持跨店！");
                    return;
                }
                if (isEmpty()){
                    showShortToast("至少选中一件！");
                    return;
                }
                convertToProduct();
                jumpToCreateOrder();
                break;
        }
    }

    private ArrayList<ProductOrderItem> items = new ArrayList<>();
    private void convertToProduct() {
        ProductOrderItem item;
        for (ShoppingCarProduct carProduct : productInfoList) {
            for (ProductListBean bean : carProduct.getProductList()) {
                if (bean.isSelect()) {
                    item = new ProductOrderItem();
                    item.setSupplierNum(carProduct.getSupplierNum());
                    item.setPrice(String.valueOf(bean.getPmPrice()));
                    item.setmTitle(bean.getPmTitle());
                    item.setProductName(bean.getName());
                    item.setImgPath(bean.getImagePath());
                    item.setpId(bean.getPId());
                    item.setPmCount(bean.getPmCount());
                    item.setUnit(bean.getPUnit());

                    items.add(item);
                }
            }
        }
    }

    private void calTotalPrice(){
        totalPrice=0;
        for (ShoppingCarProduct carProduct : productInfoList) {
            for (ProductListBean bean : carProduct.getProductList()) {
                if (bean.isSelect()) {
                    totalPrice = totalPrice + bean.getPmCount() * Double.valueOf(bean.getPmPrice());
                }
            }
        }
        tvTotalPrice.setText(getString(R.string.yuan).concat(String.valueOf(totalPrice)));
    }

    private double totalPrice;
    private void jumpToCreateOrder() {
        Intent intent = new Intent(MyShoppingCarAty.this, CreateProductOrderAty.class);
        intent.putExtra("itemList", items);
        intent.putExtra("totalPrice",totalPrice );
        startActivity(intent);
    }

    private boolean isEmpty() {
        int count=0;
        for (ShoppingCarProduct carProduct : productInfoList) {
            if (carProduct.isSelect()) {
                return false;
            }
            for (ProductListBean bean : carProduct.getProductList()) {
                if (bean.isSelect()) {
                    count++;
                }
            }
        }
        if (count==0){
            return true;
        }
        return false;
    }

    private boolean isDiffer() {
        int identify=0;
        for (ShoppingCarProduct carProduct : productInfoList) {
            for (ProductListBean bean : carProduct.getProductList()) {
                if (bean.isSelect()) {
                    identify++;
                    break;
                }
            }
        }
        if (identify > 1) {
            return true;
        }
        return false;
    }
}
