package com.yiyekeji.coolschool.db;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.dao.DaoMaster;
import com.yiyekeji.coolschool.dao.DaoSession;
import com.yiyekeji.coolschool.dao.LessonSchedule;
import com.yiyekeji.coolschool.dao.LessonScheduleDao;
import com.yiyekeji.coolschool.dao.PullMsg;
import com.yiyekeji.coolschool.dao.PullMsgDao;
import com.yiyekeji.coolschool.dao.StudentScore;
import com.yiyekeji.coolschool.dao.StudentScoreDao;
import com.yiyekeji.coolschool.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Created by lxl on 2016/11/4.
 */
public class DbUtil {
    private static DaoMaster.DevOpenHelper helper;
    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private  static PullMsgDao pullMsgDao;
    private  static StudentScoreDao scoreDao;
    private  static LessonScheduleDao scheduleDao;

     static  {
         LogUtil.d("DbUtil","数据库操作工具类正在初始化");
         helper = new DaoMaster.DevOpenHelper(App.getContext(), "cs_db", null);
         db = helper.getWritableDatabase();
         daoMaster = new DaoMaster(db);
         daoSession = daoMaster.newSession();
         pullMsgDao = daoSession.getPullMsgDao();  //拿到这么个工具dao
         scoreDao = daoSession.getStudentScoreDao();
         scheduleDao = daoSession.getLessonScheduleDao();
         LogUtil.d("DbUtil","数据库操作工具类初始化完成");
    }



    public static void insertPullMsg(PullMsg pullMsg){
        pullMsg.setIsRead(0);//默认都是未读
        pullMsg.setOwner(App.getUserInfo().getUserNum());
        //只要关心是否同信息 不用知道接收的
        Query query = pullMsgDao.queryBuilder()
                .where(PullMsgDao.Properties.Id.eq(pullMsg.getId()))
                .build();
        if (query.list().isEmpty()) {
            pullMsgDao.insert(pullMsg);
        }
    }


    public static boolean havaUnReadMsg(){
        Query query = pullMsgDao.queryBuilder()
                .where(PullMsgDao.Properties.IsRead.eq("0"))
                .build();
        return !query.list().isEmpty();
    }

    /**
     * 保存 学生查询成绩返回的原始的html字符串
     * @param htmlString
     */
    public static void insertScoreString(String htmlString ){
        StudentScore score = new StudentScore();
        score.setHtmlString(htmlString);
        score.setStudentNo(App.getUserInfo().getUserNum());
        Query query = scoreDao.queryBuilder()
                .where(StudentScoreDao.Properties.StudentNo.eq(score.getStudentNo()))
                .build();
        if (query.list().isEmpty()) {
            scoreDao.insert(score);
        } else {
            List<StudentScore> studentScoreList=query.list();
            score.setId(studentScoreList.get(0).getId());
            scoreDao.update(score);
        }

    }

    /**
     * 保存 学生查询课程表返回的原始的html字符串
     * @param htmlString
     */
    public static void insertScheduleString(String htmlString){
        LessonSchedule schedule = new LessonSchedule();
        schedule.setHtmlString(htmlString);
        schedule.setStudentNo(App.getUserInfo().getUserNum());
        Query query = scheduleDao.queryBuilder()
                .where(LessonScheduleDao.Properties.StudentNo.eq(schedule.getStudentNo()))
                .build();
        if (query.list().isEmpty()) {
            scheduleDao.insert(schedule);
        } else {
            List<LessonSchedule> scheduleList=query.list();
            schedule.setId(scheduleList.get(0).getId());
            scheduleDao.update(schedule);
        }
    }

    /**
     * 根据学号获得对应的课程表htmlString
     * @return
     */
    public static String getScheduleString(){
        String studentNo = App.getUserInfo().getUserNum();
        Query query = scheduleDao.queryBuilder()
                .where(LessonScheduleDao.Properties.StudentNo.eq(studentNo))
                .build();
        if (!query.list().isEmpty()) {
            List<LessonSchedule> list = query.list();
            return  list.get(0).getHtmlString();
        }
        return  "";
    }

    /**
     * 根据学号获得对应的成绩htmlString
     * @return
     */
    public static String getScoreString(){
        String studentNo = App.getUserInfo().getUserNum();
        Query query = scoreDao.queryBuilder()
                .where(StudentScoreDao.Properties.StudentNo.eq(studentNo))
                .build();
        if (!query.list().isEmpty()) {
            List<StudentScore> list = query.list();
            return  list.get(0).getHtmlString();
        }
        return  "";
    }


    /**
     *  fixme 根据owner获得其所有 注意排序
     * 获得所有通知消息
     * @return
     */
    public static List<PullMsg> getAllPullMsg(){
        String  owner=App.getUserInfo().getUserNum();
        if (TextUtils.isEmpty(owner)) {
            return new ArrayList<>();
        }
        Query query = pullMsgDao.queryBuilder()
                .where(PullMsgDao.Properties.Owner.eq(owner))
                .orderDesc(PullMsgDao.Properties.Id)
                .build();
        if (!query.list().isEmpty()) {
            List<PullMsg> list = query.list();
            return  list;
        }
        return new ArrayList<>();
    }

    /**
     * 更新通知消息
     * @return
     */
    public static void upDatePullMsg(PullMsg msg){
        pullMsgDao.update(msg);
    }

}
