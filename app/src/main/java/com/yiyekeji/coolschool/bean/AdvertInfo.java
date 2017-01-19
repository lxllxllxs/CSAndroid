package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/19.
 */
public class AdvertInfo implements Parcelable {
    private int advertId;
    private int pId;

    public int getAdvertId() {
        return advertId;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getAdvertImage() {
        return advertImage;
    }

    public void setAdvertImage(String advertImage) {
        this.advertImage = advertImage;
    }

    private String advertImage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.advertId);
        dest.writeInt(this.pId);
        dest.writeString(this.advertImage);
    }

    public AdvertInfo() {
    }

    protected AdvertInfo(Parcel in) {
        this.advertId = in.readInt();
        this.pId = in.readInt();
        this.advertImage = in.readString();
    }

    public static final Parcelable.Creator<AdvertInfo> CREATOR = new Parcelable.Creator<AdvertInfo>() {
        @Override
        public AdvertInfo createFromParcel(Parcel source) {
            return new AdvertInfo(source);
        }

        @Override
        public AdvertInfo[] newArray(int size) {
            return new AdvertInfo[size];
        }
    };
}
