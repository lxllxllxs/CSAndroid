package com.yiyekeji.coolschool.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CategoryInfo;
import com.yiyekeji.coolschool.bean.Product;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.HaveName;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.HaveNameAdapter;
import com.yiyekeji.coolschool.ui.adapter.ProductAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
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

    ShopService service;
    HaveNameAdapter mCategoryAdapter;
    List<HaveName> infoList = new ArrayList<>();
    ProductAdapter productAdapter;
    List<Product> productList = new ArrayList<>();
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
        mCategoryAdapter = new HaveNameAdapter(getActivity(), infoList);
        rvCategory.setAdapter(mCategoryAdapter);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCategoryAdapter.setOnItemClickLitener(new HaveNameAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                CategoryInfo info = (CategoryInfo) infoList.get(position);
                getProductLIst(info.getCategoryId());
            }
        });
        //右侧产品
        productAdapter=new ProductAdapter(getActivity(),productList);
        rvProducttype.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rvProducttype.setAdapter(productAdapter);
        rvProducttype.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        productAdapter.setOnItemClickLitener(new ProductAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void initData() {
        service = RetrofitUtil.create(ShopService.class);
        getCategoryList();
    }

    private void getCategoryList() {
        showLoadDialog("");
        Call<ResponseBody> call = service.getShopCategoryList();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                infoList = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CategoryInfo>>() {
                        }.getType(), "categoryInfo");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infoList != null) {
                    mCategoryAdapter.notifyDataSetChanged(infoList);
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

    private void getProductLIst(int pcId) {
        showLoadDialog("");
        HashMap<String, Object> map = new HashMap<>();
        map.put("pcId",pcId);
        Call<ResponseBody> call = service.getProductList(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (!rb.getResult().equals("1")){
                    showShortToast(rb.getMessage());
                    return;
                }
                productList = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<Product>>() {
                        }.getType(), "productList");
                if (productList != null) {
                    productAdapter.notifyDataSetChanged(productList);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}