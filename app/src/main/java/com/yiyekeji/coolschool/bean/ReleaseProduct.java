package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lxl on 2017/1/20.
 */
public class ReleaseProduct implements Parcelable {

    private String tokenId;
    private String pTitle;
    private String pDescrition;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    private String supplierNum;
    private String pUnit;
    private int categoryId;

    private ArrayList<Integer> pictureIdList;
    private ArrayList<ProductModel> modelList;

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescrition() {
        return pDescrition;
    }

    public void setpDescrition(String pDescrition) {
        this.pDescrition = pDescrition;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public String getpUnit() {
        return pUnit;
    }

    public void setpUnit(String pUnit) {
        this.pUnit = pUnit;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public ArrayList<Integer> getPictureIdList() {
        return pictureIdList;
    }

    public void setPictureIdList(ArrayList<Integer> pictureIdList) {
        this.pictureIdList = pictureIdList;
    }

    public ArrayList<ProductModel> getModelList() {
        return modelList;
    }

    public void setModelList(ArrayList<ProductModel> modelList) {
        this.modelList = modelList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenId);
        dest.writeString(this.pTitle);
        dest.writeString(this.pDescrition);
        dest.writeString(this.supplierNum);
        dest.writeString(this.pUnit);
        dest.writeInt(this.categoryId);
        dest.writeList(this.pictureIdList);
        dest.writeTypedList(this.modelList);
    }

    public ReleaseProduct() {
    }

    protected ReleaseProduct(Parcel in) {
        this.tokenId = in.readString();
        this.pTitle = in.readString();
        this.pDescrition = in.readString();
        this.supplierNum = in.readString();
        this.pUnit = in.readString();
        this.categoryId = in.readInt();
        this.pictureIdList = new ArrayList<Integer>();
        in.readList(this.pictureIdList, Integer.class.getClassLoader());
        this.modelList = in.createTypedArrayList(ProductModel.CREATOR);
    }

    public static final Parcelable.Creator<ReleaseProduct> CREATOR = new Parcelable.Creator<ReleaseProduct>() {
        @Override
        public ReleaseProduct createFromParcel(Parcel source) {
            return new ReleaseProduct(source);
        }

        @Override
        public ReleaseProduct[] newArray(int size) {
            return new ReleaseProduct[size];
        }
    };
}