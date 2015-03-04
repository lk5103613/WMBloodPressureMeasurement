package com.wm.wmbloodpressuremeasurement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.astuetz.PagerSlidingTitleIconTabStrip;
import com.wm.adapter.IndexPagerAdapter;

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

		if (currentPage != -1) {
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt(PREVIOUS_TAB_PAGE, -1);
			editor.commit();
			mPager.setCurrentItem(currentPage);
		}

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {

		// // Inflate the menu; this adds items to the action bar if it is
		// present.
		// MenuInflater inflater2 = getMenuInflater();

//		getLayoutInflater().setFactory(new Factory() {
//
//			@Override
//			public View onCreateView(String name, Context context,
//					AttributeSet attrs) {
//				System.out.println(name);
//				
//				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")
//						|| name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView")) {
//					try {
//						LayoutInflater f = getLayoutInflater();
//						final View view = f.createView(name, null, attrs);
//						
//						System.out.println((view instanceof TextView));
//						
//						if (view instanceof TextView) {
//							((TextView) view).setTextColor(Color.GREEN);
//						}
//						return view;
//					} catch (InflateException e) {
//						e.printStackTrace();
//					} catch (ClassNotFoundException e) {
//						e.printStackTrace();
//					}
//				}
//				return null;
//			}
//
//		});
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
