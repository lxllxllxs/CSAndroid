package com.yiyekeji.coolschool.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.MainMenu;
import com.yiyekeji.coolschool.ui.TeacherRollCallActivitiy;
import com.yiyekeji.coolschool.ui.adapter.HomeAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.widget.DividerGridItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 */
public class HomeFragment extends BaseFragment {


    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.title_bar)
    TitleBar titleBar;

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
        MainMenu m1 = null;
        if (App.userInfo.getRoleType() == 1) {
            m1 = new MainMenu("点名", R.mipmap.ic_roll_call, TeacherRollCallActivitiy.class);
        } else {
            m1 = new MainMenu("签到", R.mipmap.ic_sign_in, null);
        }

        MainMenu m2 = new MainMenu("上门收件", R.mipmap.ic_take_express, null);
        MainMenu m3 = new MainMenu("代拿快递", R.mipmap.ic_deliver, null);
        MainMenu m4 = new MainMenu("订水", R.mipmap.ic_order_water, null);

        MainMenu m5 = new MainMenu("再来一桶", R.mipmap.ic_deliver_water, null);
        MainMenu m6 = new MainMenu("打印", R.mipmap.ic_print, null);
        MainMenu m7 = new MainMenu("广告", R.mipmap.ic_ad, null);

        mainMenuList.add(m1);
        mainMenuList.add(m2);
        mainMenuList.add(m3);
        mainMenuList.add(m4);
        mainMenuList.add(m5);
        mainMenuList.add(m6);
        mainMenuList.add(m7);

    }

    private void initView() {
        titleBar.initView(getActivity(),"首页");

        mAdapter = new HomeAdapter(getActivity(), mainMenuList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}