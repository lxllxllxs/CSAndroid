package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yiyekeji.coolschool.inter.HaveName;

/**
 * Created by lxl on 2017/1/20.
 */
public class ProductModel implements Parcelable,HaveName {
    private String pmTitle="默认";
    private int pmBalance=0;

    public int getPmId() {
        return pmId;
    }

    public void setPmId(int pmId) {
        this.pmId = pmId;
    }

    private String pmPrice="0";
    private int  pmId;
    private boolean isSelect;

    @Override
    public String toString() {
        return "ProductModel{" +
                "pmTitle='" + pmTitle + '\'' +
                ", pmBalance=" + pmBalance +
                ", pmPrice='" + pmPrice + '\'' +
                ", pmId=" + pmId +
                ", isSelect=" + isSelect +
                '}';
    }

    public String getPmTitle() {
        return pmTitle;
    }

    public void setPmTitle(String pmTitle) {
        this.pmTitle = pmTitle;
    }

    public int getPmBalance() {
        return pmBalance;
    }

    public void setPmBalance(int pmBalance) {
        this.pmBalance = pmBalance;
    }

    public String getPmPrice() {
        return pmPrice;
    }

    public void setPmPrice(String pmPrice) {
        this.pmPrice = pmPrice;
    }

    public ProductModel() {
    }

    @Override
    public String getName() {
        return pmTitle;
    }


    @Override
    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public void setSelect(boolean b) {
        this.isSelect = b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pmTitle);
        dest.writeInt(this.pmBalance);
        dest.writeString(this.pmPrice);
        dest.writeInt(this.pmId);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected ProductModel(Parcel in) {
        this.pmTitle = in.readString();
        this.pmBalance = in.readInt();
        this.pmPrice = in.readString();
        this.pmId = in.readInt();
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel source) {
            return new ProductModel(source);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };
}
