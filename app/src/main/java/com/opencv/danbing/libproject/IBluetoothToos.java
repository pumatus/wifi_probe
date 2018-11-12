package com.opencv.danbing.libproject;

import android.bluetooth.BluetoothSocket;

/**
 * Creation time: 2018/10/24
 * Editor: $USER_NAME
 * Functional description:
 */
public interface IBluetoothToos {

	BluetoothSocket getBluetoothSocket(String var1);

	boolean connect_bluetooth(BluetoothSocket var1);

	boolean close_bluetooth(BluetoothSocket var1);

	void getSockeMsg(BluetoothSocket var1, IBluetoothListener var2);
}
