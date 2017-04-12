package com.yiyekeji.coolschool;

import android.test.InstrumentationTestCase;

import com.yiyekeji.coolschool.dao.PullMsg;
import com.yiyekeji.coolschool.db.DbUtil;
import com.yiyekeji.coolschool.utils.LogUtil;

import java.util.List;

public class TestGreenDao extends InstrumentationTestCase {

    public void  testCheckUnRead(){
        boolean a= DbUtil.havaUnReadMsg();
        if (a) {
            LogUtil.d("hava");
            System.out.println("ahva");
        } else {
            LogUtil.d("none");
            System.out.println("none");
        }
    }

    public void  testGetAllMsg(){
        List<PullMsg> list= DbUtil.getAllPullMsg();
        if (list.isEmpty()) {
            LogUtil.d("none");
            System.out.println("none");
        } else {
            System.out.println("hava");
        }
    }
}