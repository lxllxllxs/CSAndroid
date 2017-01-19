package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yiyekeji.coolschool.inter.HaveName;

/**
 * Created by lxl on 2017/1/18.
 */
public class CategoryInfo implements Parcelable,HaveName {
    private   int categoryId;
    private String categoryName;
    private boolean isSelect;
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryId);
        dest.writeString(this.categoryName);
    }

    public CategoryInfo() {
    }

    protected CategoryInfo(Parcel in) {
        this.categoryId = in.readInt();
        this.categoryName = in.readString();
    }

    public static final Parcelable.Creator<CategoryInfo> CREATOR = new Parcelable.Creator<CategoryInfo>() {
        @Override
        public CategoryInfo createFromParcel(Parcel source) {
            return new CategoryInfo(source);
        }

        @Override
        public CategoryInfo[] newArray(int size) {
            return new CategoryInfo[size];
        }
    };

    @Override
    public String getName() {
        return categoryName;
    }

    @Override
    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public void setSelect(boolean b) {
        this.isSelect = b;
    }
}
