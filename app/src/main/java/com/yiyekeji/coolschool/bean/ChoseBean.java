package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/25.
 */
public class ChoseBean implements Parcelable {
    private String  key;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isSelect;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    public ChoseBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeString(this.value);
    }

    protected ChoseBean(Parcel in) {
        this.key = in.readString();
        this.isSelect = in.readByte() != 0;
        this.value = in.readString();
    }

    public static final Creator<ChoseBean> CREATOR = new Creator<ChoseBean>() {
        @Override
        public ChoseBean createFromParcel(Parcel source) {
            return new ChoseBean(source);
        }

        @Override
        public ChoseBean[] newArray(int size) {
            return new ChoseBean[size];
        }
    };
}
