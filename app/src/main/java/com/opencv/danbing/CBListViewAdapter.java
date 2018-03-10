package com.opencv.danbing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.opencv.danbing.greendao.db.GreenDaoManager;
import com.opencv.danbing.greendao.entity.ScanResults;
import com.opencv.danbing.greendao.entity.ScanTasks;
import com.opencv.danbing.greendao.gen.ScanResultsDao;
import com.opencv.danbing.greendao.gen.ScanResultsDao.Properties;
import com.opencv.danbing.greendao.gen.ScanTasksDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pumatus on 2018/3/10.
 */

public class CBListViewAdapter extends BaseAdapter {
	
	private Context context;
	private List<ScanTasks> beans;
	private ScanResultsDao scanResultsDao;
	private ScanTasksDao scanTasksDao;
	private ScanTasks scanTasks = new ScanTasks();
	private List<ScanResults> scanResultsList = new ArrayList<>();
	
	public static HashMap<Integer, Boolean> isSelected;
	
	class ViewHolder {
		CheckBox checkBox;
		TextView taskName;
		TextView taskDT;
		RelativeLayout rlSelect;
		RelativeLayout rlDelete;
	}
	
	public CBListViewAdapter(Context context, List<ScanTasks> beans, HashMap<Integer, Boolean> isSelected) {
		this.beans = beans;
		this.context = context;
		CBListViewAdapter.isSelected = isSelected;
		initData();
	}
	
	private void initData() {
		for (int i = 0; i < beans.size(); i++) {
			getIsSelected().put(i, false);
		}
	}
	
	@Override
	public int getCount() {
		return beans.size();
	}
	
	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@SuppressLint("SetTextI18n")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		ScanTasks beans = this.beans.get(position);
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_data_task, null);
			viewHolder = new ViewHolder();
			viewHolder.checkBox = convertView.findViewById(R.id.cb_selector);
			viewHolder.taskName = convertView.findViewById(R.id.tv_scan_task);
			viewHolder.taskDT = convertView.findViewById(R.id.tv_scan_dt);
			viewHolder.rlSelect = convertView.findViewById(R.id.rl_select);
			viewHolder.rlDelete = convertView.findViewById(R.id.rl_delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.taskName.setText("扫描任务-" + beans.getTaskName() + "-" + beans.getDefaultTaskID());
		viewHolder.taskDT.setText(beans.getStartTime() + "-" + beans.getEndTime());
		viewHolder.rlSelect.setOnClickListener(v -> {
			Intent intent = new Intent(context, DataResultActivity.class);
			intent.putExtra("defaultTaskID", beans.getDefaultTaskID());
			context.startActivity(intent);
		});
		viewHolder.rlDelete.setOnClickListener(v -> {
			scanResultsDao = GreenDaoManager.getDaoSession().getScanResultsDao();
			scanTasksDao = GreenDaoManager.getDaoSession().getScanTasksDao();
			
			scanResultsList = scanResultsDao.queryBuilder().where(Properties.DefaultTaskID.eq(beans.getDefaultTaskID())).list();
			scanResultsDao.deleteInTx(scanResultsList);
			scanTasks = scanTasksDao.queryBuilder().where(ScanTasksDao.Properties.DefaultTaskID.eq(beans.getDefaultTaskID())).unique();
			scanTasksDao.delete(scanTasks);
			
			this.beans.remove(position);
			notifyDataSetChanged();
			Toast.makeText(context, "已删除该扫描任务段的所有信息", Toast.LENGTH_LONG).show();
		});
		viewHolder.checkBox.setChecked(getIsSelected().get(position));
		viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				isSelected.put(position, true);
				setIsSelected(isSelected);
			} else {
				isSelected.put(position, false);
				setIsSelected(isSelected);
			}
		});
		return convertView;
	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		CBListViewAdapter.isSelected = isSelected;
	}
}