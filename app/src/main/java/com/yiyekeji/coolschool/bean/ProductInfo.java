package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lxl on 2017/1/21.
 */
public class ProductInfo implements Parcelable {
    private  String pImage;
    private  String pTitle;
    private  String pUnit;

    @Override
    public String toString() {
        return "ProductInfo{" +
                "pImage='" + pImage + '\'' +
                ", pTitle='" + pTitle + '\'' +
                ", pUnit='" + pUnit + '\'' +
                ", pid=" + pid +
                ", pPrice=" + pPrice +
                ", pSalenum=" + pSalenum +
                ", modelList=" + modelList +
                ", pictureList=" + pictureList +
                '}';
    }

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
    private ArrayList<ProductModel> modelList;

    public void setpPrice(double pPrice) {
        this.pPrice = pPrice;
    }

    public ArrayList<ProductModel> getModelList() {
        return modelList;
    }

    public void setModelList(ArrayList<ProductModel> modelList) {
        this.modelList = modelList;
    }

    public ArrayList<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(ArrayList<String> pictureList) {
        this.pictureList = pictureList;
    }

    private ArrayList<String> pictureList;

    public ProductInfo() {
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
        dest.writeTypedList(this.modelList);
        dest.writeStringList(this.pictureList);
    }

    protected ProductInfo(Parcel in) {
        this.pImage = in.readString();
        this.pTitle = in.readString();
        this.pUnit = in.readString();
        this.pid = in.readInt();
        this.pPrice = in.readDouble();
        this.pSalenum = in.readInt();
        this.modelList = in.createTypedArrayList(ProductModel.CREATOR);
        this.pictureList = in.createStringArrayList();
    }

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel source) {
            return new ProductInfo(source);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };
}
