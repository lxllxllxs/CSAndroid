package com.yiyekeji.coolschool.bean;

/**
 * 课程缺席记录
 * Created by lxl on 2017/1/16.
 */
public class CourseAbsenceInfo {
    private String userCount;
    private String courseTime;

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    @Override
    public String toString() {
        return "CourseAbsenceInfo{" +
                "userCount='" + userCount + '\'' +
                ", courseTime='" + courseTime + '\'' +
                '}';
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }
}
