package com.yiyekeji.coolschool.bean;

/**
 * Created by Administrator on 2017/4/14/014.
 */
public class CancelOrder {
    private long id;
    private String orderId;
    private String status;
    private String reason;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "CancelOrder{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", createDate='" + createDate + '\'' +
                ", userNo='" + userNo + '\'' +
                '}';
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

}