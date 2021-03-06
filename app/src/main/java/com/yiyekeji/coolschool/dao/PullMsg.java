package com.yiyekeji.coolschool.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PULL_MSG".
 */
public class PullMsg {

    private Long id;
    private String owner;
    private String teacherName;
    private String title;
    private String courseNo;
    private String content;
    private String date;
    private String status;
    private Integer isRead;
    private String validDay;

    public PullMsg() {
    }

    public PullMsg(Long id) {
        this.id = id;
    }

    public PullMsg(Long id, String owner, String teacherName, String title, String courseNo, String content, String date, String status, Integer isRead, String validDay) {
        this.id = id;
        this.owner = owner;
        this.teacherName = teacherName;
        this.title = title;
        this.courseNo = courseNo;
        this.content = content;
        this.date = date;
        this.status = status;
        this.isRead = isRead;
        this.validDay = validDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getValidDay() {
        return validDay;
    }

    public void setValidDay(String validDay) {
        this.validDay = validDay;
    }

}
