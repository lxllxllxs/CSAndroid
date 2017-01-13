package com.yiyekeji.coolschool.bean;

import java.util.List;

/**
 * Created by lxl on 2017/1/9.
 */
public class StudentSign {
    private String userNum;
    private List<String> courseNo;//课程编号
    private String IMEI;
    private String ip;
    private String tokenId;

    public String getUserNum() {
        return userNum;
    }

    @Override
    public String toString() {
        return "StudentSign{" +
                "userNum='" + userNum + '\'' +
                ", courseNo=" + courseNo +
                ", IMEI='" + IMEI + '\'' +
                ", ip='" + ip + '\'' +
                ", tokenId='" + tokenId + '\'' +
                '}';
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public List<String> getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(List<String> courseNo) {
        this.courseNo = courseNo;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

/*    private float longitude;//经度
    private float latitude;//纬度*/


}
