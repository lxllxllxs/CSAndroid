package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/26.
 */
public class ProductOrder implements Parcelable {
    private int poId;
    private int poState;
    private String pTitle;
    private String orderTime;

    private String receivePhone;
    private String pUnit;
    private String timeType;
    private String pmTitle;
    private String pImage;

    private String message;
    private String receiveName;
    private String receiveAddr;

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getpUnit() {
        return pUnit;
    }

    public void setpUnit(String pUnit) {
        this.pUnit = pUnit;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getPmTitle() {
        return pmTitle;
    }

    public void setPmTitle(String pmTitle) {
        this.pmTitle = pmTitle;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public int getPmCount() {
        return pmCount;
    }

    public void setPmCount(int pmCount) {
        this.pmCount = pmCount;
    }



    public double getPmPrice() {
        return pmPrice;
    }

    public void setPmPrice(double pmPrice) {
        this.pmPrice = pmPrice;
    }

    private int pmCount;

    private double pmPrice;


    public int getPoId() {
        return poId;
    }

    public void setPoId(int poId) {
        this.poId = poId;
    }

    public int getPoState() {
        return poState;
    }

    public void setPoState(int poState) {
        this.poState = poState;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public ProductOrder() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.poId);
        dest.writeInt(this.poState);
        dest.writeString(this.pTitle);
        dest.writeString(this.orderTime);
        dest.writeString(this.receivePhone);
        dest.writeString(this.pUnit);
        dest.writeString(this.timeType);
        dest.writeString(this.pmTitle);
        dest.writeString(this.pImage);
        dest.writeString(this.message);
        dest.writeString(this.receiveName);
        dest.writeString(this.receiveAddr);
        dest.writeInt(this.pmCount);
        dest.writeDouble(this.pmPrice);
    }

    protected ProductOrder(Parcel in) {
        this.poId = in.readInt();
        this.poState = in.readInt();
        this.pTitle = in.readString();
        this.orderTime = in.readString();
        this.receivePhone = in.readString();
        this.pUnit = in.readString();
        this.timeType = in.readString();
        this.pmTitle = in.readString();
        this.pImage = in.readString();
        this.message = in.readString();
        this.receiveName = in.readString();
        this.receiveAddr = in.readString();
        this.pmCount = in.readInt();
        this.pmPrice = in.readDouble();
    }

    public static final Creator<ProductOrder> CREATOR = new Creator<ProductOrder>() {
        @Override
        public ProductOrder createFromParcel(Parcel source) {
            return new ProductOrder(source);
        }

        @Override
        public ProductOrder[] newArray(int size) {
            return new ProductOrder[size];
        }
    };
}
