package com.opencv.danbing.ble;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pumatus on 2018/3/6.
 * 设置自动跳过PIN码
 */

public class BluetoothConnectActivityReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.bluetooth.device.action.PAIRING_REQUEST".equals(intent.getAction())) {
			BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			try {
				abortBroadcast();
				boolean ret = ClsUtil.setPin(bluetoothDevice.getClass(), bluetoothDevice, "1234");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
