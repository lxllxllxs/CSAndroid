package com.yiyekeji.coolschool.bean;

/**
 * Created by lxl on 2017/3/28.
 */
public class PullMsg {
  private long id;
  private String  teacherName;
  private String   title;
  private String   courseNo;
  private String   content;
  private String   date;
  private String   status;
  private String   validDay;

  public String getTeacherName() {
    return teacherName;
  }

  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "PullMsg{" +
            "id=" + id +
            ", teacherName='" + teacherName + '\'' +
            ", title='" + title + '\'' +
            ", courseNo='" + courseNo + '\'' +
            ", content='" + content + '\'' +
            ", date='" + date + '\'' +
            ", status='" + status + '\'' +
            ", validDay='" + validDay + '\'' +
            '}';
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

  public String getValidDay() {
    return validDay;
  }

  public void setValidDay(String validDay) {
    this.validDay = validDay;
  }


}
