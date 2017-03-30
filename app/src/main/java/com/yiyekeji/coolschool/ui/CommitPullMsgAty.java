package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.dao.PullMsg;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.inter.TeacherService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.DateUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;

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
 * Created by lxl on 2017/2/18.
 */
public class CommitPullMsgAty extends BaseActivity {
    PullMsg pullMsg = new PullMsg();
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.ledt_title)
    LableEditView ledtTitle;
    @InjectView(R.id.edt_descrition)
    EditText edtDescrition;
    @InjectView(R.id.tv_cancel)
    TextView tvCancel;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;
    @InjectView(R.id.sp_course)
    Spinner spCourse;
    List<CourseInfo> infoList = new ArrayList<>();
    private ArrayAdapter<String> arr_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_pull_msg);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    private void initView() {
        titleBar.initView(this);
    }

    private void initData() {
        getCourseList();
    }

    ArrayList<String> list = new ArrayList<>();
    private void setSpinner(){
        courseInfo2String(infoList,list);
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spCourse.setAdapter(arr_adapter);
        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String courseNo=list.get(position).split("：")[0];
                showShortToast("课程编号是："+courseNo);
                pullMsg.setCourseNo(courseNo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void courseInfo2String(List<CourseInfo> infos,ArrayList<String> list){
        for (CourseInfo info: infos) {
            //这里是第一个
            if (list.size()==0){
                pullMsg.setCourseNo(info.getCourseNo());
            }
            list.add(info.getCourseNo()+"："+info.getCourseName());
        }
    }

    @OnClick(R.id.tv_confirm)
    public void onClick() {
        if (!check()) {
            return;
        }
        commitPullMsg();
    }

    private boolean check() {
        if (TextUtils.isEmpty(ledtTitle.getEditText())) {
            showShortToast("标题不能为空！");
            return false;
        }
        if (ledtTitle.getEditText().length()>30){
            showShortToast("不能超过30个字");
            return false;
        }
        if (TextUtils.isEmpty(edtDescrition.getText())) {
            showShortToast("内容不能为空！");
            return false;
        }
        pullMsg.setTeacherName(App.getUserInfo().getName());
        pullMsg.setTitle(ledtTitle.getEditText());
        pullMsg.setContent(edtDescrition.getText().toString());
        pullMsg.setDate(DateUtils.getTimeString());
        pullMsg.setValidDay("30");
        return true;
    }

    private void commitPullMsg() {
        TeacherService service = RetrofitUtil.create(TeacherService.class);
        Call<ResponseBody> call = service.inserPullMsg(pullMsg);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                String jsonString = GsonUtil.toJsonString(response);
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast(rb.getMessage());
                    finish();
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                showShortToast(getString(R.string.response_err));
            }
        });
    }
    private void getCourseList() {
        RollCallService  service = RetrofitUtil.create(RollCallService.class);
        Map<String, Object> params = new HashMap<>();
        params.put("tnum", App.userInfo.getUserNum());
        params.put("tokenId", App.userInfo.getTokenId());
        Call<ResponseBody> call = service.getAllCourse(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    infoList = GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<CourseInfo>>() {
                            }.getType(), "courseInfo");
                    if (infoList == null) {
                        return;
                    }
                    LogUtil.d("CourseInfo", infoList.size());
                    setSpinner();
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
            }
        });
    }


}
