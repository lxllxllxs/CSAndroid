package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/21.
 */
public class Product implements Parcelable {
    private  String pImage;
    private  String pTitle;
    private  String pUnit;

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpUnit() {
        return pUnit;
    }

    public void setpUnit(String pUnit) {
        this.pUnit = pUnit;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public double getpPrice() {
        return pPrice;
    }

    public void setpPrice(float pPrice) {
        this.pPrice = pPrice;
    }

    public int getpSalenum() {
        return pSalenum;
    }

    public void setpSalenum(int pSalenum) {
        this.pSalenum = pSalenum;
    }

    private int pid;
    private double pPrice;
    private int pSalenum;

    public Product() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pImage);
        dest.writeString(this.pTitle);
        dest.writeString(this.pUnit);
        dest.writeInt(this.pid);
        dest.writeDouble(this.pPrice);
        dest.writeInt(this.pSalenum);
    }

    protected Product(Parcel in) {
        this.pImage = in.readString();
        this.pTitle = in.readString();
        this.pUnit = in.readString();
        this.pid = in.readInt();
        this.pPrice = in.readDouble();
        this.pSalenum = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
