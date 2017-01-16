package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/16.
 */
public class CutClassNumInfo implements Parcelable {
    private String userNum;
    private String realName;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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

    private int count;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userNum);
        dest.writeString(this.realName);
        dest.writeInt(this.count);
    }

    public CutClassNumInfo() {
    }

    protected CutClassNumInfo(Parcel in) {
        this.userNum = in.readString();
        this.realName = in.readString();
        this.count = in.readInt();
    }

    public static final Parcelable.Creator<CutClassNumInfo> CREATOR = new Parcelable.Creator<CutClassNumInfo>() {
        @Override
        public CutClassNumInfo createFromParcel(Parcel source) {
            return new CutClassNumInfo(source);
        }

        @Override
        public CutClassNumInfo[] newArray(int size) {
            return new CutClassNumInfo[size];
        }
    };
}
