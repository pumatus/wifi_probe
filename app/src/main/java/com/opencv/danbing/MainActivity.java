package com.opencv.danbing;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.opencv.danbing.ble.BLTContant;
import com.opencv.danbing.ble.BLTManager;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	
	private List<BluetoothDevice> bltList;
	private MyAdapter myAdapter;
	private TextView textViewTitle;
	private TextView tvTitle;
	private AlertDialog build;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1://搜索蓝牙
					break;
				case 2://蓝牙可以被搜索
					break;
				case 3://设备已经接入
					BluetoothDevice device = (BluetoothDevice) msg.obj;
					Log.e("BluetoothDevice ", device.getName() + "");
					if (device != null) {
						tvTitle.setText("设备" + device.getName() + "已经接入");
						Toast.makeText(MainActivity.this, "设备" + device.getName() + "已经接入", Toast.LENGTH_LONG).show();
					}
					break;
				case 4://已连接某个设备
					if (build.isShowing() && build != null) {
						build.dismiss();
					}
					BluetoothDevice device1 = (BluetoothDevice) msg.obj;
					Log.e("BluetoothDevice ", device1.getName() + "");
					tvTitle.setText("已连接" + device1.getName() + "设备");
					Toast.makeText(MainActivity.this, "已连接" + device1.getName() + "设备", Toast.LENGTH_LONG).show();
					Intent intent1 = new Intent(MainActivity.this, BLTListActivity.class);
					intent1.putExtra("address", device1.getAddress());
					startActivity(intent1);
					break;
				case 5:
					tvTitle.setText("选取设备连接");
					Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BLTManager.getInstance().initBltManager(this);
		
		ImageView imageView = findViewById(R.id.img_click);
		imageView.setOnClickListener(this);
		ImageView dataImg = findViewById(R.id.img_data_strings_and);
		dataImg.setOnClickListener(this);
		textViewTitle = findViewById(R.id.tv_title);
		
		bltList = new ArrayList<>();
		myAdapter = new MyAdapter();
		//检查蓝牙是否开启
		BLTManager.getInstance().checkBleDevice(this);
		//注册蓝牙扫描广播
		blueToothRegister();
		//第一次进来搜索设备
		BLTManager.getInstance().clickBlt(this, BLTContant.BLUE_TOOTH_SEARTH);
	}
	
	/**
	 * 注册蓝牙回调广播
	 */
	private void blueToothRegister() {
		BLTManager.getInstance().registerBltReceiver(this, new BLTManager.OnRegisterBltReceiver() {
			
			/**搜索到新设备
			 * @param device
			 */
			@Override
			public void onBluetoothDevice(BluetoothDevice device) {
				if (bltList != null && !bltList.contains(device)) {
					bltList.add(device);
				}
				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
			}
			
			/**连接中
			 * @param device
			 */
			@Override
			public void onBltIng(BluetoothDevice device) {
				tvTitle.setText("连接" + device.getName() + "中……");
			}
			
			/**连接完成
			 * @param device
			 */
			@Override
			public void onBltEnd(BluetoothDevice device) {
				tvTitle.setText("连接" + device.getName() + "完成");
			}
			
			/**取消链接
			 * @param device
			 */
			@Override
			public void onBltNone(BluetoothDevice device) {
				tvTitle.setText("取消了连接" + device.getName());
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_click:
				showOneDialog();
				break;
			case R.id.img_data_strings_and:
				startActivity(new Intent(MainActivity.this, DataResultStringsAndActivity.class));
				break;
			default:
				break;
		}
	}
	
	private void showOneDialog() {
		build = new AlertDialog.Builder(this).create();
		View view = getLayoutInflater().inflate(R.layout.dialog_costom, null);
		build.setView(view, 0, 0, 0, 0);
		build.setCanceledOnTouchOutside(false);
		build.show();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		WindowManager.LayoutParams params = build.getWindow().getAttributes();
		params.width = width - (width / 6);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		build.getWindow().setAttributes(params);
		tvTitle = view.findViewById(R.id.tv_title);
		ListView listView = view.findViewById(R.id.list);
		listView.setAdapter(myAdapter);
		Button searchBtn = view.findViewById(R.id.btn_search);
		Button cancelBtn = view.findViewById(R.id.btn_cancel);
		searchBtn.setOnClickListener(v -> {
			tvTitle.setText("查找设备中...");
			BLTManager.getInstance().clickBlt(MainActivity.this, BLTContant.BLUE_TOOTH_SEARTH);
		});
		cancelBtn.setOnClickListener(v -> {
//				BLTManager.getInstance().unregisterReceiver(MainActivity.this);
			build.dismiss();
		});
		listView.setOnItemClickListener((parent, view1, position, id) -> {
			final BluetoothDevice bluetoothDevice = bltList.get(position);
			tvTitle.setText("正在连接" + bluetoothDevice.getName());
			//链接的操作应该在子线程
			new Thread(() -> BLTManager.getInstance().createBond(bluetoothDevice, handler)).start();
		});
	}
	
	private class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return bltList.size();
		}
		
		@Override
		public Object getItem(int position) {
			return bltList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			ViewHolder viewHolder;
			BluetoothDevice device = bltList.get(position);
			if (convertView == null) {
				v = getLayoutInflater().inflate(R.layout.listview_item, null);
				viewHolder = new ViewHolder();
				viewHolder.bltName = v.findViewById(R.id.tv_name);
				viewHolder.bltAddress = v.findViewById(R.id.tv_address);
				v.setTag(viewHolder);
			} else {
				v = convertView;
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.bltName.setText(device.getName());
			viewHolder.bltAddress.setText(device.getAddress());
			return v;
		}
		
		private class ViewHolder {
			
			TextView bltName, bltAddress;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BLTManager.getInstance().unregisterReceiver(this);
	}
}
