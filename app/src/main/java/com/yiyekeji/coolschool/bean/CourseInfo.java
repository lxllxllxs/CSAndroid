package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lxl on 2017/1/10.
 */
public class CourseInfo implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.count);
        dest.writeString(this.courseName);
        dest.writeString(this.roomNum);
        dest.writeString(this.courseClass);
        dest.writeString(this.courseNo);
    }

    public CourseInfo() {
    }

    protected CourseInfo(Parcel in) {
        this.id = in.readInt();
        this.count = in.readString();
        this.courseName = in.readString();
        this.roomNum = in.readString();
        this.courseClass = in.readString();
        this.courseNo = in.readString();
    }

    public static final Parcelable.Creator<CourseInfo> CREATOR = new Parcelable.Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel source) {
            return new CourseInfo(source);
        }

        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };
}
