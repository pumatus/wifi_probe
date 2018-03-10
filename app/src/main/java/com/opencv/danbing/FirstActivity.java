package com.opencv.danbing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Pumatus on 2018/2/26.
 */

public class FirstActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		//定时3000毫秒
		Integer time = 2000;
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(FirstActivity.this, MainActivity.class));
				finish();
			}
		}, time);
		
		//预先加载数据
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(3000);
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							startActivity(new Intent(FirstActivity.this, MainActivity.class));
//							finish();
//						}
//					});
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}
}
