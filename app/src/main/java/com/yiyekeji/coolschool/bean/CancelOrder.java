package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/14/014.
 */
public class CancelOrder implements Parcelable {
    private long id;
    private String orderId;
    private String status;
    private String reason;

    //订单自身消息
    private String sum;
    private String address;
    private String realName;
    private String phone;

    public String getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "CancelOrder{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", sum='" + sum + '\'' +
                ", address='" + address + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", createDate='" + createDate + '\'' +
                ", userNo='" + userNo + '\'' +
                '}';
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    private String createDate;
    private String userNo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.orderId);
        dest.writeString(this.status);
        dest.writeString(this.reason);
        dest.writeString(this.sum);
        dest.writeString(this.address);
        dest.writeString(this.realName);
        dest.writeString(this.phone);
        dest.writeString(this.createDate);
        dest.writeString(this.userNo);
    }

    public CancelOrder() {
    }

    protected CancelOrder(Parcel in) {
        this.id = in.readLong();
        this.orderId = in.readString();
        this.status = in.readString();
        this.reason = in.readString();
        this.sum = in.readString();
        this.address = in.readString();
        this.realName = in.readString();
        this.phone = in.readString();
        this.createDate = in.readString();
        this.userNo = in.readString();
    }

    public static final Parcelable.Creator<CancelOrder> CREATOR = new Parcelable.Creator<CancelOrder>() {
        @Override
        public CancelOrder createFromParcel(Parcel source) {
            return new CancelOrder(source);
        }

        @Override
        public CancelOrder[] newArray(int size) {
            return new CancelOrder[size];
        }
    };
}