package com.wm.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.adapter.IndexPagerAdapter;
import com.wm.customview.MyViewPager;
import com.wm.customview.PagerSlidingTitleIconTabStrip;

public class MainActivity extends ActionBarActivity {
	
	public static String PREVIOUS_TAB_PAGE = "previous_page";
	public static int PAGE_SETTING = 2;
	public static int PAGE_DEVICE = 1;
	public static int PAGE_HOME = 0;
	
	@InjectView(R.id.index_toolbar)
	Toolbar mToolbar;
	@InjectView(R.id.main_pager)
	MyViewPager mPager;
	@InjectView(R.id.main_tabs)
	PagerSlidingTitleIconTabStrip mTabs;
	
	private SharedPreferences mSharePref;
	private int mBackClickTimes = 0;
	private Context mContext;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		
		mSharePref = getPreferences(Context.MODE_PRIVATE);
		mContext = MainActivity.this;
		mBackClickTimes = 0;
		
		mToolbar.setTitle("智慧医疗血压仪");
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
		int currentPage = mSharePref.getInt(PREVIOUS_TAB_PAGE, -1);
		if(currentPage != -1) {
			clearPageInfo(mSharePref);
			mPager.setCurrentItem(currentPage);
		}
	}
	
	private void clearPageInfo(SharedPreferences sharedPref) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(PREVIOUS_TAB_PAGE, -1);
		editor.commit();
	}
	
	@Override
	public void onBackPressed() {
		// 连续点击两次返回键，退出程序
		if (mBackClickTimes == 0) {
			String str = getResources().getString(R.string.ask_when_exit);
			Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
			mBackClickTimes = 1;
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						mBackClickTimes = 0;
					}
				}
			}.start();
			return;
		} else {
			finish();
			System.exit(0);
		}
	}
	
	@Override
	protected void onDestroy() {
		System.out.println("main activity destory");
		super.onDestroy();
	}
}
