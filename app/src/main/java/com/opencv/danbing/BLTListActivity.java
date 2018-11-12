package com.opencv.danbing;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencv.danbing.ble.BLTManager;
import com.opencv.danbing.ble.ClsUtil;
import com.opencv.danbing.blelib.BluetoothToos;
import com.opencv.danbing.blelib.IBluetoothListener;
import com.opencv.danbing.greendao.db.GreenDaoManager;
import com.opencv.danbing.greendao.entity.ScanResults;
import com.opencv.danbing.greendao.entity.ScanTasks;
import com.opencv.danbing.greendao.gen.ScanResultsDao;
import com.opencv.danbing.greendao.gen.ScanTasksDao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Pumatus on 2018/2/27.
 * MAC ADDRESS LIST VIEW
 */

public class BLTListActivity extends AppCompatActivity {

	private ListView list_address;
	private List<String> arr = new ArrayList<>();
	private MyAdapter myAdapter;
	private Button btn_cancel;
	private ScanResultsDao scanResultsDao;
	private ScanTasksDao scanTasksDao;
	private String defaultTaskID = "";
	private String startDT, endDT = "";

	private BluetoothToos bluetoothToos = new BluetoothToos();
	private BluetoothSocket bluetoothSocket;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String str = msg.obj.toString();
//					arr.add(str.substring(1, str.length() - 1));
					arr.add(str);
					list_address.setAdapter(myAdapter);
					myAdapter.notifyDataSetChanged();

