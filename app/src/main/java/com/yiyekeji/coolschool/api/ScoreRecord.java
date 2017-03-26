package com.yiyekeji.coolschool.api;

/**
 * 成绩
 *
 */
public class ScoreRecord {
	public int lessonTerm = 0;
	public String lessonCode = ""; // 课程代码
	public String lessonName = ""; // 课程名称
	public String category = ""; // 类别
	public String lessonScore = ""; // 学分
	public String score = ""; // 成绩
	public String remark = ""; // 备注

	public ScoreRecord(){}
	public ScoreRecord(int lessonTerm, String lessonCode, String lessonName, String category, String lessonScore, String score, String remark) {
		this.lessonTerm = lessonTerm;
		this.lessonCode = lessonCode;
		this.lessonName = lessonName;
		this.category = category;
		this.lessonScore = lessonScore;
		this.score = score;
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "ScoreRecord [lessonTerm=" + lessonTerm + ", lessonCode="
				+ lessonCode + ", lessonName=" + lessonName + ", category="
				+ category + ", lessonScore=" + lessonScore + ", score="
				+ score + ", remark=" + remark + "]";
	}
}

