package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ClassAbsenceInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.adapter.AbsenceRecordAdapter;
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
 * 缺勤名单
 * Created by lxl on 2017/1/14.
 */
public class AbsenceRecordAty extends BaseActivity {
    AbsenceRecordAdapter mAdapter;
    List<ClassAbsenceInfo> infoList = new ArrayList<>();
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

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
        titleBar.setTvRight("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getDeleteMode()) {
                    titleBar.setTvRightText("编辑");
                    mAdapter.setDeleteMode(false);
                } else {
                    titleBar.setTvRightText("完成");
                    mAdapter.setDeleteMode(true);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter=new AbsenceRecordAdapter(this,infoList);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickLitener(new AbsenceRecordAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                delItem=infoList.get(position);
                deleteRecord();
            }
        });
    }

    private ClassAbsenceInfo delItem;
    private void deleteRecord() {
        if (delItem == null) {
            return;
        }
        if (service == null) {
            service=RetrofitUtil.create(RollCallService.class);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("cutClassId", delItem.getCutClassId());
        showLoadDialog("");
        Call<ResponseBody> call = service.deleteCutClass(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")){
                    showShortToast("删除成功！");
                    infoList.remove(delItem);
                    mAdapter.notifyDataSetChanged();
                }else {
                    showShortToast("操作失败！"+rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });


    }

    RollCallService service;
    private void initData() {
        if (getIntent().getParcelableArrayListExtra("infos") != null) {
            infoList=getIntent().getParcelableArrayListExtra("infos");
            mAdapter.notifyDataSetChanged(infoList);
            return;
        }
        String courseTime = getIntent().getStringExtra("courseTime");
        String courseNo=getIntent().getStringExtra("courseNo");
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("courseTime", courseTime);
        params.put("courseNo", courseNo);

        service = RetrofitUtil.create(RollCallService.class);
        showLoadDialog("");
        Call<ResponseBody> call = service.getCutClassStudentList(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                infoList = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<ClassAbsenceInfo>>() {
                        }.getType(), "cutClassList");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infoList != null) {
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
}
