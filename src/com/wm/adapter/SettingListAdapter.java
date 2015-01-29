package com.wm.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;

import com.wm.entity.SettingData;
import com.wm.wmbloodpressuremeasurement.R;

public class SettingListAdapter extends RecyclerView.Adapter<SettingListAdapter.ViewHolder> {
	
	private List<SettingData> mItems;
	
	public SettingListAdapter(List<SettingData> items) {
		this.mItems = items;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		
		List<SettingData> items;
		
		TextView lblContent;
		TextView lblSubContent;
		ImageView imgBtn;

		public ViewHolder(View itemView, List<SettingData> items) {
			super(itemView);
			this.items = items;
			lblContent = ButterKnife.findById(itemView, R.id.setting_main_content);
			lblSubContent = ButterKnife.findById(itemView, R.id.setting_sub_content);
			imgBtn = ButterKnife.findById(itemView, R.id.setting_more);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			SettingData data = items.get(getPosition());
			if(data.hasMoreContent) {
				data.context.startActivity(data.targetIntent);
			}
		}
		
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		SettingData sd = mItems.get(position);
		viewHolder.lblContent.setText(sd.settingName);
		if(mItems.get(position).hasSubContent) {
			viewHolder.lblSubContent.setText(sd.subContent);
		}
		if(sd.hasMoreContent) {
			viewHolder.imgBtn.setImageResource(sd.src);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.setting_item, viewGroup, false);
		ViewHolder vh = new ViewHolder(view, this.mItems);
		return vh;
	}

}