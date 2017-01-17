package com.yiyekeji.coolschool.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.utils.SoapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 分类首页
 * Created by lxl on 2016/8/16 0016.
 */
public class CategoryFragment extends BaseFragment {

    @InjectView(R.id.rv_producttype)
    RecyclerView rvProducttype;
    @InjectView(R.id.rv_category)
    RecyclerView rvCategory;
    @InjectView(R.id.tv_refresh)
    TextView tvRefresh;


    private List<Category> categoryList = new ArrayList<>();
    private HashMap<Category, List<Category>> SubCategoryMap = new HashMap<>();
    private int color_black;
    private int color_gray;
    private int color_red;
    private ProductAdapter productAdapter;
    private CategoryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        color_black = ContextCompat.getColor(getContext(), R.color.black);
        color_gray = ContextCompat.getColor(getContext(), R.color.background_gray);
        color_red = ContextCompat.getColor(getContext(), R.color.theme_red);
        initProductAdapter();
        initClassifyAdapter();
    }

    public void initData() {
        if (!categoryList.isEmpty()){
            return;
        }
        getAllIndustryList();
    }

    /**
     * 设置 右侧recycleview适配器
     * 在点击中更新
     */
    LinearLayoutManager linearLayoutManager;

    private void initProductAdapter() {
        productAdapter = new ProductAdapter(getActivity(), SubCategoryMap);
        rvProducttype.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProducttype.setAdapter(productAdapter);
        rvProducttype.setItemAnimator(new DefaultItemAnimator());
        rvProducttype.setHasFixedSize(true);
    }


    private int rvHeight;
    private int itemHeight = 0, offset;

    /**
     * 左侧栏
     */
    private void initClassifyAdapter() {
        mAdapter = new ClassifyAdapter(getActivity(), categoryList);
        rvCategory.setAdapter(mAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(linearLayoutManager);
        mAdapter.setOnItemClickLitener(new ClassifyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = categoryList.get(position);
                getProductTypeList(category.getId());
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
    }

    /**
     * 获得所有行业
     * 设置默认的第一行数据
     */
    private void getAllIndustryList() {
        showProgressDialog("正在加载");
        SoapUtils.call(Config.YiYe8ServerSystem_TYPE, MarketMethodUtils.GET_INDUSTRY_CATEGORY_LIST, false, null, new SoapUtils.CallBack() {
            @Override
            public void onSuccess(ResultParser parser) {
                getLoadDialog().dismiss();
                Category category;
                List<Map<String, String>> list = parser.getList("a:Category");
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    category = new Category();
                    SoapUtils.reflectTest(category, map);
                    //初始化第一个
                    if (i == 0) {
                        category.setSelect(true);
                        getProductTypeList(category.getId());
                    }
                    categoryList.add(category);
                }
                mAdapter.notifyDataSetChanged();
                final ViewTreeObserver vto = rvCategory.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rvCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        rvHeight = rvCategory.getMeasuredHeight();
                        LogUtils.d("onGlobalLayout", rvHeight + "===" + rvCategory.getHeight());
                    }
                });
                LogUtils.d("CategoryFragment", categoryList.size());
            }

            @Override
            public void onFailure(String errorString, String errorCode) {
                getLoadDialog().dismiss();
                showShortToast("网络连接失败");
                tvRefresh.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 获得二级三级类型
     * 三级根据二级的parentId加入以二级category为key的Map中
     * 二级
     *
     * @param industryId
     */
    private void getProductTypeList(String industryId) {

        showProgressDialog("");
        List<RequestParam> params = new ArrayList<>();
        RequestParam industryIdParam = new RequestParam("industryId", industryId, null);

        params.add(industryIdParam);
        SoapUtils.call(Config.YiYe8ServerSystem_TYPE, MarketMethodUtils.GET_INDUSTRY_SUB_CATEGORY_MAP, false, params, new SoapUtils.CallBack() {
            @Override
            public void onSuccess(ResultParser parser) {
                SubCategoryMap.clear();
                List<Map<String, String>> keylist = parser.getList("a:Key");
                List<Map<String, String>> valuelist = parser.getList("b:Category");
                Category category;
                List<Category> keyCategories = new ArrayList<Category>();
                List<Category> valueCategories = new ArrayList<Category>();
                //所有Key
                for (int i = 0; i < keylist.size(); i++) {
                    Map<String, String> map = keylist.get(i);
                    category = new Category();
                    SoapUtils.reflectTest(category, map, "b");
                    keyCategories.add(category);
                }
                //所有value
                for (int i = 0; i < valuelist.size(); i++) {
                    Map<String, String> map = valuelist.get(i);
                    category = new Category();
                    SoapUtils.reflectTest(category, map, "b");
                    valueCategories.add(category);
                }
                LogUtils.d("SubCategory", keyCategories.size() + "=" + valueCategories.size());
                for (Category category1 : keyCategories) {
                    List<Category> categoryList = new ArrayList<Category>();
                    LogUtils.d("key", category1.getShowName());
                    for (Category category2 : valueCategories) {
                        if (category1.getId().equals(category2.getParentId())) {
                            categoryList.add(category2);
                        }
                    }
                    SubCategoryMap.put(category1, categoryList);
                }
                getLoadDialog().dismiss();
                productAdapter.notifyDataSetChanged(SubCategoryMap);
                LogUtils.d("SubCategoryMap", SubCategoryMap.size());

            }

            @Override
            public void onFailure(String errorString, String errorCode) {
                getLoadDialog().dismiss();
                tvRefresh.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @OnClick(R.id.tv_refresh)
    public void onClick() {
        tvRefresh.setVisibility(View.GONE);
        getAllIndustryList();
    }
}
