package com.yiyekeji.coolschool.bean;

/**
 * Created by lxl on 2017/1/10.
 */
public class CourseInfo {

    private  int  id;
    private  String  count;
    private  String  courseName;
    private  String  roomNum;
    private  String  courseClass;
    private String   courseNo;
    public int getId() {
        return id;
    }

    public String getCourseNo() {
        return courseNo;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "id=" + id +
                ", count='" + count + '\'' +
                ", courseName='" + courseName + '\'' +
                ", roomNum='" + roomNum + '\'' +
                ", courseClass='" + courseClass + '\'' +
                ", courseNo='" + courseNo + '\'' +
                '}';
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(String courseClass) {
        this.courseClass = courseClass;
    }
}
