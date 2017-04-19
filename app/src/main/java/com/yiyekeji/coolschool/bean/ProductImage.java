package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/4/19.
 */
public class ProductImage implements Parcelable {
    private String imgUrl;
    private int pId;
    private int id;
    //排序id
    private int sortId;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    private String userNo;

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "imgUrl='" + imgUrl + '\'' +
                ", pId=" + pId +
                ", id=" + id +
                ", sortId=" + sortId +
                ", userNo='" + userNo + '\'' +
                '}';
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeInt(this.pId);
        dest.writeInt(this.id);
        dest.writeInt(this.sortId);
        dest.writeString(this.userNo);
    }

    public ProductImage() {
    }

    protected ProductImage(Parcel in) {
        this.imgUrl = in.readString();
        this.pId = in.readInt();
        this.id = in.readInt();
        this.sortId = in.readInt();
        this.userNo = in.readString();
    }

    public static final Parcelable.Creator<ProductImage> CREATOR = new Parcelable.Creator<ProductImage>() {
        @Override
        public ProductImage createFromParcel(Parcel source) {
            return new ProductImage(source);
        }

        @Override
        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };
}
