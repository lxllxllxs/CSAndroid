package com.yiyekeji.coolschool.bean;

import com.yiyekeji.coolschool.ui.base.BaseActivity;

/**
 * Created by lxl on 2017/1/7.
 */
public class MainMenu {

    public MainMenu(){

    }

    public MainMenu(String name,int resId,Class<?extends BaseActivity> targetActivity){
        this.name = name;
        this.resId = resId;
        this.targetActivity = targetActivity;
    }
    private Class<?extends BaseActivity> targetActivity;

    public Class<?extends BaseActivity> getTargetActivity() {
        return targetActivity;
    }

    public MainMenu setTargetActivity(Class<?extends BaseActivity> targetActivity) {
        this.targetActivity = targetActivity;
        return this;
    }

    public String getName() {
        return name;
    }

    public MainMenu setName(String name) {
        this.name = name;
        return this;
    }

    public int getResId() {
        return resId;
    }

    public MainMenu setResId(int resId) {
        this.resId = resId;
        return this;
    }

    private String name;
    private int    resId;
}
