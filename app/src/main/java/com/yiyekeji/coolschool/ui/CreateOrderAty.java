package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductOrderInfo;
import com.yiyekeji.coolschool.bean.ProductOrderItem;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RegexUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/24.
 */
public class CreateOrderAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
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
    @InjectView(R.id.tv_totalPrice)
    TextView tvTotalPrice;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;
    @InjectView(R.id.edt_recipient)
    EditText edtRecipient;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_address)
    EditText edtAddress;
    @InjectView(R.id.tv_deliverTime)
    TextView tvDeliverTime;
    @InjectView(R.id.ll_sex)
    LinearLayout llSex;
    @InjectView(R.id.edt_message)
    EditText edtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    ArrayList<ProductOrderItem> itemList;

    private void initView() {
        titleBar.initView(this);

        edtAddress.setText(App.userInfo.getAddr()== null ? "" : App.userInfo.getAddr());
        edtRecipient.setText(App.userInfo.getName());
        edtPhone.setText(App.userInfo.getPhone() == null ? "" : App.userInfo.getPhone());
        if (itemList == null || itemList.isEmpty()) {
            return;
        }
        ProductOrderItem item = itemList.get(0);
        GlideUtil.setImageToView(item.getImgPath(), ivProduct);
        tvModel.setText(item.getmTitle());
        tvName.setText(item.getProductName());
        tvNum.setText(getString(R.string.multiply)
                .concat(String.valueOf(item.getPmCount())
                        .concat(item.getUnit())));
        tvPrice.setText(getString(R.string.yuan)
                .concat(item.getPrice()));

        StringBuilder sb = new StringBuilder();
        sb.append("总计：").append(getString(R.string.yuan)).append(item.getSubTotal());
        tvTotalPrice.setText(sb.toString());
    }

    private void initData() {
        itemList = getIntent().getParcelableArrayListExtra("itemList");
    }

    private void setDrawerFragment() {
 /*       SearChProductFragment spf = new SearChProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("productSubTypeList", productSubTypeList);
        spf.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_drawerlayout, spf).commit();*/
    }

    @OnClick({R.id.iv_product, R.id.tv_name, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_product:
                break;
            case R.id.tv_name:
                break;
            case R.id.tv_confirm:
                if (check()){
                    createOrder();
                }
                break;
        }
    }

    private void createOrder() {
        ShopService service = RetrofitUtil.create(ShopService.class);
        ProductOrderInfo info=new ProductOrderInfo();
        info.setTokenId(App.geTokenId());
        info.setUserNum(App.userInfo.getUserNum());
        info.setProductOrderItems(itemList);
        info.setReceiveAddr(edtAddress.getText().toString());
        info.setReceiveName(edtRecipient.getText().toString());
        info.setSum(itemList.get(0).getSubTotal());
        info.setReceivePhone(edtPhone.getText().toString());
        info.setTimeType(0);
        Call<ResponseBody> call = service.createProductOrder(info);
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
                    showShortToast(rb.getMessage());
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

    private boolean check() {
        if (TextUtils.isEmpty(edtRecipient.getText())){
            showShortToast("收货人不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(edtPhone.getText())){
            showShortToast("手机号不能为空！");
            return false;
        }
        if (RegexUtils.checkMobile(edtPhone.getText().toString())){
            showShortToast("手机号不正确！");
            return false;
        }
        if (TextUtils.isEmpty(edtAddress.getText())){
            showShortToast("收货地址不能为空！");
            return false;
        }
        return true;
    }
}
