package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/24.
 */
public class ProductOrderItem implements Parcelable {

    //自加字段 单价
    private String price;
    //自加字段 名称
    private String mTitle;
    //自加字段 产品名称
    private String productName;
    //自加字段 图片地址
    private String imgPath;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    //自加字段 计量单位
    private String unit;


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    private int pmCount;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    private double subTotal;

    public String getPrice() {
        return price;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    private int pId;
    public int getPmCount() {
        return pmCount;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "ProductOrderItem{" +
                "pmCount=" + pmCount +
                ", subTotal=" + subTotal +
                ", pmId=" + pmId +
                ", message='" + message + '\'' +
                '}';
    }

    public void setPmCount(int pmCount) {
        this.pmCount = pmCount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getPmId() {
        return pmId;
    }

    public void setPmId(int pmId) {
        this.pmId = pmId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int pmId;
    private String message;

    public ProductOrderItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeString(this.mTitle);
        dest.writeString(this.productName);
        dest.writeString(this.imgPath);
        dest.writeString(this.unit);
        dest.writeInt(this.pmCount);
        dest.writeDouble(this.subTotal);
        dest.writeInt(this.pId);
        dest.writeInt(this.pmId);
        dest.writeString(this.message);
    }

    protected ProductOrderItem(Parcel in) {
        this.price = in.readString();
        this.mTitle = in.readString();
        this.productName = in.readString();
        this.imgPath = in.readString();
        this.unit = in.readString();
        this.pmCount = in.readInt();
        this.subTotal = in.readDouble();
        this.pId = in.readInt();
        this.pmId = in.readInt();
        this.message = in.readString();
    }

    public static final Creator<ProductOrderItem> CREATOR = new Creator<ProductOrderItem>() {
        @Override
        public ProductOrderItem createFromParcel(Parcel source) {
            return new ProductOrderItem(source);
        }

        @Override
        public ProductOrderItem[] newArray(int size) {
            return new ProductOrderItem[size];
        }
    };
}
