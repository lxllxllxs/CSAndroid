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
    private  String supplierNum;

    private Integer pcId;
    public Integer getpcId() {
        return pcId;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "pImage='" + pImage + '\'' +
                ", pTitle='" + pTitle + '\'' +
                ", supplierNum='" + supplierNum + '\'' +
                ", pcId=" + pcId +
                ", pUnit='" + pUnit + '\'' +
                ", pState=" + pState +
                ", pDescrition='" + pDescrition + '\'' +
                ", pId=" + pId +
                ", pPrice=" + pPrice +
                ", pSalenum=" + pSalenum +
                ", modelList=" + modelList +
                ", pictureList=" + pictureList +
                '}';
    }

    public void setpcId(Integer pcId) {
        this.pcId = pcId;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    //自加字段
    private  String pUnit;

    public int getpState() {
        return pState;
    }

    public void setpState(int pState) {
        this.pState = pState;
    }

    private int pState;
    public String getpDescrition() {
        return pDescrition;
    }

    public void setpDescrition(String pDescrition) {
        this.pDescrition = pDescrition;
    }

    private String pDescrition;

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
        return pId;
    }

    public void setPid(int pId) {
        this.pId=pId;
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

    private int pId;
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
        dest.writeString(this.supplierNum);
        dest.writeValue(this.pcId);
        dest.writeString(this.pUnit);
        dest.writeInt(this.pState);
        dest.writeString(this.pDescrition);
        dest.writeInt(this.pId);
        dest.writeDouble(this.pPrice);
        dest.writeInt(this.pSalenum);
        dest.writeTypedList(this.modelList);
        dest.writeStringList(this.pictureList);
    }

    protected ProductInfo(Parcel in) {
        this.pImage = in.readString();
        this.pTitle = in.readString();
        this.supplierNum = in.readString();
        this.pcId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pUnit = in.readString();
        this.pState = in.readInt();
        this.pDescrition = in.readString();
        this.pId = in.readInt();
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
