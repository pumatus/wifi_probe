package com.opencv.danbing.blelib;

import java.util.List;

/**
 * Creation time: 2018/10/24
 * Editor: $USER_NAME
 * Functional description:
 */
public interface IBluetoothListener {
	void getMsg(String var1);

	void getMsgList(List<String> var1);
}
