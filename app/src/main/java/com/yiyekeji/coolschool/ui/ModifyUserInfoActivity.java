package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.RegexUtils;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/7.
 */
public class ModifyUserInfoActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.tv_male)
    TextView tvMale;
    @InjectView(R.id.iv_male)
    ImageView ivMale;
    @InjectView(R.id.tv_female)
    TextView tvFemale;
    @InjectView(R.id.iv_female)
    ImageView ivFemale;
    @InjectView(R.id.tv_secret)
    TextView tvSecret;
    @InjectView(R.id.iv_secret)
    ImageView ivSecret;
    @InjectView(R.id.ll_modifySex)
    LinearLayout llModifySex;
    @InjectView(R.id.ledt_modifyName)
    LableEditView ledtModifyName;
    @InjectView(R.id.ledt_modifyMobile)
    LableEditView ledtModifyMobile;
    @InjectView(R.id.ledt_modifyEmail)
    LableEditView ledtModifyEmail;
    @InjectView(R.id.ledt_modifyAddress)
    LableEditView ledtModifyAddress;


    private String realName;
    private int REQUEST_CODE;
    private String mobile;
    private String email;
    private String address;

    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_userinfo);
        ButterKnife.inject(this);
        userInfo=getIntent().getParcelableExtra("userInfo");
        REQUEST_CODE=getIntent().getFlags();
        initView();
    }

    private void initView() {
        switch (REQUEST_CODE){
            case UserInfomationActivity.SEX:
                titleBar.initView(this,"修改性别");
                llModifySex.setVisibility(View.VISIBLE);
                switch (userInfo.getSex()) {
                    case 0:
                        setFemale();
                        break;
                    case 1:
                        setMale();
                        break;
                    case 2:
                        setSecret();
                        break;
                }
                break;
            case UserInfomationActivity.REAL_NAME:
                titleBar.initView(this,"修改姓名");
                ledtModifyName.setVisibility(View.VISIBLE);
                ledtModifyName.setEditText(userInfo.getName());
                break;
            case UserInfomationActivity.MOBILE:
                titleBar.initView(this,"修改手机号码");
                ledtModifyMobile.setVisibility(View.VISIBLE);
                ledtModifyMobile.setEditText(userInfo.getPhone());
                break;
            case UserInfomationActivity.EMAIL:
                titleBar.initView(this,"修改邮箱");
                ledtModifyEmail.setVisibility(View.VISIBLE);
                ledtModifyEmail.setEditText(userInfo.getEmail());
                break;
            case UserInfomationActivity.ADDRESS:
                titleBar.initView(this,"修改住宿地址");
                ledtModifyAddress.setVisibility(View.VISIBLE);
                ledtModifyAddress.setEditText(userInfo.getAddr());
                break;
        }
    }

    private void saveUserInfo() {
        if (REQUEST_CODE==UserInfomationActivity.ADDRESS){
            userInfo.setAddr(ledtModifyAddress.getEditText());
        }
        if (REQUEST_CODE==UserInfomationActivity.MOBILE){
            String mobile=ledtModifyMobile.getEditText();
            if (!RegexUtils.checkMobile(mobile)) {
                showShortToast("手机号码不正确！");
                return;
            }
            userInfo.setPhone(mobile);
        }
        if (REQUEST_CODE==UserInfomationActivity.EMAIL){
            String email=ledtModifyEmail.getEditText();
            if (!RegexUtils.checkEmail(email)){
                showShortToast("邮箱地址不正确！");
                return;
            }
            userInfo.setEmail(email);
        }
        if (REQUEST_CODE==UserInfomationActivity.REAL_NAME){
            String name=ledtModifyName.getEditText();
            if (!RegexUtils.checkChinese(name)){
                showShortToast("只能中文！");
                return;
            }
            if (name.length()<2||name.length()>6){
                showShortToast("2~6位中文！");
                return;
            }
            userInfo.setName(ledtModifyName.getEditText());
        }
        Intent intent1=new Intent();
        intent1.putExtra("userInfo", userInfo);
        setResult(RESULT_OK,intent1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        saveUserInfo();
        super.finish();

    }

    @OnClick({R.id.tv_male, R.id.tv_female, R.id.tv_secret})
    public void onClick(View view) {
        if (REQUEST_CODE==UserInfomationActivity.SEX) {
            switch (view.getId()) {
                case R.id.tv_male:
                    setMale();
                    userInfo.setSex(1);
                    break;
                case R.id.tv_female:
                    setFemale();
                    userInfo.setSex(0);
                    break;
                case R.id.tv_secret:
                    setSecret();
                    userInfo.setSex(2);
                    break;
            }
        }
        Intent intent = new Intent();
        intent.putExtra("userInfo", userInfo);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void setSecret() {
        ivMale.setVisibility(View.INVISIBLE);
        ivFemale.setVisibility(View.INVISIBLE);
        ivSecret.setVisibility(View.VISIBLE);
    }
    private void setMale() {
        ivFemale.setVisibility(View.INVISIBLE);
        ivSecret.setVisibility(View.INVISIBLE);
        ivMale.setVisibility(View.VISIBLE);
    }
    private void setFemale() {
        ivMale.setVisibility(View.INVISIBLE);
        ivSecret.setVisibility(View.INVISIBLE);
        ivFemale.setVisibility(View.VISIBLE);
    }

}
