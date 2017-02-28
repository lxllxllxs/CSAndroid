package com.yiyekeji.coolschool.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.AdvertInfo;
import com.yiyekeji.coolschool.bean.CategoryInfo;
import com.yiyekeji.coolschool.bean.ProductInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.HaveName;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.ProductDetailAty;
import com.yiyekeji.coolschool.ui.adapter.HaveNameAdapter;
import com.yiyekeji.coolschool.ui.adapter.ProductAdapter;
import com.yiyekeji.coolschool.ui.adapter.RollPagerViewAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerGridItemDecoration;

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
 */
public class CategoryFragment extends BaseFragment {

    @InjectView(R.id.rv_category)
    RecyclerView rvCategory;
    @InjectView(R.id.rv_producttype)
    RecyclerView rvProducttype;
    @InjectView(R.id.tv_refresh)
    TextView tvRefresh;

    RollPagerViewAdapter rollPagerViewAdapter;
    ShopService service;
    HaveNameAdapter mCategoryAdapter;
    List<HaveName> infoList = new ArrayList<>();
    ProductAdapter productAdapter;
    List<ProductInfo> productInfoList = new ArrayList<>();
    @InjectView(R.id.roll_view_pager)
    RollPagerView rollViewPager;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        initRollPagerViewAdapter();

        mCategoryAdapter = new HaveNameAdapter(getActivity(), infoList);
        rvCategory.setAdapter(mCategoryAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(linearLayoutManager);
        mCategoryAdapter.setSelectColor(Color.WHITE, ContextCompat.getColor(getActivity(), R.color.theme_red));
        mCategoryAdapter.setOnItemClickLitener(new HaveNameAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                CategoryInfo info = (CategoryInfo) infoList.get(position);
                getProductLIst(info.getCategoryId());
                //初始化
                if (itemHeight == 0) {
                    int middlePostion = 0;
                    itemHeight = view.getHeight();
                    middlePostion = rvHeight / (itemHeight * 2);
                    offset = middlePostion * itemHeight;
                }
                //直接算 Y轴偏移量
                linearLayoutManager.scrollToPositionWithOffset(position, offset);
                //右侧栏滚动到顶部
                rvProducttype.scrollToPosition(0);

            }
        });
        //右侧产品
        productAdapter = new ProductAdapter(getActivity(), productInfoList);
        rvProducttype.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvProducttype.setAdapter(productAdapter);
        rvProducttype.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        productAdapter.setOnItemClickLitener(new ProductAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailAty.class);
                intent.putExtra("pId", productInfoList.get(position).getPid());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        service = RetrofitUtil.create(ShopService.class);
        getCategoryList();

        getAdList();
    }

    int rvHeight;
    int itemHeight = 0, offset;

    private void getCategoryList() {
//        showLoadDialog("");
        Call<ResponseBody> call = service.getShopCategoryList();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                infoList = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CategoryInfo>>() {
                        }.getType(), "categoryInfo");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infoList != null) {
                    infoList.get(0).setSelect(true);
                    getProductLIst(((CategoryInfo) infoList.get(0)).getCategoryId());
                    mCategoryAdapter.notifyDataSetChanged(infoList);
                    final ViewTreeObserver vto = rvCategory.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            rvCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            rvHeight = rvCategory.getMeasuredHeight();
                            LogUtil.d("onGlobalLayout", rvHeight + "===" + rvCategory.getHeight());
                        }
                    });
                    return;
                }
//                showShortToast(rb.getMessage());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });
    }

    private int cId;
    private void getProductLIst(int pcId) {
//        showLoadDialog("");
        productInfoList.clear();
        tvRefresh.setVisibility(View.GONE);
        cId = pcId;
        HashMap<String, Object> map = new HashMap<>();
        map.put("pcId", pcId);
        Call<ResponseBody> call = service.getProductList(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (!rb.getResult().equals("1")) {
//                    showShortToast(rb.getMessage());
                    productAdapter.notifyDataSetChanged();
                    tvRefresh.setVisibility(View.VISIBLE);
                    return;
                }
                productInfoList = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<ProductInfo>>() {
                        }.getType(), "productList");
                if (productInfoList != null) {
                    productAdapter.notifyDataSetChanged(productInfoList);
                } else {
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                getLoadDialog().dismiss();
                tvRefresh.setVisibility(View.VISIBLE);
                showShortToast(getString(R.string.response_err));
            }
        });
    }

    private void getAdList() {
//        showLoadDialog("");
        Call<ResponseBody> call = service.getShopAdvertList();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (!rb.getResult().equals("1")) {
//                    showShortToast(rb.getMessage());
                    return;
                }
                adList = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<AdvertInfo>>() {
                        }.getType(), "advertInfo");
                if (productInfoList != null) {
                    rollViewPager.setVisibility(View.VISIBLE);
                    rollPagerViewAdapter.notifyDataSetChanged(adList);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });
    }


    List<AdvertInfo> adList = new ArrayList<>();

    private void initRollPagerViewAdapter() {
        rollPagerViewAdapter = new RollPagerViewAdapter(adList, false);
        rollPagerViewAdapter.setOnItemClickLitener(new RollPagerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (adList.size() < 1) {
                    return;
                }
                AdvertInfo productInfo = adList.get(position);
                Intent intent = new Intent();
                intent.setClass(getContext(), ProductDetailAty.class);
                intent.putExtra("pId", productInfo.getpId());
                startActivity(intent);
            }
        });
        rollViewPager.setHintView(new ColorPointHintView(getActivity(), Color.parseColor("#F9B908"), Color.GRAY));
        //设置播放时间间隔
        rollViewPager.setPlayDelay(3000);
        //设置透明度
        rollViewPager.setAnimationDurtion(1000);
        //设置适配器
        rollViewPager.setAdapter(rollPagerViewAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.tv_refresh)
    public void onClick() {
        getProductLIst(cId);
    }
}