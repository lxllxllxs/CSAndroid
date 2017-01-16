package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/16.
 */
public class ClassAbsenceInfo implements Parcelable {
   private int cutClassId;
    private String userNum;
    private String realName;

    public int getCutClassId() {
        return cutClassId;
    }

    public void setCutClassId(int cutClassId) {
        this.cutClassId = cutClassId;
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

    public ClassAbsenceInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cutClassId);
        dest.writeString(this.userNum);
        dest.writeString(this.realName);
    }

    protected ClassAbsenceInfo(Parcel in) {
        this.cutClassId = in.readInt();
        this.userNum = in.readString();
        this.realName = in.readString();
    }

    public static final Creator<ClassAbsenceInfo> CREATOR = new Creator<ClassAbsenceInfo>() {
        @Override
        public ClassAbsenceInfo createFromParcel(Parcel source) {
            return new ClassAbsenceInfo(source);
        }

        @Override
        public ClassAbsenceInfo[] newArray(int size) {
            return new ClassAbsenceInfo[size];
        }
    };
}
