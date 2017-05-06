package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/7.
 */
public class UserInfomationActivity extends BaseActivity {

    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.tv_userNo)
    TextView tvUserNo;
    @InjectView(R.id.ll_userNo)
    LinearLayout llUserNo;
    @InjectView(R.id.tv_userType)
    TextView tvUserType;
    @InjectView(R.id.ll_userType)
    LinearLayout llUserType;
    @InjectView(R.id.tv_realName)
    TextView tvRealName;
    @InjectView(R.id.ll_realName)
    LinearLayout llRealName;
    @InjectView(R.id.tv_sex)
    TextView tvSex;
    @InjectView(R.id.ll_sex)
    LinearLayout llSex;
    @InjectView(R.id.tv_mobile)
    TextView tvMobile;
    @InjectView(R.id.ll_mobile)
    LinearLayout llMobile;
    @InjectView(R.id.tv_email)
    TextView tvEmail;
    @InjectView(R.id.ll_email)
    LinearLayout llEmail;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.ll_address)
    LinearLayout llAddress;

    UserInfo useInfo;

    public static final int REAL_NAME = 0;
    public static final int SEX = 1;
    public static final int ADDRESS = 2;
    public static final int EMAIL = 3;
    public static final int MOBILE = 4;
    public static final int NickName = 5;
    @InjectView(R.id.tv_nickName)
    TextView tvNickName;
    @InjectView(R.id.ll_nickName)
    LinearLayout llNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infomation);
        ButterKnife.inject(this);
        initData();
        titleBar.initView(this);
        titleBar.setBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
        initView();
    }

    private void initData() {
        //克隆值
        try {
            useInfo = (UserInfo) App.userInfo.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        tvUserNo.setText(useInfo.getUserNum());
        tvUserType.setText(useInfo.getRoleType() == 0 ? "学生" : "老师");
        tvRealName.setText(useInfo.getName());

        tvSex.setText(useInfo.getSex() == 1 ? "男" : "女");
        tvMobile.setText(useInfo.getPhone());
        tvAddress.setText(useInfo.getAddr());
        tvEmail.setText(useInfo.getEmail());
        tvNickName.setText(useInfo.getNickname());
    }

    @OnClick({R.id.ll_sex, R.id.ll_mobile, R.id.ll_email, R.id.ll_address,R.id.ll_nickName})
    public void onClick(View view) {
        Intent intent = new Intent(this, ModifyUserInfoActivity.class);
        intent.putExtra("userInfo", useInfo);
        switch (view.getId()) {
       /*     case R.id.ll_realName:
                intent.setFlags(REAL_NAME);
                startActivityForResult(intent,REAL_NAME);
                break;*/
            case R.id.ll_sex:
                // FIXME: 2017/4/24/024 这里不应该可以修改
            /*    intent.putExtra("sex", useInfo.getSex());
                intent.setFlags(SEX);
                startActivityForResult(intent,SEX);*/
                break;
            case R.id.ll_mobile:
                intent.setFlags(MOBILE);
                startActivityForResult(intent, MOBILE);
                break;
            case R.id.ll_email:
                intent.setFlags(EMAIL);
                startActivityForResult(intent, EMAIL);
                break;
            case R.id.ll_address:
                intent.setFlags(ADDRESS);
                startActivityForResult(intent, ADDRESS);
                break;
            case R.id.ll_nickName:
                intent.setFlags(NickName);
                startActivityForResult(intent, NickName);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        useInfo = data.getParcelableExtra("userInfo");
        initView();
    }

    @Override
    public void onBackPressed() {
        saveUserInfo();
    }

    /**
     * 如果没有改动就不用保存
     */
    private void saveUserInfo() {
        if (App.userInfo.equals(useInfo)) {
            finish();
            return;
        }
        UserService userService = RetrofitUtil.create(UserService.class);
        Call<ResponseBody> call = userService.appUpdateUserInfo(useInfo);

        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("保存成功！");
                    App.userInfo = GsonUtil.fromJSon(jsonString, UserInfo.class, "userInfo");
                } else {
                    showShortToast(rb.getMessage());
                }
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                showShortToast(t.getMessage());
                finish();
            }
        });
    }
}
