package com.yiyekeji.coolschool.test;

import android.os.Bundle;

import com.yiyekeji.coolschool.api.SubSystemApi;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.ThreadPools;

/**
 * Created by lxl on 2017/2/12.
 */
public class TestStuSystemAty extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        final SubSystemApi api=new SubSystemApi();
        ThreadPools.getInstance().addRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                  int result=  api.login("3112002665","1501981");
                    LogUtil.d("SubSystemApi:",result+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
