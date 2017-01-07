package com.yiyekeji.coolschool.bean;

/**
 * Created by lxl on 2017/1/3.
 */
public class UserInfo  {
    private String name;
    private String password;
    private String userNum;
    private int roleType;
    private int id;
    private String tokenId;
    private String headImg;
    private String aBalance;
    private String imei;
    private String email;
    private String addr;
    private String lastTime;
    private String wBalance;
    private String phone;

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", userNum='" + userNum + '\'' +
                ", roleType=" + roleType +
                ", id=" + id +
                ", tokenId='" + tokenId + '\'' +
                ", headImg='" + headImg + '\'' +
                ", aBalance='" + aBalance + '\'' +
                ", imei='" + imei + '\'' +
                ", email='" + email + '\'' +
                ", addr='" + addr + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", wBalance='" + wBalance + '\'' +
                ", phone='" + phone + '\'' +
                ", isSupplier='" + isSupplier + '\'' +
                ", sex='" + sex + '\'' +
                ", regTime='" + regTime + '\'' +
                '}';
    }

    private int isSupplier;
    private int sex;

    public void setIsSupplier(int isSupplier) {
        this.isSupplier = isSupplier;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIsSupplier() {
        return isSupplier;
    }

    public int getSex() {
        return sex;
    }

    private String regTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getaBalance() {
        return aBalance;
    }

    public void setaBalance(String aBalance) {
        this.aBalance = aBalance;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getwBalance() {
        return wBalance;
    }

    public void setwBalance(String wBalance) {
        this.wBalance = wBalance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }



}
