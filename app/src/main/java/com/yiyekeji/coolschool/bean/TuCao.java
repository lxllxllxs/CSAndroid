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
    private String author;
    private String userNo;
    private String sex;
    private int id;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getSex() {
        return sex;
    }

    @Override
    public String toString() {
        return "TuCao{" +
                "content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", author='" + author + '\'' +
                ", userNo='" + userNo + '\'' +
                ", sex='" + sex + '\'' +
                ", id=" + id +
                '}';
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public TuCao() {
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
        dest.writeString(this.author);
        dest.writeString(this.userNo);
        dest.writeString(this.sex);
        dest.writeInt(this.id);
    }

    protected TuCao(Parcel in) {
        this.content = in.readString();
        this.date = in.readString();
        this.commentCount = in.readString();
        this.author = in.readString();
        this.userNo = in.readString();
        this.sex = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<TuCao> CREATOR = new Creator<TuCao>() {
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
