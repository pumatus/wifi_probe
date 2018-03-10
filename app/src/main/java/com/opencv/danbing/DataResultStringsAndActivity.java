package com.opencv.danbing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.opencv.danbing.greendao.db.GreenDaoManager;
import com.opencv.danbing.greendao.entity.ScanTasks;
import com.opencv.danbing.greendao.gen.ScanTasksDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pumatus on 2018/3/10.
 */

public class DataResultStringsAndActivity extends AppCompatActivity implements OnClickListener {
	
	private HashMap<Integer, Boolean> isSelected;
	private ListView listView;
	private Button buttonResult;
	private CBListViewAdapter cbListViewAdapter;
	
	private List<ScanTasks> scanTasks;
	private ScanTasksDao scanTasksDao;
	private ArrayList<String> strings = new ArrayList<>();
	
	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_result_and);
		
		listView = findViewById(R.id.list_task);
		buttonResult = findViewById(R.id.btn_and_task);
		buttonResult.setOnClickListener(this::onClick);
		isSelected = new HashMap<>();
		
		scanTasksDao = GreenDaoManager.getDaoSession().getScanTasksDao();
		scanTasks = new ArrayList<>();
		scanTasks = scanTasksDao.queryBuilder().list();
		if (scanTasks != null && scanTasks.size() > 0) {
			cbListViewAdapter = new CBListViewAdapter(DataResultStringsAndActivity.this, scanTasks, isSelected);
			listView.setAdapter(cbListViewAdapter);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_and_task:
				if (scanTasks.size() > 0) {
					isSelected = CBListViewAdapter.getIsSelected();
					strings.clear();
					for (int i = 0; i < isSelected.size(); i++) {
						if (isSelected.get(i).equals(true)) {
							String defaultTaskID = scanTasks.get(i).getDefaultTaskID();
							strings.add(defaultTaskID);
							Log.e("defaultTaskID", defaultTaskID + " ");
						}
					}
					if (strings.size() > 0) {
						Intent intent = new Intent(this, DataResultActivity.class);
						intent.putStringArrayListExtra("defaultTaskIDList", strings);
						startActivity(intent);
					} else {
						showToast("至少选择一个任务用于串并计算");
					}
				} else {
					showToast("没有可用于串并计算的任务");
				}
				break;
			default:
				break;
		}
	}
	
	private void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}
}
