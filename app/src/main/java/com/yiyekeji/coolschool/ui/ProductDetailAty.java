package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/22.
 */
public class ProductDetailAty extends BaseActivity {
    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.tv_productName)
    TextView tvProductName;
    @InjectView(R.id.rv_imgs)
    RecyclerView rvImgs;


    ProductInfo productInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.inject(this);
//        initView();
        initData();
    }

    private void initViewAfter() {
        tvProductName.setText(productInfo.getpTitle());

    }
    private void initData() {
        int id=getIntent().getIntExtra("pid",0);
        getProductInfo(id);
    }

    @OnClick(R.id.rv_imgs)
    public void onClick() {
    }


    private void getProductInfo(int id){
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("pId",id);
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.getProductInfo(parms);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    productInfo = GsonUtil.fromJSon(GsonUtil.getValueByTag(jsonString,"productInfo"),
                            ProductInfo.class);
                    if (productInfo == null) {
                        return;
                    }

                    initViewAfter();
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
}
