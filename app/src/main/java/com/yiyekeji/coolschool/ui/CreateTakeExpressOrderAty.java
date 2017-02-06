package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ChoseBean;
import com.yiyekeji.coolschool.bean.CreateProductOrderInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RegexUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/25.
 */
public class CreateTakeExpressOrderAty extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_take_express_order);
        ButterKnife.inject(this);
        initView();
        initData();
    }


    private void initView() {
        titleBar.initView(this);

        edtAddress.setText(App.userInfo.getAddr() == null ? "" : App.userInfo.getAddr());
        edtRecipient.setText(App.userInfo.getName());
        edtPhone.setText(App.userInfo.getPhone() == null ? "" : App.userInfo.getPhone());
    }

    private void initData() {
        getTimeTypeList();
    }

    @OnClick({R.id.tv_confirm, R.id.ll_deliverTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                if (check()) {
                    createOrder();
                }
                break;
            case R.id.ll_deliverTime:
                Intent intent = new Intent(CreateTakeExpressOrderAty.this, SelectDeliverTimeAty.class);
                intent.putExtra("choseBeanList", beanArrayList);
                intent.putExtra("title", "送货时间");
                startActivityForResult(intent, 0);
                break;
        }
    }


    ArrayList<ChoseBean> beanArrayList = new ArrayList<>();
    /**
     * 创建订单 前面已校验
     */
    CreateProductOrderInfo info = new CreateProductOrderInfo();

    private void createOrder() {
        ShopService service = RetrofitUtil.create(ShopService.class);

        Gson gson = new Gson();
        LogUtil.d("toJsonTree", gson.toJsonTree(info));
        LogUtil.d("toJsonTree", gson.toJsonTree(info, CreateProductOrderInfo.class));

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
                    finish();
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
        if (TextUtils.isEmpty(edtRecipient.getText())) {
            showShortToast("收货人不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(edtPhone.getText())) {
            showShortToast("手机号不能为空！");
            return false;
        }
        if (!RegexUtils.checkMobile(edtPhone.getText().toString().trim())) {
            showShortToast("手机号不正确！");
            return false;
        }
        if (TextUtils.isEmpty(edtAddress.getText())) {
            showShortToast("收货地址不能为空！");
            return false;
        }
        return true;
    }


    /**
     * 获得送货时间
     */
    private void getTimeTypeList() {
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.getTimeTypeList();
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
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Iterator iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        String value = jsonObject.getString(key);
                        ChoseBean bean = new ChoseBean();
                        bean.setKey(key);
                        bean.setValue(value);
                        beanArrayList.add(bean);
                    }
                    beanArrayList.get(0).setSelect(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        String key = data.getStringExtra("key");
        for (ChoseBean bean : beanArrayList) {
            bean.setSelect(false);//这里要清 intent是复制对象
            if (bean.getKey().equals(key)) {
                bean.setSelect(true);
                //设置
                info.setTimeType(Integer.valueOf(key));
                tvDeliverTime.setText((String) bean.getValue());
            }
        }
    }
}
