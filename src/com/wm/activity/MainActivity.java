package com.wm.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.adapter.IndexPagerAdapter;
import com.wm.customview.MyViewPager;
import com.wm.customview.PagerSlidingTitleIconTabStrip;
import com.wm.fragments.DeviceFragment;
import com.wm.fragments.DeviceFragment.OnStateChangeListener;
import com.wm.network.CheckNeedUploadTask;
import com.wm.utils.NetUtils;
import com.wm.utils.TabPager;

public class MainActivity extends BaseActivity implements
		OnStateChangeListener, OnPageChangeListener {

	// request code to open bluetooth
	public static int REQUEST_ENABLE_BT = 1;
	
	@InjectView(R.id.index_toolbar)
	Toolbar mToolbar;
	@InjectView(R.id.main_pager)
	MyViewPager mPager;
	@InjectView(R.id.main_tabs)
	PagerSlidingTitleIconTabStrip mTabs;

	private int mBackClickTimes = 0;
	private boolean mDeviceEdit = false;
	private IndexPagerAdapter mIndexPagerAdapter;
	private TabPager mTabPager;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		// 初始化Tab
		initTab();
		// 初始化全局参数
		mBackClickTimes = 0;
		mTabPager = TabPager.getInstance(mContext);
		
		int connectState = NetUtils.getConnectState(mContext);
		new CheckNeedUploadTask(mContext, connectState, mAlertDialog).execute();
	}

	/**
	 * 初始化tab
	 */
	private void initTab() {
		mIndexPagerAdapter = new IndexPagerAdapter(getSupportFragmentManager());
		mToolbar.setTitle("智慧医疗");
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
		int currentPage = mTabPager.getCurrentPage();
		if (currentPage != -1) {
			mTabPager.clear();
			mPager.setCurrentItem(currentPage);
		}
	}

	@Override
	public void onBackPressed() {
		if (mDeviceEdit) { // 如果设备列表处于编辑状态，点击返回时使设备列表返回普通状态
			DeviceFragment deviceFragment = mIndexPagerAdapter
					.getDeviceFragment();
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
		if (state == DeviceFragment.STATE_NORMAL) {
			this.mDeviceEdit = false;
		} else {
			this.mDeviceEdit = true;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		if (mDeviceEdit && position != 1) {
			mIndexPagerAdapter.getDeviceFragment().resetList();
		}
	}

}
