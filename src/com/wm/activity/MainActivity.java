package com.wm.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.adapter.IndexPagerAdapter;
import com.wm.customview.MyViewPager;
import com.wm.customview.PagerSlidingTitleIconTabStrip;
import com.wm.fragments.DeviceFragment;
import com.wm.fragments.DeviceFragment.OnStateChangeListener;

public class MainActivity extends ActionBarActivity implements OnStateChangeListener, OnPageChangeListener {
	
	public static String PREVIOUS_TAB_PAGE = "previous_page";
	public static String SP_NAME = "prefs_page";
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
	private boolean mDeviceEdit = false;
	private IndexPagerAdapter mIndexPagerAdapter;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		
		mSharePref = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mContext = MainActivity.this;
		mBackClickTimes = 0;
		
		mIndexPagerAdapter = new IndexPagerAdapter(getSupportFragmentManager());
		mToolbar.setTitle("智慧医疗血压仪");
		setSupportActionBar(mToolbar);
		mPager.setAdapter(mIndexPagerAdapter);
		mTabs.setOnPageChangeListener(this);
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
		if(mDeviceEdit) {
			DeviceFragment deviceFragment = mIndexPagerAdapter.getDeviceFragment();
			deviceFragment.resetList();
			return;
		}
		// 连续点击两次返回键，退出程序
		if (mBackClickTimes == 0) {
			String str = getResources().getString(R.string.ask_when_exit);
			Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
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
	public void onStateChange(int state) {
		if(state == DeviceFragment.STATE_NORMAL) {
			this.mDeviceEdit = false;
		} else {
			this.mDeviceEdit = true;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		if(mDeviceEdit && position != 1) {
			mIndexPagerAdapter.getDeviceFragment().resetList();
		}
	}
	
}
