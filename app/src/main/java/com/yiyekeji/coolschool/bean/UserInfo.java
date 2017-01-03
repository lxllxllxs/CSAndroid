package com.yiyekeji.coolschool.bean;

/**
 * Created by lxl on 2017/1/3.
 */
public class UserInfo {
    private String name;
    private String pwd;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
