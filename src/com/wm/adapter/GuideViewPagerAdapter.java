package com.wm.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class GuideViewPagerAdapter extends PagerAdapter{
    
    //�����б�
    private List<View> views;
    
    public GuideViewPagerAdapter (List<View> views){
        this.views = views;
    }

    //����positionλ�õĽ���
    @Override
    public void destroyItem(View view, int position, Object arg2) {
        ((ViewPager) view).removeView(views.get(position));        
    }

    @Override
    public void finishUpdate(View view) {
        
    }

    //��õ�ǰ������
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }
    

    //��ʼ��positionλ�õĽ���
    @Override
    public Object instantiateItem(View view, int position) {
        ((ViewPager) view).addView(views.get(position), 0);
        return views.get(position);
    }

    //�ж��Ƿ��ɶ������ɽ���
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
        
    }

}