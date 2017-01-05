package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.text.TextUtils;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.RegisterService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RegexUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.CButton;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/5.
 */
public class RegisterActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.ledt_loginName)
    LableEditView ledtLoginName;
    @InjectView(R.id.ledt_pwd)
    LableEditView ledtPwd;
    @InjectView(R.id.ledt_confrimPwd)
    LableEditView ledtConfrimPwd;
    @InjectView(R.id.ledt_realName)
    LableEditView ledtRealName;
    @InjectView(R.id.cb_confirm)
    CButton cbConfirm;

    private int roleTye=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        titleBar.initView(this);
    }

    @OnClick(R.id.cb_confirm)
    public void onClick() {
        String loginName = ledtLoginName.getEditText();
        String realName = ledtRealName.getEditText();
        String pwd = ledtPwd.getEditText();
        String repeatPwd = ledtConfrimPwd.getEditText();

        if (TextUtils.isEmpty(loginName)) {
            showShortToast("用户账号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(realName)) {
            showShortToast("真实姓名不能为空！");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showShortToast("密码不能为空！");
            return;
        }
        if (TextUtils.isEmpty(repeatPwd)) {
            showShortToast("确认密码不能为空！");
            return;
        }
        if (!pwd.equals(repeatPwd)){
            showShortToast("两次密码不一致！！");
            return;
        }

        if (!RegexUtils.checkChinese(realName)){
            showShortToast("姓名请输入中文！");
            return;
        }
        if (!RegexUtils.checkDigit(loginName)){
            showShortToast("请输入正确学号！");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserNum(loginName);
        userInfo.setName(realName);
        userInfo.setPassword(pwd);
        userInfo.setRoleType(roleTye);

        RegisterService service = RetrofitUtil.create(RegisterService.class);
        Call<ResponseBean> call = service.register(userInfo);
        call.enqueue(new Callback<ResponseBean>() {
            @Override
            public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
                ResponseBean rb = response.body();
                LogUtil.d(rb.toString());
                if (rb.getResult().equals("1")){
                    showShortToast("注册成功！");
                }else {
                    showShortToast("操作失败！"+rb.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBean> call, Throwable t) {
                showShortToast(t.toString());
            }
        });

    }
}
