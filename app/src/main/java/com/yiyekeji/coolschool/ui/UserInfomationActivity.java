package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

    final int REAL_NAME = 0;
    final int SEX = 1;
    final int ADDRESS = 2;
    final int EMAIL = 3;
    final int MOBILE =4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infomation);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    private void initData() {
        useInfo = App.userInfo;
    }

    private void initView() {
        titleBar.initView(this);

        tvUserNo.setText(useInfo.getUserNum());
        tvUserType.setText(useInfo.getRoleType()==0?"学生":"老师");
        tvRealName.setText(useInfo.getName());

        tvSex.setText(useInfo.getSex()==1?"男":"女");
        tvMobile.setText(useInfo.getPhone());
        tvAddress.setText(useInfo.getAddr());
        tvEmail.setText(useInfo.getEmail());
    }


    @OnClick({R.id.ll_realName, R.id.ll_sex, R.id.ll_mobile, R.id.ll_email, R.id.ll_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_realName:
                Intent intent0 = new Intent(this, ModifyRealNameActivity.class);
                startActivityForResult(intent0,REAL_NAME);
                break;
            case R.id.ll_sex:
                Intent intent = new Intent(this, ModifySexActivity.class);
                intent.putExtra("sex", useInfo.getSex());
                startActivityForResult(intent,SEX);
                break;
            case R.id.ll_mobile:
                Intent intent1 = new Intent(this, ModifyMobileActivity.class);

                startActivityForResult(intent1,MOBILE);
                break;
            case R.id.ll_email:
                Intent intent2 = new Intent(this, ModifyEmailActivity.class);
                startActivityForResult(intent2,EMAIL);
                break;
            case R.id.ll_address:
                Intent intent3 = new Intent(this, ModifyAddressActivity.class);
                startActivityForResult(intent3,ADDRESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REAL_NAME:
                useInfo.setName(data.getStringExtra("realName"));
                break;
            case SEX:
                useInfo.setSex(data.getIntExtra("sex",2));
                break;
            case ADDRESS:
                useInfo.setAddr(data.getStringExtra("address"));
                break;
            case MOBILE:
                useInfo.setPhone(data.getStringExtra("mobile"));
                break;
            case EMAIL:
                useInfo.setEmail(data.getStringExtra("email"));
                break;
        }
        initView();

    }
}
