package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.SellerProductOrder;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/26.
 */
public class SellProductOrderDetailAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.tv_recipient)
    TextView tvRecipient;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.iv_product)
    ImageView ivProduct;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_model)
    TextView tvModel;
    @InjectView(R.id.tv_price)
    TextView tvPrice;
    @InjectView(R.id.tv_num)
    TextView tvNum;
    @InjectView(R.id.tv_deliverTime)
    TextView tvDeliverTime;
    @InjectView(R.id.tv_message)
    TextView tvMessage;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;
    @InjectView(R.id.ll_deliverTime)
    LinearLayout llDeliverTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product_order_detail);
        ButterKnife.inject(this);
        titleBar.initView(this);
        initData();
    }

    private void initView() {
        GlideUtil.setImageToView(sellerProductOrder.getpImage(), ivProduct);
        tvRecipient.setText(sellerProductOrder.getReceiveName());
        tvPhone.setText(sellerProductOrder.getReceivePhone());
        tvAddress.setText(sellerProductOrder.getReceiveAddr());
        tvName.setText(sellerProductOrder.getpTitle());
        tvModel.setText(sellerProductOrder.getPmTitle());
        tvPrice.setText(String.valueOf(sellerProductOrder.getPmPrice()));
        tvNum.setText(String.valueOf(sellerProductOrder.getPmCount()));
        tvDeliverTime.setText(sellerProductOrder.getTimeType());
        tvMessage.setText(sellerProductOrder.getMessage());
    }

    private void initData() {
        pOrderId=getIntent().getIntExtra("pOrderId",0);
        getSupplierPorderInfo();
    }

    int pOrderId;
    SellerProductOrder sellerProductOrder;
    private void getSupplierPorderInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("poId",pOrderId);
        ShopService service = RetrofitUtil.create(ShopService.class);
        showLoadDialog("");
        Call<ResponseBody> call=service.getSupplierPorderInfo(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (!rb.getResult().equals("1")){
                    showShortToast(rb.getMessage());
                    return;
                }
                List<SellerProductOrder> list;
                list= GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<SellerProductOrder>>() {}.getType(),"orderInfo") ;
                if (list != null) {
                    sellerProductOrder=list.get(0);
                    initView();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
            }
        });
    }

    @OnClick({ R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                break;
        }
    }
}
