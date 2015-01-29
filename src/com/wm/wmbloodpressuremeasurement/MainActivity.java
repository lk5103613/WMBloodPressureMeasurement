package com.wm.wmbloodpressuremeasurement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.astuetz.PagerSlidingTitleIconTabStrip;
import com.wm.adapter.IndexPagerAdapter;

public class MainActivity extends ActionBarActivity {
	
	public static String PREVIOUS_TAB_PAGE = "previous_page";
	public static int PAGE_SETTING = 3;
	public static int PAGE_DEVICE = 2;
	public static int PAGE_HOME = 1;
	
	@InjectView(R.id.index_toolbar)
	Toolbar mToolbar;
	@InjectView(R.id.main_pager)
	MyViewPager mPager;
	@InjectView(R.id.main_tabs)
	PagerSlidingTitleIconTabStrip mTabs;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		mToolbar.setTitle("ÖÇ»ÛÒ½ÁÆÑªÑ¹ÒÇ");
		
		setSupportActionBar(mToolbar);
		mPager.setAdapter(new IndexPagerAdapter(getSupportFragmentManager()));
		mTabs.setShouldExpand(true);
		mTabs.setViewPager(mPager);
		mTabs.setIndicatorColorResource(R.color.colorPrimary);
		mTabs.setTextColorResource(R.color.colorPrimary);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		int currentPage = sharedPref.getInt(PREVIOUS_TAB_PAGE, -1);
		if(currentPage != -1) {
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt(PREVIOUS_TAB_PAGE, -1);
			editor.commit();
			mPager.setCurrentItem(currentPage);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
