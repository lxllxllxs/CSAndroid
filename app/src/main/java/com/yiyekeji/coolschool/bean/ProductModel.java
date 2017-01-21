package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/20.
 */
public class ProductModel implements Parcelable {
    private String pmTitle="默认";
    private int pmBalance=0;
    private String pmPrice="0";

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pmTitle);
        dest.writeInt(this.pmBalance);
        dest.writeString(this.pmPrice);
    }

    public ProductModel() {
    }

    protected ProductModel(Parcel in) {
        this.pmTitle = in.readString();
        this.pmBalance = in.readInt();
        this.pmPrice = in.readString();
    }

    public static final Parcelable.Creator<ProductModel> CREATOR = new Parcelable.Creator<ProductModel>() {
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
