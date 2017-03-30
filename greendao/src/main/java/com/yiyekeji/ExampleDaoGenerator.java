package com.yiyekeji;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ExampleDaoGenerator {

    public static void main(String[] args) throws IOException, Exception {
        
        Schema schema = new Schema(1, "com.yiyekeji.coolschool.dao");
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
        LessonSchedule.addStringProperty("studentNo");
        LessonSchedule.addStringProperty("htmlString");


        Entity PullMsg= schema.addEntity("PullMsg");
        PullMsg.addIdProperty();
        PullMsg.addStringProperty("teacherName");
        PullMsg.addStringProperty("title");
        PullMsg.addStringProperty("courseNo");
        PullMsg.addStringProperty("content");
        PullMsg.addStringProperty("date");
        PullMsg.addStringProperty("status");
        PullMsg.addIntProperty("isRead");
        PullMsg.addStringProperty("validDay");
    }


    private static void addStudent(Schema schema) {
        Entity Student = schema.addEntity("Student");
        Student.addIdProperty();
        Student.addStringProperty("name");
        Student.addStringProperty("age");
        Student.addStringProperty("sex");
        Student.addStringProperty("birthDay");//单聊 群聊？单聊没有此id
        Student.addStringProperty("studentId");//文字还是语音还是图片
        Student.addStringProperty("headImg");//非文本消息是需要根据messagetype转换
        Student.addStringProperty("nation");//发送日期
        Student.addStringProperty("phone");//发送状态
        Student.addStringProperty("note");//收件人姓名

        Entity Administrator = schema.addEntity("Administrator");
        Administrator.addIdProperty();
        Administrator.addStringProperty("name");
    }
}