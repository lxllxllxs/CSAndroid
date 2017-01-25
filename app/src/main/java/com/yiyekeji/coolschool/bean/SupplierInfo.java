package com.yiyekeji.coolschool.bean;

/**
 * Created by lxl on 2017/1/23.
 */
public class SupplierInfo  {
    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getsRegTime() {
        return sRegTime;
    }

    public void setsRegTime(String sRegTime) {
        this.sRegTime = sRegTime;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    private String sPhone;
    private String sRegTime;
    private String sName;

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    private String userNum;
}
