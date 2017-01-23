package com.yiyekeji.coolschool.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductInfo;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.ImageAdapter;
import com.yiyekeji.coolschool.ui.adapter.SelectModelAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.CountView;

import java.util.ArrayList;
import java.util.Collections;
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
 * Created by lxl on 2017/1/22.
 */
public class ProductDetailAty extends BaseActivity {
    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.tv_productName)
    TextView tvProductName;
    @InjectView(R.id.rv_imgs)
    RecyclerView rvImgs;


    ImageAdapter imageAdapter;
    ProductInfo productInfo;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_product_buycar)
    ImageView ivProductBuycar;
    @InjectView(R.id.rl_title_container)
    RelativeLayout rlTitleContainer;
    @InjectView(R.id.tv_buy)
    TextView tvBuy;
    @InjectView(R.id.tv_shoppingcar)
    TextView tvShoppingcar;
    @InjectView(R.id.ll_bottom)
    LinearLayout llBottom;

    boolean isShoppingCar = false;
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
        GlideUtil.setImageToView(productInfo.getpImage(), imageView);
        imageAdapter = new ImageAdapter(this, productInfo.getPictureList());
        rvImgs.setAdapter(imageAdapter);
        rvImgs.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        int id = getIntent().getIntExtra("pid", 0);
        getProductInfo(id);
    }


    private void getProductInfo(int id) {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("pId", id);
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.getProductInfo(parms);
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
                    productInfo = GsonUtil.fromJSon(GsonUtil.getValueByTag(jsonString, "productInfo"),
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

    @OnClick({R.id.iv_back, R.id.iv_product_buycar, R.id.tv_buy, R.id.tv_shoppingcar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_product_buycar:
                jumpToShoppCar();
                break;
            case R.id.tv_buy:
                popConfirmWindow(view);
                break;
            case R.id.tv_shoppingcar:
                popConfirmWindow(view);
                break;
        }
    }

    private void jumpToShoppCar() {
        startActivity(MyShoppingCarAty.class);
    }



    PopupWindow popWindow;
    SelectModelAdapter modelAdapter;
    RecyclerView rvModel;
    private void popConfirmWindow(View view) {
        if (productInfo == null) {
            return;
        }
        if (popWindow == null) {
            final View contentView = LayoutInflater.from(this).inflate(R.layout.layout_product_detail_popwindow, null);
            final TextView tv_price = (TextView) contentView.findViewById(R.id.tv_price_interval);
            final TextView tv_total = (TextView) contentView.findViewById(R.id.tv_total_goods);
            ImageView iv_product = (ImageView) contentView.findViewById(R.id.iv_main_product);
            final CountView countView=(CountView)contentView.findViewById(R.id.countView);
            final TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
            rvModel=(RecyclerView)contentView.findViewById(R.id.rv_model);
            //设置主图
            GlideUtil.setImageToView(productInfo.getpImage(),iv_product);
            //设置价格区间
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //设置型号
            modelAdapter=new SelectModelAdapter(ProductDetailAty.this,productInfo.getModelList());
            rvModel.setAdapter(modelAdapter);
            rvModel.setLayoutManager(new GridLayoutManager(this,4));
            modelAdapter.setOnItemClickLitener(new SelectModelAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    ProductModel model=productInfo.getModelList().get(position);
                    countView.setTotalGoods(model.getPmBalance());
                    tv_price.setText("￥".concat(model.getPmPrice()));
                    tv_total.setText(String.valueOf(model.getPmBalance()));
                }
            });
            //设置初始数据
            productInfo.getModelList().get(0).setSelect(true);
            ProductModel model=productInfo.getModelList().get(0);
            countView.setTotalGoods(model.getPmBalance());
            tv_price.setText("￥".concat(model.getPmPrice()));
            tv_total.setText("库存量：".concat(String.valueOf(model.getPmBalance())));

            //第二层（在PopupWindow是第一层）为相对布局时WrapContent失效
            popWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        setPopupWindow(view);
        // 设置好参数之后再show
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private String getPriceInterval() {
        List<Double> list=new ArrayList<>();
        if (productInfo.getModelList().isEmpty() || productInfo.getModelList() == null) {
            return "";
        }
        for (ProductModel model : productInfo.getModelList()) {
                list.add(Double.valueOf(model.getPmPrice()));
        }
        Collections.sort(list);

        return "￥"+list.get(0)+"-"+list.get(list.size()-1);
    }

    /**
     * 设置属性
     *
     * @param view
     */
    private void setPopupWindow(View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                //使背景变暗
                backgroundAlpha(0.7f);
            }
        }, 200);
        popWindow.setAnimationStyle(R.style.popwin_anim_style);
        popWindow.setTouchable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        popWindow.setFocusable(true);
        //设置透明 完全透明 不设置的话back无法关闭 外部也不能触发
        popWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.transparent)));
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
