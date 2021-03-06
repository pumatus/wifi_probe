package com.opencv.danbing.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.opencv.danbing.greendao.entity.ScanTasks;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SCAN_TASKS".
*/
public class ScanTasksDao extends AbstractDao<ScanTasks, Long> {

    public static final String TABLENAME = "SCAN_TASKS";

    /**
     * Properties of entity ScanTasks.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TaskName = new Property(1, String.class, "taskName", false, "TASK_NAME");
        public final static Property TaskDT = new Property(2, String.class, "taskDT", false, "TASK_DT");
        public final static Property StartTime = new Property(3, String.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(4, String.class, "endTime", false, "END_TIME");
        public final static Property DefaultTaskID = new Property(5, String.class, "defaultTaskID", false, "DEFAULT_TASK_ID");
    }


    public ScanTasksDao(DaoConfig config) {
        super(config);
    }
    
    public ScanTasksDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SCAN_TASKS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TASK_NAME\" TEXT," + // 1: taskName
                "\"TASK_DT\" TEXT," + // 2: taskDT
                "\"START_TIME\" TEXT," + // 3: startTime
                "\"END_TIME\" TEXT," + // 4: endTime
                "\"DEFAULT_TASK_ID\" TEXT);"); // 5: defaultTaskID
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SCAN_TASKS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ScanTasks entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String taskName = entity.getTaskName();
        if (taskName != null) {
            stmt.bindString(2, taskName);
        }
 
        String taskDT = entity.getTaskDT();
        if (taskDT != null) {
            stmt.bindString(3, taskDT);
        }
 
        String startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindString(4, startTime);
        }
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(5, endTime);
        }
 
        String defaultTaskID = entity.getDefaultTaskID();
        if (defaultTaskID != null) {
            stmt.bindString(6, defaultTaskID);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ScanTasks entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String taskName = entity.getTaskName();
        if (taskName != null) {
            stmt.bindString(2, taskName);
        }
 
        String taskDT = entity.getTaskDT();
        if (taskDT != null) {
            stmt.bindString(3, taskDT);
        }
 
        String startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindString(4, startTime);
        }
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(5, endTime);
        }
 
        String defaultTaskID = entity.getDefaultTaskID();
        if (defaultTaskID != null) {
            stmt.bindString(6, defaultTaskID);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ScanTasks readEntity(Cursor cursor, int offset) {
        ScanTasks entity = new ScanTasks( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // taskName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // taskDT
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // startTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // endTime
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // defaultTaskID
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ScanTasks entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTaskName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTaskDT(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStartTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEndTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDefaultTaskID(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ScanTasks entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ScanTasks entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ScanTasks entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
