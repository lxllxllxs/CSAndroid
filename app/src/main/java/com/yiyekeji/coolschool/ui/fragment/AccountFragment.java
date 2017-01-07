package com.yiyekeji.coolschool.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.UserInfomationActivity;
import com.yiyekeji.coolschool.ui.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 */
public class AccountFragment extends BaseFragment {


    @InjectView(R.id.ll_userInfo)
    LinearLayout llUserInfo;
    @InjectView(R.id.ll_setting)
    LinearLayout llSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll_userInfo, R.id.ll_setting})
    public void onClick(View view) {
        Intent intent=new Intent();
        switch (view.getId()) {
            case R.id.ll_userInfo:
                intent.setClass(getActivity(), UserInfomationActivity.class);
                break;
            case R.id.ll_setting:
                intent.setClass(getActivity(), UserInfomationActivity.class);
                break;
        }
        startActivity(intent);
    }
}