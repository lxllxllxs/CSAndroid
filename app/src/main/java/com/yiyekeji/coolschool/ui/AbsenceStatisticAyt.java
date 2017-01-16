package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CutClassNumInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.adapter.AbsenceStaticAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
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
 * Created by lxl on 2017/1/16.
 */
public class AbsenceStatisticAyt extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    AbsenceStaticAdapter mAdapter;
    List<CutClassNumInfo> infos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycleview);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initView() {
        titleBar.initView(this);
        mAdapter=new AbsenceStaticAdapter(this,infos);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    RollCallService service;
    private void initData() {
        String courseNo=getIntent().getStringExtra("courseNo");
        if (TextUtils.isEmpty(courseNo)) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("courseNo", courseNo);
        service = RetrofitUtil.create(RollCallService.class);
        showLoadDialog("");
        Call<ResponseBody> call = service.getStudenCutClassNum(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                infos = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CutClassNumInfo>>() {
                        }.getType(), "cutClassNumInfo");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infos != null) {
                    mAdapter.notifyDataSetChanged(infos);
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
            }
        });
    }
}
