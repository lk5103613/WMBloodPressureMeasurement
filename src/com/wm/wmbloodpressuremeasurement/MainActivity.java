package com.wm.wmbloodpressuremeasurement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends ActionBarActivity {
	
	@InjectView(R.id.index_toolbar)
	Toolbar mToolbar;
	@InjectView(R.id.main_pager)
	ViewPager mPager;
	@InjectView(R.id.main_tabs)
	PagerSlidingTabStrip mTabs;
	
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
