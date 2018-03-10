package com.opencv.danbing.greendao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import com.opencv.danbing.greendao.gen.DaoMaster;
import com.opencv.danbing.greendao.gen.ScanResultsDao;
import com.opencv.danbing.greendao.gen.ScanTasksDao;
import org.greenrobot.greendao.database.Database;

/**
 * Created by Pumatus on 2018/3/9.
 */

public class GreenDaoOpenHelper extends DaoMaster.OpenHelper {
	
	public GreenDaoOpenHelper(Context context, String name) {
		super(context, name);
	}
	
	public GreenDaoOpenHelper(Context context, String name, CursorFactory factory) {
		super(context, name, factory);
	}
	
	@Override
	public void onCreate(Database db) {
		super.onCreate(db);
	}
	
	@Override
	public void onUpgrade(Database db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
		switch (oldVersion) {
			case 1:
				ScanTasksDao.createTable(db, true);
				ScanResultsDao.createTable(db, true);
			case 2:
				ScanTasksDao.dropTable(db, true);
				ScanResultsDao.dropTable(db, true);
				break;
		}
	}
}
