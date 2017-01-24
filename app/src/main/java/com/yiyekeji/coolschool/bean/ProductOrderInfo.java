package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lxl on 2017/1/24.
 */
public class ProductOrderInfo implements Parcelable {

    private String tokenId;
    private double sum;
    private String receiveAddr;
    private String receiveName;
    private String receivePhone;

    public String getTokenId() {
        return tokenId;
    }

    @Override
    public String toString() {
        return "ProductOrderInfo{" +
                "tokenId='" + tokenId + '\'' +
                ", sum=" + sum +
                ", receiveAddr='" + receiveAddr + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", receivePhone='" + receivePhone + '\'' +
                ", userNum='" + userNum + '\'' +
                ", timeType=" + timeType +
                ", productOrderItems=" + productOrderItems +
                '}';
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public double getSum() {
        return sum;

    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public ArrayList<ProductOrderItem> getProductOrderItems() {
        return productOrderItems;
    }

    public void setProductOrderItems(ArrayList<ProductOrderItem> productOrderItems) {
        this.productOrderItems = productOrderItems;
    }

    private String userNum;
    private int timeType;
    private ArrayList<ProductOrderItem> productOrderItems;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenId);
        dest.writeDouble(this.sum);
        dest.writeString(this.receiveAddr);
        dest.writeString(this.receiveName);
        dest.writeString(this.receivePhone);
        dest.writeString(this.userNum);
        dest.writeInt(this.timeType);
        dest.writeTypedList(this.productOrderItems);
    }

    public ProductOrderInfo() {
    }

    protected ProductOrderInfo(Parcel in) {
        this.tokenId = in.readString();
        this.sum = in.readDouble();
        this.receiveAddr = in.readString();
        this.receiveName = in.readString();
        this.receivePhone = in.readString();
        this.userNum = in.readString();
        this.timeType = in.readInt();
        this.productOrderItems = in.createTypedArrayList(ProductOrderItem.CREATOR);
    }

    public static final Parcelable.Creator<ProductOrderInfo> CREATOR = new Parcelable.Creator<ProductOrderInfo>() {
        @Override
        public ProductOrderInfo createFromParcel(Parcel source) {
            return new ProductOrderInfo(source);
        }

        @Override
        public ProductOrderInfo[] newArray(int size) {
            return new ProductOrderInfo[size];
        }
    };
}
