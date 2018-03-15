package com.opencv.danbing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.opencv.danbing.greendao.db.GreenDaoManager;
import com.opencv.danbing.greendao.entity.ScanResults;
import com.opencv.danbing.greendao.gen.ScanResultsDao;
import com.opencv.danbing.greendao.gen.ScanResultsDao.Properties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Pumatus on 2018/3/9.
 */

public class DataResultActivity extends AppCompatActivity {
	
	private List<ScanResults> scanResultsList = new ArrayList<>();
	private List strList = new ArrayList();
	private List<ScanResults> resultsListCompare = new ArrayList<>();
	private List<String> defaultTaskIDList = new ArrayList<>();
	private List<List<String>> stringlists = new ArrayList<>();
	private List<String> setStrings = new ArrayList<>();
	private boolean adapterFlag = false;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_scan_result);
		ListView listView = findViewById(R.id.list_address);
		
		ScanResultsDao scanResultsDao = GreenDaoManager.getDaoSession().getScanResultsDao();
		MyAdapter myAdapter = new MyAdapter(DataResultActivity.this);
		Intent intent = getIntent();
		if (intent != null) {
			scanResultsList.clear();
			strList.clear();
			String defaultTaskID = intent.getStringExtra("defaultTaskID");
			if (defaultTaskID != null) {
				adapterFlag = true;
				scanResultsList = scanResultsDao.queryBuilder().where(Properties.DefaultTaskID.eq(defaultTaskID))
						.list();
				
				if (scanResultsList != null && scanResultsList.size() > 0) {
					for (int i = 0; i < scanResultsList.size(); i++) {
						strList.add(scanResultsList.get(i).getMacAddress());
					}
					resultsListCompare = removeDuplicateResult(scanResultsList);
					listView.setAdapter(myAdapter);
					myAdapter.notifyDataSetChanged();
				}
				
				listView.setAdapter(myAdapter);
				myAdapter.notifyDataSetChanged();
			}
			
			defaultTaskIDList.clear();
			defaultTaskIDList = intent.getStringArrayListExtra("defaultTaskIDList");
			if (defaultTaskIDList != null && defaultTaskIDList.size() > 0) {
				adapterFlag = false;
				for (int i = 0; i < defaultTaskIDList.size(); i++) {
					List<ScanResults> scanResults = scanResultsDao.queryBuilder()
							.where(Properties.DefaultTaskID.eq(defaultTaskIDList.get(i))).list();
					scanResultsList.addAll(scanResults);
					
					List<String> strings = new ArrayList<>();
					for (ScanResults scanResults1 : scanResults) {
						strings.add(scanResults1.getMacAddress());
					}
					stringlists.add(strings);
					
				}
				Set<String> set = getIntersection(stringlists);
				setStrings.addAll(set);
				/////
				if (scanResultsList != null && scanResultsList.size() > 0) {
					for (int i = 0; i < scanResultsList.size(); i++) {
						strList.add(scanResultsList.get(i).getMacAddress());
						Log.e("scanResultsList ", scanResultsList.get(i).getMacAddress());
					}
					resultsListCompare = removeDuplicateResult(scanResultsList);
					
					listView.setAdapter(myAdapter);
					myAdapter.notifyDataSetChanged();
				}
			}
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
			if (adapterFlag) {
				return resultsListCompare.size();
			} else {
				return setStrings.size();
			}
		}
		
		@Override
		public Object getItem(int position) {
			if (adapterFlag) {
				return resultsListCompare.get(position);
			} else {
				return setStrings.get(position);
			}
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
				v = layoutInflater.inflate(R.layout.listview_data_and, null);
				viewHolder = new ViewHolder();
				viewHolder.bltAddress = v.findViewById(R.id.tv_mac_address);
				viewHolder.bltType = v.findViewById(R.id.tv_mac_type);
				viewHolder.bltCount = v.findViewById(R.id.tv_count);
				viewHolder.bltTime = v.findViewById(R.id.tv_time);
				v.setTag(viewHolder);
			} else {
				v = convertView;
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (adapterFlag) {
				viewHolder.bltAddress.setText(resultsListCompare.get(position).getMacAddress());
				viewHolder.bltType.setText(resultsListCompare.get(position).getDeviceType());
				viewHolder.bltCount.setText(Collections.frequency(strList, scanResultsList.get(position).getMacAddress()) + "");
				viewHolder.bltTime.setText("采集时间: " + resultsListCompare.get(position).getDt());
				
			} else {
				viewHolder.bltAddress.setText(setStrings.get(position));
				viewHolder.bltType.setText(buildType(setStrings.get(position)));
				viewHolder.bltCount.setText(Collections.frequency(strList, setStrings.get(position)) + "");
				viewHolder.bltTime.setText("采集时间: " + buildTime(setStrings.get(position)));
			}
			return v;
		}
		
		private class ViewHolder {
			
			TextView bltAddress, bltType, bltCount, bltTime;
		}
	}
	
	private String buildType(String mac) {
		String type = "";
		for (int i = 0; i < scanResultsList.size(); i++) {
			if (mac.equals(scanResultsList.get(i).getMacAddress())) {
				type = scanResultsList.get(i).getDeviceType();
			}
		}
		return type;
	}
	
	private String buildTime(String mac) {
		String time = "";
		for (int i = 0; i < scanResultsList.size(); i++) {
			if (mac.equals(scanResultsList.get(i).getMacAddress())) {
				time = scanResultsList.get(i).getDt();
			}
		}
		return time;
	}
	
	private static ArrayList<ScanResults> removeDuplicateResult(List<ScanResults> scanResults) {
		Set<ScanResults> set = new TreeSet<>((o1, o2) -> o1.getMacAddress().compareTo(o2.getMacAddress()));
		set.addAll(scanResults);
		return new ArrayList<>(set);
	}
	
	//取多个集合的交集
	private Set<String> getIntersection(List<List<String>> list) {
		Set<String> set = new HashSet<>();
		int size = list.size();
		if (size > 1) {
//取集合中的交集
			for (int i = 0; i < size; i++) {
				int j = i + 1;
				if (j < size) {
					list.get(0).retainAll(list.get(j));
					if (i == size - 2) {
						List<String> resultList = list.get(0);
						set.addAll(resultList);
					}
				}
			}
		} else {
//只有一个集合则不取交集
			for (List<String> list2 : list) {
				set.addAll(list2);
			}
		}
		return set;
	}
	
}