					Calendar calendar = Calendar.getInstance();
					String data = (calendar.get(Calendar.YEAR)) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.DAY_OF_MONTH));
					String time = (calendar.get(Calendar.HOUR_OF_DAY)) + ":" + (calendar.get(Calendar.MINUTE)) + ":" + (calendar.get(Calendar.SECOND));
					String dt = (calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.DAY_OF_MONTH)) + " "
							+ (calendar.get(Calendar.HOUR_OF_DAY)) + ":" + (calendar.get(Calendar.MINUTE)) + ":" + (calendar.get(Calendar.SECOND));

					String dataType = macMatchcs(str.replace(":", "-"));
					ScanResults scanResults = new ScanResults();
					scanResults.setDt(dt);
					scanResults.setDataStr(data);
					scanResults.setTimeStr(time);
					scanResults.setMacAddress(str);
					scanResults.setDeviceType(dataType);
					scanResults.setDefaultTaskID(defaultTaskID);
					scanResultsDao.insert(scanResults);
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);

		list_address = findViewById(R.id.list_address);
		btn_cancel = findViewById(R.id.btn_cancel);
		myAdapter = new MyAdapter(BLTListActivity.this);

		scanResultsDao = GreenDaoManager.getDaoSession().getScanResultsDao();
		scanTasksDao = GreenDaoManager.getDaoSession().getScanTasksDao();
		String i = ClsUtil.getParam(BLTListActivity.this, "defaultTaskID");
		if (i.equals("")) {
			defaultTaskID = "1";
		} else {
			int o = Integer.parseInt(i);
			defaultTaskID = String.valueOf(o + 1);
		}
		ClsUtil.setUserParam(BLTListActivity.this, "defaultTaskID", defaultTaskID);

		btn_cancel.setOnClickListener(v -> {
			try {
				BLTManager.getInstance().getmBluetoothSocket().close();
				if (bluetoothSocket == null) {
					return;
				}
				boolean flag = bluetoothToos.close_bluetooth(bluetoothSocket);
				endDT = buildDT();
				ScanTasks scanTasks = new ScanTasks();
				scanTasks.setDefaultTaskID(defaultTaskID);
				scanTasks.setTaskDT(buildData());
				scanTasks.setTaskName(buildData());
				scanTasks.setStartTime(startDT);
				scanTasks.setEndTime(endDT);
				scanTasksDao.insert(scanTasks);
				Toast.makeText(BLTListActivity.this, "已结束扫描", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(BLTListActivity.this, DataResultActivity.class);
				intent.putExtra("defaultTaskID", defaultTaskID);
				startActivity(intent);
				finish();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		startDT = buildDT();
//		new Thread(() -> ReceiveSocketService.receiveMessage(handler)).start();

		Intent intent = getIntent();
		if (intent != null) {
			String address = intent.getStringExtra("address");
			new Thread(() -> init(address)).start();
		}
	}

	private void init(String address) {
		bluetoothSocket = bluetoothToos.getBluetoothSocket(address);
		if (bluetoothSocket == null) {
			return;
		}
		boolean isConn = bluetoothToos.connect_bluetooth(bluetoothSocket);
		if (isConn) {
			Log.e("isConn  ", "连接成功");
			bluetoothToos.getSockeMsg(bluetoothSocket, new IBluetoothListener() {
				@Override
				public void getMsg(final String str) {
					Log.e("isConn  ", str + "");
					String mac;
					String dbs;
					if (str.length() == 17) {
						mac = str;
						dbs = "";
					} else {        //DB
						mac = str.split(" ")[0];
						dbs = str.split(" ")[1].replace("rssi:", "");
					}

					Message message = new Message();
					message.obj = mac;
					message.what = 1;
					handler.sendMessage(message);
				}

				@Override
				public void getMsgList(List<String> list) {
					Log.e("isConn  ", list.size() + "");
				}
			});
		} else {
			Log.e("isConn  ", "连接失败");
		}
	}

	private class MyAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		private MyAdapter(Context context) {
			super();
			layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return arr.size();
		}

		@Override
		public Object getItem(int position) {
			return arr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({"InflateParams", "SetTextI18n"})
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			ViewHolder viewHolder;
			if (convertView == null) {
				v = layoutInflater.inflate(R.layout.activity_listview_item, null);
				viewHolder = new ViewHolder();
				viewHolder.bltAddress = v.findViewById(R.id.tv_mac_address);
				viewHolder.bltType = v.findViewById(R.id.tv_mac_type);
				viewHolder.bltTime = v.findViewById(R.id.tv_time);
				v.setTag(viewHolder);
			} else {
				v = convertView;
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Calendar calendar = Calendar.getInstance();
			String data = (calendar.get(Calendar.YEAR)) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.DAY_OF_MONTH));
			String time = (calendar.get(Calendar.HOUR_OF_DAY)) + ":" + (calendar.get(Calendar.MINUTE)) + ":" + (calendar.get(Calendar.SECOND));

			String dataType = macMatchcs(arr.get(position).replace(":", "-"));
			String address = arr.get(position);
			viewHolder.bltAddress.setText(address);
			viewHolder.bltType.setText(dataType);
			viewHolder.bltTime.setText(data + " " + time);

			return v;
		}

		private class ViewHolder {
			TextView bltAddress, bltType, bltTime;
		}
	}

	private String macMatchcs(String mac) {
		if (mac.startsWith("C4-07-2F") || mac.startsWith("0C-D6-BD") || mac.startsWith("BC-25-E0")
				|| mac.startsWith("58-7F-66") || mac.startsWith("90-67-1C") ||
				mac.startsWith("74-88-2A") || mac.startsWith("50-01-6b")) {
			return "华为";
		}
		if (mac.startsWith("9C-99-A0") || mac.startsWith("18-59-36") || mac.startsWith("98-FA-E3") || mac
				.startsWith("64-09-80") || mac.startsWith("64-CC-2E") || mac.startsWith("F8-A4-5F")) {
			return "小米";
		}
		if (mac.startsWith("44-66-FC ") || mac.startsWith("E8-BB-A8") || mac.startsWith("BC-3A-EA")
				|| mac.startsWith("6C-5C-14") || mac.startsWith("8C-0E-E3") || mac.startsWith("2C-5B-B8")) {
			return "oppo";
		}
		if (mac.startsWith("C4-B3-01") || mac.startsWith("E0-5F-45") || mac.startsWith("48-3B-38")
				|| mac.startsWith("E0-C7-67") || mac.startsWith("4C-32-75") || mac.startsWith("90-B9-31")) {
			return "苹果";
		}
		if (mac.startsWith("00-23-C2") || mac.startsWith("38-2D-E8") || mac.startsWith("D0-87-E2")
				|| mac.startsWith("20-55-31") || mac.startsWith("54-40-AD")) {
			return "三星";
		}
		if (mac.startsWith("68-3E-34") || mac.startsWith("90-F0-52") || mac.startsWith("5C-CD-7C")
				|| mac.startsWith("38-BC-1A ")) {
			return "魅族";
		}
		if (mac.startsWith("F4-29-81") || mac.startsWith("3C-A3-48") || mac.startsWith("28-FA-A0")
				|| mac.startsWith("3C-B6-B7") || mac.startsWith("54-19-C8") || mac.startsWith("E4-5A-A2")) {
			return "vivo";
		}
		if (mac.startsWith("98-FF-D0") || mac.startsWith("50-3C-C4") || mac.startsWith("E0-2C-B2")
				|| mac.startsWith("70-72-0D") || mac.startsWith("D4-22-3F")) {
			return "联想";
		}
		if (mac.startsWith("F4-B8-A7") || mac.startsWith("8C-79-67")) {
			return "中兴";
		}
		return "其他";
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			BLTManager.getInstance().getmBluetoothSocket().close();
			if (bluetoothSocket == null) {
				return;
			}
			boolean flag = bluetoothToos.close_bluetooth(bluetoothSocket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String buildDT() {
		Date date = new Date();
		//yyyy-MM-dd
		@SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(date);
	}

	private String buildData() {
		Date date = new Date();
		@SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
}
