package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/3/7.
 */
public class TuCao implements Parcelable {
    private String content;
    private String date;
    private String commentCount;
    private String postMan;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String viewCount) {
        this.date = viewCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPostMan() {
        return postMan;
    }

    public void setPostMan(String postMan) {
        this.postMan = postMan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.date);
        dest.writeString(this.commentCount);
        dest.writeString(this.postMan);
    }

    public TuCao() {
    }

    protected TuCao(Parcel in) {
        this.content = in.readString();
        this.date = in.readString();
        this.commentCount = in.readString();
        this.postMan = in.readString();
    }

    public static final Parcelable.Creator<TuCao> CREATOR = new Parcelable.Creator<TuCao>() {
        @Override
        public TuCao createFromParcel(Parcel source) {
            return new TuCao(source);
        }

        @Override
        public TuCao[] newArray(int size) {
            return new TuCao[size];
        }
    };
}
