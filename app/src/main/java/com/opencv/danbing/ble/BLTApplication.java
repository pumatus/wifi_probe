package com.opencv.danbing.ble;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import com.opencv.danbing.greendao.db.GreenDaoManager;

/**
 * Created by Pumatus on 2018/2/27.
 */

public class BLTApplication extends Application {
	
	public static Context context = null;
	
	public static BluetoothSocket bluetoothSocket;
	
	public static Context getInstances() {
		return context;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		
		GreenDaoManager.getInstance();
	}
}
