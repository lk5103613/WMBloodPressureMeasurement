package com.lichkin.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.lichkin.adapter.GuideViewPagerAdapter;
import com.lichkin.customview.BarViewPager;
import com.wm.activity.R;

public class GuideViewActivity extends ActionBarActivity implements
		View.OnClickListener, OnPageChangeListener {

	@InjectView(R.id.guide_pager)
	BarViewPager mViewPager;
	@InjectView(R.id.dots_container)
	LinearLayout dotsContainer;

	private GuideViewPagerAdapter mAdapter;
	List<View> mViews;
	private static final int[] pics = { R.drawable.change_name_img,
			R.drawable.add_device_img, R.drawable.bp_history_img,
			R.drawable.bs_result_img };
	private ImageView[] dots;
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_view);
		ButterKnife.inject(this);

		mViews = new ArrayList<View>();
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setImageResource(pics[i]);
			mViews.add(iv);
		}
		mAdapter = new GuideViewPagerAdapter(mViews);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		initDots();
	}

	@OnClick(R.id.guide_back)
	public void back(View v) {
		finish();
	}

	private void initDots() {
		dots = new ImageView[pics.length];

		// 循环取得小点图片
		for (int i = 0; i < pics.length; i++) {
			dots[i] = (ImageView) dotsContainer.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	/**
	 * 设置当前的引导页
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}
		mViewPager.setCurrentItem(position);
	}

	/**
	 * 设置当前引导小点的选中
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}
		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		currentIndex = positon;
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		setCurDot(position);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_right);
		}
	}

}
