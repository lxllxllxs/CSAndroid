package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/2/6.
 */
public class CreatePrintOrder implements Parcelable {
    private String tokenId;
    private String receiveAddr;
    private String receivePhone;
    private String receiveName;
    private String userNum;
    private int fileId;
    private int timeType;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenId);
        dest.writeString(this.receiveAddr);
        dest.writeString(this.receivePhone);
        dest.writeString(this.receiveName);
        dest.writeString(this.userNum);
        dest.writeInt(this.fileId);
        dest.writeInt(this.timeType);
    }

    public CreatePrintOrder() {
    }

    protected CreatePrintOrder(Parcel in) {
        this.tokenId = in.readString();
        this.receiveAddr = in.readString();
        this.receivePhone = in.readString();
        this.receiveName = in.readString();
        this.userNum = in.readString();
        this.fileId = in.readInt();
        this.timeType = in.readInt();
    }

    public static final Parcelable.Creator<CreatePrintOrder> CREATOR = new Parcelable.Creator<CreatePrintOrder>() {
        @Override
        public CreatePrintOrder createFromParcel(Parcel source) {
            return new CreatePrintOrder(source);
        }

        @Override
        public CreatePrintOrder[] newArray(int size) {
            return new CreatePrintOrder[size];
        }
    };
}
