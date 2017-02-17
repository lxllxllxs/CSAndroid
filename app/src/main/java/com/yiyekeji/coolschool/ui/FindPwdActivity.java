package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.UserService;
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
 * Created by lxl on 2017/2/4.
 */
public class FindPwdActivity extends BaseActivity {


    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.ledt_userNum)
    LableEditView ledtUserNum;
    @InjectView(R.id.ledt_newPwd)
    LableEditView ledtNewPwd;
    @InjectView(R.id.ledt_reaptPwd)
    LableEditView ledtReaptPwd;
    @InjectView(R.id.ledt_pswOrCode)
    LableEditView ledtPswOrCode;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;
    @InjectView(R.id.iv_student)
    ImageView ivStudent;
    @InjectView(R.id.iv_teacher)
    ImageView ivTeacher;
    private int roleTye = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        titleBar.initView(this);
    }


    private void appFindPsw() {
        if (TextUtils.isEmpty(ledtUserNum.getEditText())) {
            showShortToast("账号不能为空！");
            return;
        }
        if (ledtNewPwd.getEditText().length() < 6 || ledtNewPwd.getEditText().length() > 8) {
            showShortToast("密码长度为6~8！");
            return;
        }
        if (!ledtNewPwd.getEditText().equals(ledtReaptPwd.getEditText())) {
            showShortToast("新密码两次输入不一致！");
            return;
        }
        if (TextUtils.isEmpty(ledtPswOrCode.getEditText())) {
            if (roleTye == 1) {
                showShortToast("邀请码不能为空！");
            } else {
                showShortToast("子系统密码不能为空！");
            }
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userNum", ledtUserNum.getEditText());
        params.put("newPsw", ledtNewPwd.getEditText());
        params.put("roleType", roleTye);
        params.put("codeOrPsw", ledtPswOrCode.getEditText());

        UserService service = RetrofitUtil.create(UserService.class);
        Call<ResponseBody> call = service.appFindPsw(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("修改成功！");
                    startActivity(LoginActivity.class);
                    finish();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });
    }

    @OnClick({R.id.tv_confirm, R.id.iv_student, R.id.iv_teacher})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                appFindPsw();
                break;
            case R.id.iv_student:
                roleTye = 0;
                ivStudent.setImageResource(R.mipmap.single_select);
                ivTeacher.setImageResource(R.mipmap.single_no_select);
                ledtPswOrCode.setLabelText("子系统密码");
                break;
            case R.id.iv_teacher:
                roleTye = 1;
                ivStudent.setImageResource(R.mipmap.single_no_select);
                ivTeacher.setImageResource(R.mipmap.single_select);
                ledtPswOrCode.setLabelText("邀请码");
                break;
        }
    }



}
