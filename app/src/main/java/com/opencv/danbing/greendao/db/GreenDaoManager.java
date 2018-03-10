package com.opencv.danbing.greendao.db;

import com.opencv.danbing.BuildConfig;
import com.opencv.danbing.ble.BLTApplication;
import com.opencv.danbing.greendao.gen.DaoMaster;
import com.opencv.danbing.greendao.gen.DaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by Pumatus on 2018/3/9.
 */

public class GreenDaoManager {
	
	private static final String DATABASE_NAME = "local-save.db";
	private static GreenDaoManager mInstance;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	
	public static GreenDaoManager getInstance() {
		if(mInstance==null) {
			synchronized (GreenDaoManager.class) {
				if(mInstance==null) {
					mInstance =new GreenDaoManager();
				}
			}
		}
		return mInstance;
	}
	
	private GreenDaoManager() {
		if (mInstance == null) {
			GreenDaoOpenHelper helper = new GreenDaoOpenHelper(BLTApplication.context, DATABASE_NAME, null);
			Database database = helper.getWritableDb();
			daoMaster = new DaoMaster(database);
			daoSession = daoMaster.newSession();
			if (BuildConfig.DEBUG) {
				QueryBuilder.LOG_SQL = true;
				QueryBuilder.LOG_VALUES = true;
			}
		}
	}
	
	public static DaoMaster getDaoMaster() {
		return daoMaster;
	}
	
	public static DaoSession getDaoSession() {
		return daoSession;
	}
}
