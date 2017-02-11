package com.yiyekeji.coolschool.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.ProductOrder;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.HashMap;
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
        GlideUtil.setImageToView(productOrder.getpImage(), ivProduct);
        tvRecipient.setText(productOrder.getReceiveName());
        tvPhone.setText(productOrder.getReceivePhone());
        tvAddress.setText(productOrder.getReceiveAddr());
        tvName.setText(productOrder.getpTitle());
        tvModel.setText(productOrder.getPmTitle());
        tvPrice.setText(String.valueOf(productOrder.getPmPrice()));
        tvNum.setText(String.valueOf(productOrder.getPmCount()));
        tvDeliverTime.setText(productOrder.getTimeType());
        tvMessage.setText(productOrder.getMessage());
        setConfiromButton(productOrder.getPoState());
    }

    private void initData() {
        pOrderId=getIntent().getIntExtra("pOrderId",0);
        getSupplierPorderInfo();
    }

    int pOrderId;
    ProductOrder productOrder;
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
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (!rb.getResult().equals("1")){
                    showShortToast(rb.getMessage());
                    return;
                }
                productOrder = GsonUtil.fromJSon(jsonString,
                        ProductOrder.class,"orderInfo") ;
                if (productOrder != null) {
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
                if (productOrder.getPoState()==1){
                    return;
                }
                AlertDialog.Builder buidler = new AlertDialog.Builder(this);
                buidler.setMessage("确定完成该订单吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateProductOrderState();
                            }
                        })
                        .show();
                break;
        }
    }


    private void updateProductOrderState() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("poId",pOrderId);
        ShopService service = RetrofitUtil.create(ShopService.class);
        showLoadDialog("");
        Call<ResponseBody> call=service.updateProductOrderState(params);
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
                setConfiromButton(1);
                showShortToast(rb.getMessage());
                finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
            }
        });
    }

    private void setConfiromButton(int type) {
        switch (type){
            case 0:
                tvConfirm.setText("完成");
                tvConfirm.setBackgroundColor(ContextCompat.getColor(SellProductOrderDetailAty.this,R.color.theme_red));
                break;
            case 1:
                tvConfirm.setText("已完成");
                tvConfirm.setBackgroundColor(ContextCompat.getColor(SellProductOrderDetailAty.this,R.color.gray_text));
                break;
        }
    }
}
