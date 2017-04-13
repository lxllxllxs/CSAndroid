package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ChoseBean;
import com.yiyekeji.coolschool.bean.CreateProductOrderInfo;
import com.yiyekeji.coolschool.bean.ProductOrder;
import com.yiyekeji.coolschool.bean.ProductOrderItem;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.CreateProductOrderAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.DateUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RegexUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class CreateReturnOrderAty extends BaseActivity {

    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.edt_descrition)
    EditText edtDescrition;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_good);
        ButterKnife.inject(this);
        initData();
        initView();
    }


    private void initView() {
        titleBar.initView(this);

    }


    private ProductOrder productOrder;

    private void initData() {
        productOrder = getIntent().getParcelableExtra("info");
    }


    @OnClick({R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                if (check()) {
                    createRejectOrder();
                }
                break;
        }
    }


    /**
     * 创建订单 前面已校验
     */

    private void createRejectOrder() {
        ShopService service = RetrofitUtil.create(ShopService.class);
        Map<String, Object> params = new HashMap<>();
        if (productOrder == null) {
            return;
        }
        if (TextUtils.isEmpty(App.geTokenId())){
            return;
        }
        params.put("orderId", productOrder.getPoId());
        params.put("createDate", DateUtils.getTimeString());
        params.put("reason", edtDescrition.getText().toString());

        Call<ResponseBody> call = service.cancelOrder(params);

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
                    showShortToast(rb.getMessage());
                    finish();
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

    private boolean check() {
        if (TextUtils.isEmpty(edtDescrition.getText())) {
            showShortToast("原因不能为空！");
            return false;
        }
        return true;
    }

}
