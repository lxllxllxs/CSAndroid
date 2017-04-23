package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.dao.PullMsg;
import com.yiyekeji.coolschool.db.DbUtil;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.inter.StudentService;
import com.yiyekeji.coolschool.ui.adapter.PullMsgListAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.PullToRefreshRecycleView;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/14.
 */
public class PullMsgListActivtiy extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    RecyclerView recyclerView;
    PullMsgListAdapter mAdapter;
    List<PullMsg> msgList = new ArrayList<>();
    @InjectView(R.id.prrv_pull_refresh_view)
    PullToRefreshRecycleView prrvPullRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_pulltorefresh_recycleview);
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
        mAdapter = new PullMsgListAdapter(this, msgList);
        recyclerView = prrvPullRefreshView.getRefreshableView();
        prrvPullRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);


        prrvPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getMyCoures();
            }
            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickLitener(new PullMsgListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                PullMsg msg = msgList.get(position);
                msg.setIsRead(1);
                DbUtil.upDatePullMsg(msg);
                Intent intent = new Intent(PullMsgListActivtiy.this, PullMsgDetailAty.class);
                intent.putExtra("content", msg.getContent());
                startActivityWithAnima(intent);
            }
        });
    }


    private List<CourseInfo> infos;
    private void getMyCoures() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        RollCallService service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.getMyCourse(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    dismissDialog();
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                infos = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CourseInfo>>() {
                        }.getType(), "courseInfo");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infos != null) {
                    getPullMsg();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                prrvPullRefreshView.onRefreshComplete();
                showShortToast(t.toString());
            }
        });
    }

    private List<PullMsg> pullMsgs;
    private void getPullMsg() {
        Map<String, Object> params = new HashMap<>();
        StudentService service = RetrofitUtil.create(StudentService.class);
        ArrayList<String> courseNos = new ArrayList<>();
        for (CourseInfo info : infos) {
            courseNos.add(info.getCourseNo());
        }
        params.put("courseNoList",courseNos);
        Call<ResponseBody> call = service.getPullMsg(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                prrvPullRefreshView.onRefreshComplete();
                if (response.code() != 200) {
                    dismissDialog();
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                pullMsgs = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<PullMsg>>() {
                        }.getType(), "pullMsgList");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (pullMsgs != null) {
                    for (PullMsg msg : pullMsgs) {
                        DbUtil.insertPullMsg(msg);
                    }
                    refresh();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                prrvPullRefreshView.onRefreshComplete();
                dismissDialog();
                showShortToast(t.toString());
            }
        });
    }
    private void refresh() {
        // TODO: 2017/4/23 先清空 再次加载
        msgList.clear();
        msgList.addAll(DbUtil.getAllPullMsg());
        mAdapter.notifyDataSetChanged();
    }
}
