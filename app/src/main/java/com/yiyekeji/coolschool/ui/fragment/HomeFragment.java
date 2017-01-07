package com.yiyekeji.coolschool.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.MainMenu;
import com.yiyekeji.coolschool.ui.adapter.HomeAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.widget.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 */
public class HomeFragment extends BaseFragment {


    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    private HomeAdapter mAdapter;
    private List<MainMenu> mainMenuList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        MainMenu m1=new MainMenu("点名",R.mipmap.user_seller_order,null);
        MainMenu m2=new MainMenu("收件",R.mipmap.user_seller_order,null);
        MainMenu m3=new MainMenu("订水",R.mipmap.user_seller_order,null);
        MainMenu m4=new MainMenu("送水",R.mipmap.user_seller_order,null);
        MainMenu m5=new MainMenu("打印",R.mipmap.user_seller_order,null);
        MainMenu m6=new MainMenu("广告",R.mipmap.user_seller_order,null);
        mainMenuList.add(m1);
        mainMenuList.add(m2);
        mainMenuList.add(m3);
        mainMenuList.add(m4);
        mainMenuList.add(m5);
        mainMenuList.add(m6);

    }

    private void initView() {
        mAdapter = new HomeAdapter(getActivity(), mainMenuList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.addItemDecoration(new GridItemDecoration());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}