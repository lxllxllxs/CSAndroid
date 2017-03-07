package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.HashMap;
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
public class AddCourseAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.ledt_courseName)
    LableEditView ledtCourseName;
    @InjectView(R.id.ledt_class)
    LableEditView ledtClass;
    @InjectView(R.id.ledt_classNo)
    LableEditView ledtClassNo;
    @InjectView(R.id.ledt_count)
    LableEditView ledtCount;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    private void initView() {
        titleBar.initView(this);
    }

    private void initData() {
    }

    @OnClick(R.id.tv_confirm)
    public void onClick() {
        if (!check()){
            return;
        }
        addCourse();
    }

    private boolean check() {
        if (TextUtils.isEmpty(ledtCourseName.getEditText())){
            showShortToast("课程名不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(ledtClass.getEditText())){
            showShortToast("上课班级不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(ledtClassNo.getEditText())){
            showShortToast("上课教室不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(ledtCount.getEditText())){
            showShortToast("人数不能为空！");
            return false;
        }
        return true;
    }

    private void addCourse() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        params.put("courseName", ledtCourseName.getEditText());
        params.put("courseClass",ledtClass.getEditText());
        params.put("roomNo",ledtClassNo.getEditText());
        params.put("count",ledtCount.getEditText());
        RollCallService service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.insertCourse(params);
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
}
