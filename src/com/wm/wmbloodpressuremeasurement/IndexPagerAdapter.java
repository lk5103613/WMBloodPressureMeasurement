package com.wm.wmbloodpressuremeasurement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.wm.fragments.DeviceFragment;
import com.wm.fragments.IndexFragment;
import com.wm.fragments.SettingFragment;

public class IndexPagerAdapter extends FragmentStatePagerAdapter  {

	public IndexPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	private Fragment[] mFragments = new Fragment[]{new IndexFragment(), new DeviceFragment(), new SettingFragment()};
	private String[] mTitles = new String[]{"首页", "设备", "设置"};
	private int[] mIcons = new int[]{R.drawable.ic_action_favorite, R.drawable.ic_action_phone, R.drawable.ic_action_settings};

	@Override
	public Fragment getItem(int index) {
		return mFragments[index];
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return  mTitles[position];
	}
	
	@Override
	public int getCount() {
		return 3;
	}

	

}
