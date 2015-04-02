package com.wm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.activity.LoginActivity;
import com.wm.activity.R;
import com.wm.customview.ImageTextView;

public class IndexFragment extends Fragment {
	
	@InjectView(R.id.bp_image_text)
	ImageTextView mImage;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);// ÏÔÊ¾fragmentµÄmenu
		View view = inflater.inflate(R.layout.fragment_index, container, false);
		ButterKnife.inject(this, view);
		
		mImage.startRotate();
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		 super.onCreateOptionsMenu(menu, inflater);
         menu.add("µÇÂ¼").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}
	
}
