package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  //订单类型 0 打印 1 上门收件 2 代拿快递
 * Created by lxl on 2017/2/8.
 */
public class OtherOrder implements Parcelable {
    private String contactName;
    private String contactAddr;
    private String orderTime;
    private int orderType;
    private String remark;
    private String timeType;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    private String contactPhone;
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    private int orderState;
    private int mappingId;

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getMappingId() {
        return mappingId;
    }

    public void setMappingId(int mappingId) {
        this.mappingId = mappingId;
    }

    public OtherOrder() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contactName);
        dest.writeString(this.contactAddr);
        dest.writeString(this.orderTime);
        dest.writeInt(this.orderType);
        dest.writeString(this.remark);
        dest.writeString(this.timeType);
        dest.writeString(this.contactPhone);
        dest.writeInt(this.orderState);
        dest.writeInt(this.mappingId);
    }

    protected OtherOrder(Parcel in) {
        this.contactName = in.readString();
        this.contactAddr = in.readString();
        this.orderTime = in.readString();
        this.orderType = in.readInt();
        this.remark = in.readString();
        this.timeType = in.readString();
        this.contactPhone = in.readString();
        this.orderState = in.readInt();
        this.mappingId = in.readInt();
    }

    public static final Creator<OtherOrder> CREATOR = new Creator<OtherOrder>() {
        @Override
        public OtherOrder createFromParcel(Parcel source) {
            return new OtherOrder(source);
        }

        @Override
        public OtherOrder[] newArray(int size) {
            return new OtherOrder[size];
        }
    };
}
