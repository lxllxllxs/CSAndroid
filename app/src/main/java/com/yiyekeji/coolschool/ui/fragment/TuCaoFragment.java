package com.yiyekeji.coolschool.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.TuCao;
import com.yiyekeji.coolschool.ui.PublishTuCaoAyt;
import com.yiyekeji.coolschool.ui.TuCaoDetailAty;
import com.yiyekeji.coolschool.ui.adapter.TuCaoAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.widget.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/23.
 */
public class TuCaoFragment extends BaseFragment {
    @InjectView(R.id.prrv_pull_refresh_view)
    PullToRefreshRecycleView prrvPullRefreshView;
    @InjectView(R.id.tv_emptyView)
    TextView tvEmptyView;

    RecyclerView recyclerView;
    TuCaoAdapter pullRreshAdapter;
    List<TuCao> datas = new ArrayList<>();
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tu_cao, container, false);
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
        setPullRefreshListView();
    }

    private void initData() {
        TuCao tu;
        for (int i = 0; i < 10; i++) {
            tu = new TuCao();
            tu.setContent(i + "asdasdadadzxclzjcwepoirupwoitpg;lcxv;l;hkjl;");
            tu.setCommentCount(i + "");
            tu.setAuthor("老王");
            tu.setDate("03-08 20:30");
            datas.add(tu);
        }
    }

    private void setPullRefreshListView() {
        //使其支持上下拉事件
        prrvPullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView = prrvPullRefreshView.getRefreshableView();
        pullRreshAdapter = new TuCaoAdapter(getContext(), datas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        prrvPullRefreshView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<RecyclerView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<RecyclerView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                tvEmptyView.setVisibility(View.GONE);
            }
        });
        prrvPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            }

            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                showShortToast("上拉加载更多");
            }
        });
        pullRreshAdapter.setOnItemClickLitener(new TuCaoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), TuCaoDetailAty.class);
                intent.putExtra("info", datas.get(position));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(pullRreshAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({ R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                Intent intent = new Intent(getActivity(), PublishTuCaoAyt.class);
                startActivity(intent);
                break;
        }
    }
}
