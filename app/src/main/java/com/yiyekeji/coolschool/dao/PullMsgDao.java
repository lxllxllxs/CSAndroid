package com.yiyekeji.coolschool.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.yiyekeji.coolschool.dao.PullMsg;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PULL_MSG".
*/
public class PullMsgDao extends AbstractDao<PullMsg, Long> {

    public static final String TABLENAME = "PULL_MSG";

    /**
     * Properties of entity PullMsg.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TeacherName = new Property(1, String.class, "teacherName", false, "TEACHER_NAME");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property CourseNo = new Property(3, String.class, "courseNo", false, "COURSE_NO");
        public final static Property Content = new Property(4, String.class, "content", false, "CONTENT");
        public final static Property Date = new Property(5, String.class, "date", false, "DATE");
        public final static Property Status = new Property(6, String.class, "status", false, "STATUS");
        public final static Property IsRead = new Property(7, Integer.class, "isRead", false, "IS_READ");
        public final static Property ValidDay = new Property(8, String.class, "validDay", false, "VALID_DAY");
    };


    public PullMsgDao(DaoConfig config) {
        super(config);
    }
    
    public PullMsgDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PULL_MSG\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TEACHER_NAME\" TEXT," + // 1: teacherName
                "\"TITLE\" TEXT," + // 2: title
                "\"COURSE_NO\" TEXT," + // 3: courseNo
                "\"CONTENT\" TEXT," + // 4: content
                "\"DATE\" TEXT," + // 5: date
                "\"STATUS\" TEXT," + // 6: status
                "\"IS_READ\" INTEGER," + // 7: isRead
                "\"VALID_DAY\" TEXT);"); // 8: validDay
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PULL_MSG\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PullMsg entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String teacherName = entity.getTeacherName();
        if (teacherName != null) {
            stmt.bindString(2, teacherName);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String courseNo = entity.getCourseNo();
        if (courseNo != null) {
            stmt.bindString(4, courseNo);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(5, content);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(6, date);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(7, status);
        }
 
        Integer isRead = entity.getIsRead();
        if (isRead != null) {
            stmt.bindLong(8, isRead);
        }
 
        String validDay = entity.getValidDay();
        if (validDay != null) {
            stmt.bindString(9, validDay);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PullMsg readEntity(Cursor cursor, int offset) {
        PullMsg entity = new PullMsg( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // teacherName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // courseNo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // content
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // date
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // status
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // isRead
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // validDay
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PullMsg entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTeacherName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCourseNo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setContent(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDate(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setStatus(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsRead(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setValidDay(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PullMsg entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PullMsg entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}