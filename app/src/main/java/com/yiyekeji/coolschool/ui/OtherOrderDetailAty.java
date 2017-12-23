package com.yiyekeji.coolschool.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.OtherOrder;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ExpressService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
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
public class OtherOrderDetailAty extends BaseActivity {


    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.edt_recipient)
    EditText edtRecipient;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_address)
    EditText edtAddress;
    @InjectView(R.id.tv_deliverTime)
    TextView tvDeliverTime;
    @InjectView(R.id.ll_deliverTime)
    LinearLayout llDeliverTime;
    @InjectView(R.id.edt_message)
    EditText edtMessage;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;
    @InjectView(R.id.tv_self)
    TextView tvSelf;

    @InjectView(R.id.ll_confirm)
    LinearLayout llConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_order_detail);
        ButterKnife.inject(this);
        titleBar.initView(this);
        initData();
        initView();
    }

    private void initView() {

        edtMessage.setHint("");

        edtRecipient.setText(otherOrder.getContactName());
        edtPhone.setText(otherOrder.getContactPhone());
        edtAddress.setText(otherOrder.getContactAddr());
        tvDeliverTime.setText(otherOrder.getTimeType());
        edtMessage.setText(otherOrder.getRemark());


        edtRecipient.setFocusable(false);
        edtPhone.setFocusable(false);
        edtAddress.setFocusable(false);
        edtMessage.setFocusable(false);
        // TODO: 2017/5/11 默认隐藏 只有管理员才可以见
        if (App.userInfo.getIsAdmin() == 1) {
            llConfirm.setVisibility(View.VISIBLE);
        }
        if (otherOrder.getOrderState()!=0){
             tvSelf.setVisibility(View.GONE);
        }
        setConfiromButton(otherOrder.getOrderState());
    }

    private void initData() {
        otherOrder = getIntent().getParcelableExtra("otherOrder");
    }

    OtherOrder otherOrder;


    @OnClick({R.id.tv_confirm,R.id.tv_self})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                if (otherOrder.getOrderState()!= 0) {
                    return;
                }
                updateProductOrderState(1);//完成
                break;
            case R.id.tv_self:
                if (otherOrder.getOrderState() != 0) {
                    return;
                }
                updateProductOrderState(2);//自取
                break;

        }
    }

    /**
     * 增加状态
     * @param state
     */
    private void updateProductOrderState(final int state) {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("mappingId", otherOrder.getMappingId());
        params.put("orderType", otherOrder.getOrderType());
        params.put("state", state);
        ExpressService service = RetrofitUtil.create(ExpressService.class);
        showLoadDialog("");
        Call<ResponseBody> call = service.updateOtherOrderState(params);
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
                if (!rb.getResult().equals("1")) {
                    showShortToast(rb.getMessage());
                    return;
                }
                tvSelf.setVisibility(View.GONE);
                setConfiromButton(state);
                showShortToast(rb.getMessage());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
            }
        });
    }

    /**
     * 对应订单的三种状态
     * @param type
     */
    private void setConfiromButton(int type) {
        switch (type) {
            case 0:
                tvConfirm.setText("完成");
                tvConfirm.setBackgroundColor(ContextCompat.getColor(OtherOrderDetailAty.this, R.color.theme_red));
                break;
            case 1:
                tvConfirm.setText("已完成");
                tvConfirm.setBackgroundColor(ContextCompat.getColor(OtherOrderDetailAty.this, R.color.gray_text));
                break;
            case 2:
                tvConfirm.setText("已自取");
                tvConfirm.setBackgroundColor(ContextCompat.getColor(OtherOrderDetailAty.this, R.color.gray_text));
                break;
        }

    }
}
