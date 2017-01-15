package com.yiyekeji.coolschool.bean;

import java.util.List;

/**
 * Created by lxl on 2017/1/9.
 */
public class StudentSign {
    private String userNum;
    private String realName;
    private String ip;
    private String imei;
    private String tokenId;
    private List<String> courseNo;//课程编号
    public String getTokenId() {
        return tokenId;
    }

    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userNum='" + userNum + '\'' +
                ", realName='" + realName + '\'' +
                ", ip='" + ip + '\'' +
                ", imei='" + imei + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", courseNo=" + courseNo +
                ", isSignIn=" + isSignIn +
                '}';
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public List<String> getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(List<String> courseNo) {
        this.courseNo = courseNo;
    }

    private int isSignIn = 0;
    public String getUserNum() {
        return userNum;
    }
    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }
    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getImei() {
        return imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }
    public int getIsSignIn() {
        return isSignIn;
    }
    public void setIsSignIn(int isSignIn) {
        this.isSignIn = isSignIn;
    }
}
