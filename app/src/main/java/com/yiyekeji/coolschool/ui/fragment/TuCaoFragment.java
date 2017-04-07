package com.yiyekeji.coolschool.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.TuCao;
import com.yiyekeji.coolschool.inter.TuCaoService;
import com.yiyekeji.coolschool.ui.PublishTuCaoAyt;
import com.yiyekeji.coolschool.ui.TuCaoDetailAty;
import com.yiyekeji.coolschool.ui.adapter.TuCaoAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    List<TuCao> tuCaoList = new ArrayList<>();
    @InjectView(R.id.iv_publishTuCao)
    ImageView ivPublishTuCao;

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

    }

    private int lastTuCaoId=999999;
    private void setPullRefreshListView() {
        //使其支持上下拉事件
        prrvPullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView = prrvPullRefreshView.getRefreshableView();
        pullRreshAdapter = new TuCaoAdapter(getContext(), tuCaoList);
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
                lastTuCaoId=999999;
                tuCaoList.clear();
                getTuCaoList();
            }
            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (tuCaoList.size() < 1) {
                    lastTuCaoId=999999;
                } else {
                    lastTuCaoId = tuCaoList.get(tuCaoList.size() - 1).getId();//降序 拿最后一个
                }
                getTuCaoList();
            }
        });
        pullRreshAdapter.setOnItemClickLitener(new TuCaoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), TuCaoDetailAty.class);
                intent.putExtra("tuCao", tuCaoList.get(position));
                startActivity(intent);
            }
        });

        if ( App.getUserInfo().getIsAdmin()==1){
            setDeleteMenu();
        }
        recyclerView.setAdapter(pullRreshAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
    }

    private void setDeleteMenu() {
        pullRreshAdapter.setOnItemLongClickLitener(new TuCaoAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(View view, int position) {
                TuCao tuCao=tuCaoList.get(position);
                int cut=tuCao.getContent().length();
                showDialog(cut>10?tuCao.getContent().substring(0,10):tuCao.getContent());
            }
        });
    }

    public void showDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("确定删除该条信息吗");//设置标题内容
        builder.setMessage(content);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                showShortToast("删除这条吐槽");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        final AlertDialog dlg = builder.create();
        dlg.show();
    }
    private static final int PAGE_SIZE = 15;



    /**
     * tucao/getTuCaoList
     */
    private void getTuCaoList() {
        TuCaoService service = RetrofitUtil.create(TuCaoService.class);
        Map<String, Object> params = new HashMap<>();
        params.put("tuCaoId", lastTuCaoId);
        params.put("pageSize", PAGE_SIZE);
        Call<ResponseBody> call = service.getTuCaoList(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (getContext() == null) {
                    return;
                }
                dismissDialog();
                prrvPullRefreshView.onRefreshComplete();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    tuCaoList = GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<TuCao>>() {
                            }.getType(), "tuCaoList");
                    if (tuCaoList != null) {
                        if (tuCaoList.size()<1){
                            showShortToast("暂无更多内容");
                            return;
                        }
                        pullRreshAdapter.notifyDataSetChanged(tuCaoList);
                    } else {
                        showShortToast("发生错误");
                    }
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                prrvPullRefreshView.onRefreshComplete();
                showShortToast(getString(R.string.response_err));
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.iv_publishTuCao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_publishTuCao:
                Intent intent = new Intent(getActivity(), PublishTuCaoAyt.class);
                startActivity(intent);
                break;
        }
    }
}
