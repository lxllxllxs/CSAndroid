package com.yiyekeji.coolschool.bean;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
public class StudentInfo implements Serializable, Parcelable {
    private String userID = "";
    private String userName = "";
    private String userSex = "";
    private String userProfession = "";
    private String userClass = "";
    private String userCollege = "";
    private String userStatus = "";

    @Override
    public String toString() {
        return "StudentInfo [userID=" + userID + ", userName=" + userName
                + ", userSex=" + userSex + ", userProfession=" + userProfession
                + ", userClass=" + userClass + ", userCollege=" + userCollege
                + ", userStatus=" + userStatus + "]";
    }

    public StudentInfo() {
    }

    public StudentInfo(String[] stu) {
        if (stu != null) {
            //stu[0] = "学生基础信息"，所以不取用
            this.userID = stu[1];
            this.userName = stu[2];
            this.userSex = stu[3];
            this.userProfession = stu[4];
            this.userClass = stu[5];
            this.userCollege = stu[6];
            this.userStatus = stu[7];
        }
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserProfession() {
        return userProfession;
    }

    public void setUserProfession(String userProfession) {
        this.userProfession = userProfession;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserCollege() {
        return userCollege;
    }

    public void setUserCollege(String userCollege) {
        this.userCollege = userCollege;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userID);
        dest.writeString(this.userName);
        dest.writeString(this.userSex);
        dest.writeString(this.userProfession);
        dest.writeString(this.userClass);
        dest.writeString(this.userCollege);
        dest.writeString(this.userStatus);
    }

    protected StudentInfo(Parcel in) {
        this.userID = in.readString();
        this.userName = in.readString();
        this.userSex = in.readString();
        this.userProfession = in.readString();
        this.userClass = in.readString();
        this.userCollege = in.readString();
        this.userStatus = in.readString();
    }

    public static final Parcelable.Creator<StudentInfo> CREATOR = new Parcelable.Creator<StudentInfo>() {
        @Override
        public StudentInfo createFromParcel(Parcel source) {
            return new StudentInfo(source);
        }

        @Override
        public StudentInfo[] newArray(int size) {
            return new StudentInfo[size];
        }
    };
}
