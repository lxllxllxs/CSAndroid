package com.yiyekeji.coolschool.api;

import java.io.Serializable;

/**
 *
 */

public class Lesson implements Serializable {

	private static final long serialVersionUID = 1L;
	public int LessonID = 0;
	public String LessonName = null;
	public String Teacher = null;
	public String Time = null;  //存放上课周数  例如   ：  	1，2，3，4
	public String startTime = null;
	public String endTime = null;
	public String address = null;
	public int classtime = 0; // 第几节
	public String week = null; // 星期几


	/**
	 * 默认的构造方法
	 */
	public Lesson() {
		LessonID = 0;
		LessonName = "";
		Teacher = "";
		Time = "";
		startTime = "";
		endTime = "";
		address = "";
		classtime = 0;
		week = "";
	}

	public Lesson(int lessonID, String lessonName, String teacher, String time, String startTime, String endTime, String address, int classtime, String week) {
		LessonID = lessonID;
		LessonName = lessonName;
		Teacher = teacher;
		Time = time;
		this.startTime = startTime;
		this.endTime = endTime;
		this.address = address;
		this.classtime = classtime;
		this.week = week;
	}

	@Override
	public String toString() {
		return "Lesson{" +
				"LessonID=" + LessonID +
				", LessonName='" + LessonName + '\'' +
				", Teacher='" + Teacher + '\'' +
				", Time='" + Time + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", address='" + address + '\'' +
				", classtime=" + classtime +
				", week='" + week + '\'' +
				'}';
	}

	public int getLessonID() {
		return LessonID;
	}

	public void setLessonID(int lessonID) {
		LessonID = lessonID;
	}

	public String getLessonName() {
		return LessonName;
	}

	public void setLessonName(String lessonName) {
		LessonName = lessonName;
	}

	public String getTeacher() {
		return Teacher;
	}

	public void setTeacher(String teacher) {
		Teacher = teacher;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getClasstime() {
		return classtime;
	}

	public void setClasstime(int classtime) {
		this.classtime = classtime;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

}

