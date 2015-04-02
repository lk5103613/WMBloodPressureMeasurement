package com.wm.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.adapter.IndexPagerAdapter;
import com.wm.customview.CustomTabView;
import com.wm.customview.MenuDialog;
import com.wm.customview.MyViewPager;
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
	@InjectView(R.id.main_tab)
	CustomTabView mTabView;

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
		// ��ʼ��Tab
		initTab();
		// ��ʼ��ȫ�ֲ���
		mBackClickTimes = 0;
		mTabPager = TabPager.getInstance(mContext);
		mTabView.setViewPager(mPager);
		mTabView.setOnPageChangeListener(this);
		
		int connectState = NetUtils.getConnectState(mContext);
		new CheckNeedUploadTask(mContext, connectState, mAlertDialog).execute();
		
	}

	/**
	 * ��ʼ��tab
	 */
	private void initTab() {
		mIndexPagerAdapter = new IndexPagerAdapter(getSupportFragmentManager());
		mToolbar.setTitle("�ǻ�ҽ��");
		setSupportActionBar(mToolbar);
		mPager.setAdapter(mIndexPagerAdapter);
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
		if (mDeviceEdit) { // ����豸�б��ڱ༭״̬���������ʱʹ�豸�б�����ͨ״̬
			DeviceFragment deviceFragment = mIndexPagerAdapter
					.getDeviceFragment();
			deviceFragment.resetList();
			return;
		}
		// ����������η��ؼ����˳�����
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
