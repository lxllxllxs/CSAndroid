package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;

/**
 * Created by lxl on 2017/1/7.
 */
public class UserInfomationActivity extends BaseActivity {


    final int REAL_NAME=0;
    final int SEX=1;
    final int ADDRESS=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infomation);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REAL_NAME:
                break;
            case SEX:
                break;
            case ADDRESS:
                break;

        }

    }
}
