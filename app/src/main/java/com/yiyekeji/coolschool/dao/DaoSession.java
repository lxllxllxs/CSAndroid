package com.yiyekeji.coolschool.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.yiyekeji.coolschool.dao.LessonSchedule;
import com.yiyekeji.coolschool.dao.StudentScore;
import com.yiyekeji.coolschool.dao.PullMsg;

import com.yiyekeji.coolschool.dao.LessonScheduleDao;
import com.yiyekeji.coolschool.dao.StudentScoreDao;
import com.yiyekeji.coolschool.dao.PullMsgDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig lessonScheduleDaoConfig;
    private final DaoConfig studentScoreDaoConfig;
    private final DaoConfig pullMsgDaoConfig;

    private final LessonScheduleDao lessonScheduleDao;
    private final StudentScoreDao studentScoreDao;
    private final PullMsgDao pullMsgDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        lessonScheduleDaoConfig = daoConfigMap.get(LessonScheduleDao.class).clone();
        lessonScheduleDaoConfig.initIdentityScope(type);

        studentScoreDaoConfig = daoConfigMap.get(StudentScoreDao.class).clone();
        studentScoreDaoConfig.initIdentityScope(type);

        pullMsgDaoConfig = daoConfigMap.get(PullMsgDao.class).clone();
        pullMsgDaoConfig.initIdentityScope(type);

        lessonScheduleDao = new LessonScheduleDao(lessonScheduleDaoConfig, this);
        studentScoreDao = new StudentScoreDao(studentScoreDaoConfig, this);
        pullMsgDao = new PullMsgDao(pullMsgDaoConfig, this);

        registerDao(LessonSchedule.class, lessonScheduleDao);
        registerDao(StudentScore.class, studentScoreDao);
        registerDao(PullMsg.class, pullMsgDao);
    }
    
    public void clear() {
        lessonScheduleDaoConfig.getIdentityScope().clear();
        studentScoreDaoConfig.getIdentityScope().clear();
        pullMsgDaoConfig.getIdentityScope().clear();
    }

    public LessonScheduleDao getLessonScheduleDao() {
        return lessonScheduleDao;
    }

    public StudentScoreDao getStudentScoreDao() {
        return studentScoreDao;
    }

    public PullMsgDao getPullMsgDao() {
        return pullMsgDao;
    }

}
