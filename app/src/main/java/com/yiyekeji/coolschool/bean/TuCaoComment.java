package com.yiyekeji.coolschool.bean;

/**
 * Created by Administrator on 2017/4/8/008.
 */
public class TuCaoComment {
    private int tuCaoId;
    private long id;
    private String content;
    private String date;

    public int getTuCaoId() {
        return tuCaoId;
    }

    public void setTuCaoId(int tuCaoId) {
        this.tuCaoId = tuCaoId;
    }

    private String userNo;

    @Override
    public String toString() {
        return "TuCaoComment{" +
                "tuCaoId=" + tuCaoId +
                ", id=" + id +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", userNo='" + userNo + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String author;
}
