package com.opencv.danbing.ble;

import android.os.Handler;
import android.os.Message;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Pumatus on 2018/2/28.
 * 接受蓝牙消息的服务
 */

public class ReceiveSocketService {
	
	public static void receiveMessage(Handler handler) {
		if (BLTApplication.bluetoothSocket == null || handler == null) return;
		InputStream inputStream;
		BufferedReader bufferedReader;
		try {
			inputStream = BLTApplication.bluetoothSocket.getInputStream();
			//从客户端接收消息
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String json;
			if (bufferedReader.readLine() != null) {
				while (true) {
					while ((json = bufferedReader.readLine()) != null) {
						Message message = new Message();
						message.obj = json;
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
