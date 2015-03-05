package com.wm.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

import com.wm.activity.AboutActivity;
import com.wm.activity.GuideViewActivity;
import com.wm.activity.MainActivity;
import com.wm.activity.R;
import com.wm.entity.SettingData;

public class SettingFragment extends Fragment  {
	
	@InjectView(R.id.setting_list)
	ListView mSettingList;
	private SettingListAdapter adapter;
	private List<SettingData> items = new ArrayList<SettingData>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		ButterKnife.inject(this, view);
		return view;
	}
	
	private void initItems() {
		// 版本信息应从服务器获取
		items.add(new SettingData("版本信息", "Demo版"));
		items.add(new SettingData("关于我们", new Intent(getActivity(), AboutActivity.class)));
		items.add(new SettingData("使用帮助", new Intent(getActivity(), GuideViewActivity.class)));
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(items.size() == 0) {
			initItems();
		}
		adapter = new SettingListAdapter(getActivity(), items);
		mSettingList.setAdapter(adapter);
	}
	
	@OnItemClick(R.id.setting_list)
	public void clickCallback(int position) {
		System.out.println("position " + position);
		SettingData data = this.items.get(position);
		if(data.hasMoreContent) {
			getActivity().startActivity(data.targetIntent);
			getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_fade_out);
		}
		
		SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(MainActivity.PREVIOUS_TAB_PAGE, MainActivity.PAGE_SETTING);
		editor.commit();
	}
	
	class SettingListAdapter extends BaseAdapter{
		private List<SettingData> mItems;
		private LayoutInflater mInflater;
		
		public SettingListAdapter(Context context, List<SettingData> items){
			this.mItems = items;
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mViewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.setting_item, null);
				mViewHolder = new ViewHolder();
				mViewHolder.lblContent = ButterKnife.findById(convertView, R.id.setting_main_content);
				mViewHolder.lblSubContent = ButterKnife.findById(convertView, R.id.setting_sub_content);
				mViewHolder.imgView = ButterKnife.findById(convertView, R.id.setting_more);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder)convertView.getTag();
			}
			
			SettingData sd = mItems.get(position);
			mViewHolder.lblContent.setText(sd.settingName);
			if(mItems.get(position).hasSubContent) {
				mViewHolder.lblSubContent.setText(sd.subContent);
			}
			if(sd.hasMoreContent) {
				mViewHolder.imgView.setImageResource(sd.src);
			}
			return convertView;
		}
		
	}
	
	final class ViewHolder{
		TextView lblContent;
		TextView lblSubContent;
		ImageView imgView;
	}
	
}
