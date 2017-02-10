package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lxl on 2017/1/24.
 */
public class CreateProductOrderInfo implements Parcelable {

    private String tokenId;
    private double sum;
    private String receiveAddr;
    private String receiveName;
    private String receivePhone;

    public ArrayList<Integer> getCartItemIdList() {
        return cartItemIdList;
    }

    public void setCartItemIdList(ArrayList<Integer> cartItemIdList) {
        this.cartItemIdList = cartItemIdList;
    }

    public ArrayList<ProductOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(ArrayList<ProductOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    private ArrayList<Integer> cartItemIdList;
    public String getTokenId() {
        return tokenId;
    }

    @Override
    public String toString() {
        return "CreateProductOrderInfo{" +
                "tokenId='" + tokenId + '\'' +
                ", sum=" + sum +
                ", receiveAddr='" + receiveAddr + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", receivePhone='" + receivePhone + '\'' +
                ", userNum='" + userNum + '\'' +
                ", timeType=" + timeType +
                ", productOrderItems=" + orderItemList +
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
        return orderItemList;
    }

    public void setProductOrderItems(ArrayList<ProductOrderItem> productOrderItems) {
        this.orderItemList = productOrderItems;
    }

    private String userNum;
    private int timeType;
    private ArrayList<ProductOrderItem> orderItemList;

    public CreateProductOrderInfo() {
    }

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
        dest.writeList(this.cartItemIdList);
        dest.writeString(this.userNum);
        dest.writeInt(this.timeType);
        dest.writeTypedList(this.orderItemList);
    }

    protected CreateProductOrderInfo(Parcel in) {
        this.tokenId = in.readString();
        this.sum = in.readDouble();
        this.receiveAddr = in.readString();
        this.receiveName = in.readString();
        this.receivePhone = in.readString();
        this.cartItemIdList = new ArrayList<Integer>();
        in.readList(this.cartItemIdList, Integer.class.getClassLoader());
        this.userNum = in.readString();
        this.timeType = in.readInt();
        this.orderItemList = in.createTypedArrayList(ProductOrderItem.CREATOR);
    }

    public static final Creator<CreateProductOrderInfo> CREATOR = new Creator<CreateProductOrderInfo>() {
        @Override
        public CreateProductOrderInfo createFromParcel(Parcel source) {
            return new CreateProductOrderInfo(source);
        }

        @Override
        public CreateProductOrderInfo[] newArray(int size) {
            return new CreateProductOrderInfo[size];
        }
    };
}
