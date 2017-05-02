package com.yiyekeji;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ExampleDaoGenerator {

    public static void main(String[] args) throws IOException, Exception {
        // FIXME: 2017/5/2/002 这里要更改数据库的版本 否则崩溃
        Schema schema = new Schema(2, "com.yiyekeji.coolschool.dao");
        addSendMessage(schema);
        DaoGenerator daoGenerator= new DaoGenerator();
        daoGenerator .generateAll(schema, "D:\\DaoExample");
    }

    /**
     * 主键用于dao的更新操作
     * msgId用于比对服务端信息
     * @param schema
     */
    private static void addSendMessage(Schema schema) {
        Entity LessonSchedule= schema.addEntity("LessonSchedule");
        LessonSchedule.addIdProperty();
        LessonSchedule.addStringProperty("studentNo");//归属
        LessonSchedule.addStringProperty("htmlString");

        Entity StudentScore= schema.addEntity("StudentScore");
        StudentScore.addIdProperty();
        StudentScore.addStringProperty("studentNo");//归属
        StudentScore.addStringProperty("htmlString");


        Entity PullMsg= schema.addEntity("PullMsg");
        PullMsg.addIdProperty();
        PullMsg.addStringProperty("owner");//归属
        PullMsg.addStringProperty("teacherName");
        PullMsg.addStringProperty("title");
        PullMsg.addStringProperty("courseNo");
        PullMsg.addStringProperty("content");
        PullMsg.addStringProperty("date");
        PullMsg.addStringProperty("status");
        PullMsg.addIntProperty("isRead");
        PullMsg.addStringProperty("validDay");
    }

}