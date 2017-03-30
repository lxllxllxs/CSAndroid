package com.yiyekeji.coolschool.db;

import android.database.sqlite.SQLiteDatabase;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.dao.DaoMaster;
import com.yiyekeji.coolschool.dao.DaoSession;
import com.yiyekeji.coolschool.dao.PullMsg;
import com.yiyekeji.coolschool.dao.PullMsgDao;
import com.yiyekeji.coolschool.utils.LogUtil;

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

     static  {
         LogUtil.d("DbUtil","数据库操作工具类正在初始化");
         helper = new DaoMaster.DevOpenHelper(App.getContext(), "cs_db", null);
         db = helper.getWritableDatabase();
         daoMaster = new DaoMaster(db);
         daoSession = daoMaster.newSession();
         pullMsgDao = daoSession.getPullMsgDao();  //拿到这么个工具dao
         LogUtil.d("DbUtil","数据库操作工具类初始化完成");
    }



    public static void insertPullMsg(PullMsg pullMsg){
        pullMsg.setIsRead(0);//默认都是未读
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
}
