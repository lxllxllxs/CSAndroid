package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/3.
 */
public class UserInfo implements Cloneable,Parcelable {
    private String name;
    private String password;
    private String userNum;
    private int roleType;

    public String getPswAnswer() {
        return pswAnswer;
    }

    public void setPswAnswer(String pswAnswer) {
        this.pswAnswer = pswAnswer;
    }

    private String  pswAnswer;


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

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (roleType != userInfo.roleType) return false;
        if (id != userInfo.id) return false;
        if (isSupplier != userInfo.isSupplier) return false;
        if (sex != userInfo.sex) return false;
        if (name != null ? !name.equals(userInfo.name) : userInfo.name != null) return false;
        if (password != null ? !password.equals(userInfo.password) : userInfo.password != null)
            return false;
        if (userNum != null ? !userNum.equals(userInfo.userNum) : userInfo.userNum != null)
            return false;
        if (tokenId != null ? !tokenId.equals(userInfo.tokenId) : userInfo.tokenId != null)
            return false;
        if (headImg != null ? !headImg.equals(userInfo.headImg) : userInfo.headImg != null)
            return false;
        if (aBalance != null ? !aBalance.equals(userInfo.aBalance) : userInfo.aBalance != null)
            return false;
        if (imei != null ? !imei.equals(userInfo.imei) : userInfo.imei != null) return false;
        if (email != null ? !email.equals(userInfo.email) : userInfo.email != null) return false;
        if (addr != null ? !addr.equals(userInfo.addr) : userInfo.addr != null) return false;
        if (lastTime != null ? !lastTime.equals(userInfo.lastTime) : userInfo.lastTime != null)
            return false;
        if (wBalance != null ? !wBalance.equals(userInfo.wBalance) : userInfo.wBalance != null)
            return false;
        if (phone != null ? !phone.equals(userInfo.phone) : userInfo.phone != null) return false;
        return regTime != null ? regTime.equals(userInfo.regTime) : userInfo.regTime == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (userNum != null ? userNum.hashCode() : 0);
        result = 31 * result + roleType;
        result = 31 * result + id;
        result = 31 * result + (tokenId != null ? tokenId.hashCode() : 0);
        result = 31 * result + (headImg != null ? headImg.hashCode() : 0);
        result = 31 * result + (aBalance != null ? aBalance.hashCode() : 0);
        result = 31 * result + (imei != null ? imei.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (addr != null ? addr.hashCode() : 0);
        result = 31 * result + (lastTime != null ? lastTime.hashCode() : 0);
        result = 31 * result + (wBalance != null ? wBalance.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + isSupplier;
        result = 31 * result + sex;
        result = 31 * result + (regTime != null ? regTime.hashCode() : 0);
        return result;
    }

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


    public UserInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.password);
        dest.writeString(this.userNum);
        dest.writeInt(this.roleType);
        dest.writeString(this.pswAnswer);
        dest.writeInt(this.id);
        dest.writeString(this.tokenId);
        dest.writeString(this.headImg);
        dest.writeString(this.aBalance);
        dest.writeString(this.imei);
        dest.writeString(this.email);
        dest.writeString(this.addr);
        dest.writeString(this.lastTime);
        dest.writeString(this.wBalance);
        dest.writeString(this.phone);
        dest.writeInt(this.isSupplier);
        dest.writeInt(this.sex);
        dest.writeString(this.regTime);
    }

    protected UserInfo(Parcel in) {
        this.name = in.readString();
        this.password = in.readString();
        this.userNum = in.readString();
        this.roleType = in.readInt();
        this.pswAnswer = in.readString();
        this.id = in.readInt();
        this.tokenId = in.readString();
        this.headImg = in.readString();
        this.aBalance = in.readString();
        this.imei = in.readString();
        this.email = in.readString();
        this.addr = in.readString();
        this.lastTime = in.readString();
        this.wBalance = in.readString();
        this.phone = in.readString();
        this.isSupplier = in.readInt();
        this.sex = in.readInt();
        this.regTime = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
