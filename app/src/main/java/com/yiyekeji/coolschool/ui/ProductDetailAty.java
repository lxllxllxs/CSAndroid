package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.graphics.Color;
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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductInfo;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.bean.ProductOrderItem;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.SupplierInfo;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.ImageAdapter;
import com.yiyekeji.coolschool.ui.adapter.SelectModelAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.CountView;
import com.yiyekeji.coolschool.widget.DockAtTopScrollView;

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
public class ProductDetailAty extends BaseActivity implements DockAtTopScrollView.OnScrollListener {
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
    @InjectView(R.id.tv_desc)
    TextView tvDesc;
    @InjectView(R.id.tv_contac)
    TextView tvContac;
    @InjectView(R.id.sv_main)
    DockAtTopScrollView svMain;
    @InjectView(R.id.tv_price)
    TextView tvPrice;
    private int gradualHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.inject(this);
//        initView();
        initData();
    }

    private void initViewAfter() {
        ivBack.setImageResource(R.mipmap.ic_product_back);
        tvTitle.setAlpha(0);
        initScrollListener();

        if (productInfo == null) {
            return;
        }
        tvProductName.setText(productInfo.getpTitle());
        tvDesc.setText(productInfo.getpDescrition());
        tvPrice.setText(getLowestPrice());
        GlideUtil.setImageToView(productInfo.getpImage(), imageView);
        imageAdapter = new ImageAdapter(this, productInfo.getPictureList());
        rvImgs.setAdapter(imageAdapter);
        rvImgs.setLayoutManager(new LinearLayoutManager(this));

        getSellerInfo();
    }

    private void initScrollListener() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
                gradualHeight = imageView.getHeight() - rlTitleContainer.getHeight();
                svMain.setOnScrollListener(ProductDetailAty.this);
            }
        });
    }


    private void getSellerInfo() {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("pId", getIntent().getIntExtra("pId", 0));
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.getSupplierInfo(parms);
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
                    SupplierInfo info = GsonUtil.fromJSon(jsonString, SupplierInfo.class, "supplierInfo");
                    if (info == null) {
                        return;
                    }
                    tvContac.setText(info.getsName().concat(info.getsPhone() == null ? "" : info.getsPhone()));
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

    private void initData() {
        int id = getIntent().getIntExtra("pId", 0);
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


    CountView countView;
    private void popConfirmWindow(View view) {
        if (productInfo == null) {
            return;
        }
        if (popWindow == null) {
            final View contentView = LayoutInflater.from(this).inflate(R.layout.layout_product_detail_popwindow, null);
            final TextView tv_price = (TextView) contentView.findViewById(R.id.tv_price_interval);
            final TextView tv_total = (TextView) contentView.findViewById(R.id.tv_total_goods);
            ImageView iv_product = (ImageView) contentView.findViewById(R.id.iv_main_product);
            countView = (CountView) contentView.findViewById(R.id.countView);
            final TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
            rvModel = (RecyclerView) contentView.findViewById(R.id.rv_model);
            //设置主图
            GlideUtil.setImageToView(productInfo.getpImage(), iv_product);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow.isShowing()) {
                        popWindow.dismiss();
                    }
                    if (countView.getCount()<1){
                        showShortToast("至少选中一件");
                        return;
                    }
                    if (isShoppingCar) {
                        addToShoppingCar();
                    } else {
                        createOrder();
                    }
                }
            });

            //设置型号
            modelAdapter = new SelectModelAdapter(ProductDetailAty.this, productInfo.getModelList());
            rvModel.setAdapter(modelAdapter);
            rvModel.setLayoutManager(new GridLayoutManager(this, 4));
            modelAdapter.setOnItemClickLitener(new SelectModelAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    ProductModel model = productInfo.getModelList().get(position);
                    countView.setTotalGoods(model.getPmBalance());
                    tv_price.setText(getString(R.string.yuan).concat(model.getPmPrice()));
                    tv_total.setText("库存量："
                            .concat(String.valueOf(model.getPmBalance()))
                            .concat(productInfo.getpUnit()));
                    productOrderItem.setPmId(model.getPmId());
                    productOrderItem.setPrice(model.getPmPrice());
                    productOrderItem.setmTitle(model.getPmTitle());
                }
            });
            //设置初始数据

            ProductModel model = productInfo.getModelList().get(0);
            productInfo.getModelList().get(0).setSelect(true);
            productOrderItem.setPmId(model.getPmId());
            productOrderItem.setmTitle(model.getPmTitle());
            countView.setTotalGoods(model.getPmBalance());
            tv_price.setText(getString(R.string.yuan).concat(model.getPmPrice()));
            tv_total.setText("库存量："
                    .concat(String.valueOf(model.getPmBalance()))
                    .concat(productInfo.getpUnit()));
            productOrderItem.setPrice(model.getPmPrice());


            //第二层（在PopupWindow是第一层）为相对布局时WrapContent失效
            popWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        setPopupWindow(view);
        // 设置好参数之后再show
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 创建订单
     */
    ProductOrderItem productOrderItem=new ProductOrderItem();
    private void createOrder() {
        ArrayList<ProductOrderItem> itemArrayList = new ArrayList<>();
        productOrderItem.setpId(productInfo.getPid());
        if (countView == null) {
            return;
        }
        productOrderItem.setUnit(productInfo.getpUnit());
        productOrderItem.setImgPath(productInfo.getpImage());
        productOrderItem.setProductName(productInfo.getpTitle());
        productOrderItem.setPmCount(countView.getCount());
        //计算总价
        productOrderItem.setSubTotal(countView.getCount() * Double.valueOf(productOrderItem.getPrice()));

        itemArrayList.add(productOrderItem);
        Intent intent = new Intent(ProductDetailAty.this, CreateOrderAty.class);
        intent.putExtra("itemList", itemArrayList);
        startActivity(intent);

    }

    //放入我的购物车
    private void addToShoppingCar() {
    }

    private String getLowestPrice() {
        List<Double> list = new ArrayList<>();
        if (productInfo.getModelList().isEmpty() || productInfo.getModelList() == null) {
            return "";
        }
        for (ProductModel model : productInfo.getModelList()) {
            list.add(Double.valueOf(model.getPmPrice()));
        }
        Collections.sort(list);
        return "" + list.get(0);
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

    /**
     * 应该是标题栏的底部与图片底部重合时 标题栏就完全不透明
     * 需要减去标题栏高度得出渐变高度gradualHeight
     *
     * @param scrollY alpha=255完全不透明
     *                两组图片 透明度变化是背景的两倍
     *                过中线切换
     *                前淡出 alpha值减 后淡入 alpha值升
     *                <p/>
     *                背景alpha值单独一个 持续增加
     */
    @Override
    public void onScroll(int scrollY) {
        float scale = (float) scrollY / gradualHeight;
        float alpha = (255 * scale);
        float tvAlpha = 1.0f * scale;
        float imgScale = (2 * (float) scrollY - gradualHeight) / gradualHeight;
        float imgAlpha = (255 * imgScale);
        if (scrollY <= 0) {
            ivBack.setImageAlpha(255);
            ivBack.setTag(R.mipmap.ic_product_back);
            ivProductBuycar.setImageAlpha(255);
            ivProductBuycar.setTag(R.mipmap.ic_product_buycar);
            tvTitle.setAlpha(tvAlpha);
            rlTitleContainer.setBackgroundColor(Color.argb((int) 0, 235, 235, 235));//AGB由相关工具获得，或者美工提供
        } else if (scrollY > 0 && scrollY < gradualHeight / 2) {
            if (!ivBack.getTag().equals(R.mipmap.ic_product_back)) {
                ivBack.setVisibility(View.INVISIBLE);
                ivBack.setImageResource(R.mipmap.ic_product_back);
                ivBack.setTag(R.mipmap.ic_product_back);
                ivBack.setVisibility(View.VISIBLE);

                ivProductBuycar.setVisibility(View.INVISIBLE);
                ivProductBuycar.setImageResource(R.mipmap.ic_product_buycar);
                ivProductBuycar.setVisibility(View.VISIBLE);
            }

            tvTitle.setAlpha(tvAlpha);//保持增加alpha值
            ivBack.setImageAlpha(255 - (int) imgAlpha);
            ivProductBuycar.setImageAlpha(255 - (int) imgAlpha);
            rlTitleContainer.setBackgroundColor(Color.argb((int) alpha, 235, 235, 235));
        } else if ((scrollY < gradualHeight && scrollY >= gradualHeight / 2)) {
            if (!ivBack.getTag().equals(R.mipmap.ic_product_back2)) {
                ivBack.setVisibility(View.INVISIBLE);
                ivBack.setImageResource(R.mipmap.ic_product_back2);
                ivBack.setTag(R.mipmap.ic_product_back2);
                ivBack.setVisibility(View.VISIBLE);

                ivProductBuycar.setVisibility(View.INVISIBLE);
                ivProductBuycar.setImageResource(R.mipmap.ic_product_buycar2);
                ivProductBuycar.setVisibility(View.VISIBLE);
            }

            tvTitle.setAlpha(tvAlpha);//保持增加alpha值
            ivBack.setImageAlpha((int) imgAlpha);
            ivProductBuycar.setImageAlpha((int) imgAlpha);
            rlTitleContainer.setBackgroundColor(Color.argb((int) alpha, 235, 235, 235));
        } else {
            tvTitle.setAlpha(1.0f);//保持增加alpha值
            ivBack.setImageAlpha(255);
            ivProductBuycar.setImageAlpha(255);
            rlTitleContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        }
    }

}
