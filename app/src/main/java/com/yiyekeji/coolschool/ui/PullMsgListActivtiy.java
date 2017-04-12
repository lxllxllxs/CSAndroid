package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.dao.PullMsg;
import com.yiyekeji.coolschool.db.DbUtil;
import com.yiyekeji.coolschool.ui.adapter.PullMsgListAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lxl on 2017/1/14.
 */
public class PullMsgListActivtiy extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    PullMsgListAdapter mAdapter;
    List<PullMsg> msgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycleview);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    private void initData() {
        msgList.addAll(DbUtil.getAllPullMsg());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        titleBar.initView(this);
        mAdapter=new PullMsgListAdapter(this,msgList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickLitener(new PullMsgListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                PullMsg msg=msgList.get(position);
                msg.setIsRead(1);
                DbUtil.upDatePullMsg(msg);
                Intent intent = new Intent(PullMsgListActivtiy.this, PullMsgDetailAty.class);
                intent.putExtra("content",msg.getContent());
                startActivity(intent);
            }
        });
    }
}
