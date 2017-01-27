package com.yiyekeji.coolschool.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RegexUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.CButton;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;
import com.zhy.autolayout.utils.ScreenUtils;

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
    @InjectView(R.id.tv_lable)
    TextView tvLable;
    @InjectView(R.id.iv_student)
    ImageView ivStudent;
    @InjectView(R.id.iv_teacher)
    ImageView ivTeacher;
    @InjectView(R.id.ledt_verifyCode)
    LableEditView ledtVerifyCode;

    private int roleTye = 0;

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

    @OnClick({R.id.cb_confirm,R.id.iv_student,R.id.iv_teacher})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cb_confirm:
                for (int i=250;i<400;i++) {
                    ledtLoginName.setEditText("3112000"+i);
                    ledtConfrimPwd.setEditText("qqqqqq");
                    ledtPwd.setEditText("qqqqqq");
                    ledtRealName.setEditText("宝元");
                    checkAndRegister();
                }
                break;
            case R.id.iv_student:
                 roleTye=0;
                ivStudent.setImageResource(R.mipmap.single_select);
                ivTeacher.setImageResource(R.mipmap.single_no_select);
                break;
            case R.id.iv_teacher:
                roleTye=1;
                ivStudent.setImageResource(R.mipmap.single_no_select);
                ivTeacher.setImageResource(R.mipmap.single_select);
                break;

        }

    }

    private void checkAndRegister(){
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
        if (pwd.length()<6||pwd.length()>8){
            showShortToast("密码长度为6~8！");
            return;
        }
        if (TextUtils.isEmpty(repeatPwd)) {
            showShortToast("确认密码不能为空！");
            return;
        }
        if (!pwd.equals(repeatPwd)) {
            showShortToast("两次密码不一致！！");
            return;
        }

        if (!RegexUtils.checkChinese(realName)) {
            showShortToast("姓名请输入中文！");
            return;
        }
        if (realName.length() < 2 || realName.length() > 6) {
            showShortToast("姓名长度为2~6位！");
            return;
        }
        if (!RegexUtils.checkDigit(loginName)) {
            showShortToast("学工号为10位数字！");
            return;
        }
        if (loginName.length() != 10) {
            showShortToast("学工号为10位数字！");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserNum(loginName);
        userInfo.setName(realName);
        userInfo.setPassword(pwd);
        userInfo.setRoleType(roleTye);
        showLoadDialog("");
        UserService service = RetrofitUtil.create(UserService.class);
        Call<ResponseBean> call = service.register(userInfo);
        call.enqueue(new Callback<ResponseBean>() {
            @Override
            public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
                ResponseBean rb = response.body();
                getLoadDialog().dismiss();
                LogUtil.d(rb.toString());
                if (rb.getResult().equals("1")) {
                    showShortToast("注册成功！");
                    upLoadPhoneModel();//只上传一次设备数据
//                    startActivity(LoginActivity.class);
                } else {
                    showShortToast("操作失败！" + rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBean> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(t.toString());
            }
        });
    }



    /**
     * pType	String	手机型号
     * pSize	String 	手机屏幕大小
     */
    private void upLoadPhoneModel() {
        Map<String, Object> params = new HashMap<>();
        params.put("pType", Build.MODEL);
        int[] screenSize = ScreenUtils.getScreenSize(this, true);
        String Size = screenSize[0] + "×" + screenSize[1];
        params.put("pSize", Size);
        LogUtil.d("upLoadPhoneModel", params.toString());
        CommonService commonService = RetrofitUtil.create(CommonService.class);
        Call<ResponseBody> call = commonService.appUploadPhoneModel(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LogUtil.d("onResponse", "upload phone params successful!");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }
}
