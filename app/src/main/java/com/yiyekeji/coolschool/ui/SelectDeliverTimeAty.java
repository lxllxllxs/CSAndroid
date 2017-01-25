package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ChoseBean;
import com.yiyekeji.coolschool.ui.adapter.ChoseAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lxl on 2017/1/25.
 */
public class SelectDeliverTimeAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<ChoseBean> choseBeanList = new ArrayList<>();
    ChoseAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycleview);
        ButterKnife.inject(this);
        initData();
        initView();

    }

    private void initView() {
        titleBar.initView(this);
        titleBar.setTitleText(title);
        mAdapter=new ChoseAdapter(this,choseBeanList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickLitener(new ChoseAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent();
                intent.putExtra("key",choseBeanList.get(position).getKey());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    String title;
    private void initData() {
        title=getIntent().getStringExtra("title");
        choseBeanList=getIntent().getParcelableArrayListExtra("choseBeanList");
    }
}
