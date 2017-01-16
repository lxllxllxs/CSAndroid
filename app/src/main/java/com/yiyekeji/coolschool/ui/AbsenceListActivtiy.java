package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseAbsenceInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.adapter.AbsenceListAdapter;
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
 * 缺勤记录 （缺勤列表）
 * Created by lxl on 2017/1/14.
 */
public class AbsenceListActivtiy extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    AbsenceListAdapter mAdapter;
    List<CourseAbsenceInfo> infoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycleview);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    String courseNo;
    private void initData() {
         courseNo=getIntent().getStringExtra("courseNo");
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("courseNo", courseNo);
        RollCallService service = RetrofitUtil.create(RollCallService.class);
        showLoadDialog("");
        Call<ResponseBody> call=service.getCutClassCount(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                infoList= GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CourseAbsenceInfo>>() {}.getType(),"cutClassInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infoList!=null) {
                    mAdapter.notifyDataSetChanged(infoList);
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

    private void initView() {
        titleBar.initView(this);
        titleBar.setTvRight("次数统计", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AbsenceListActivtiy.this, AbsenceStatisticAyt.class);
                intent.putExtra("courseNo", courseNo);
                startActivity(intent);
            }
        });


        mAdapter=new AbsenceListAdapter(this,infoList);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickLitener(new AbsenceListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(AbsenceListActivtiy.this, AbsenceRecordAty.class);
                intent.putExtra("courseTime", infoList.get(position).getCourseTime());
                intent.putExtra("courseNo", courseNo);
                startActivity(intent);
            }
        });
    }
}
