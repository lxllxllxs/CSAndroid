package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.adapter.HaveNameAdapter;
import com.yiyekeji.coolschool.inter.HaveName;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lxl on 2017/1/11.
 */
public class DynamicSignInActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<HaveName> haveNameList = new ArrayList<>();
    HaveNameAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_sign_in);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initView() {
        titleBar.initView(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickLitener(new HaveNameAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void initData() {
    }
}
