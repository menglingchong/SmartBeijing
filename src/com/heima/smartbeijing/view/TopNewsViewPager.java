package com.heima.smartbeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/*
 * 新闻头条的自定义Viewpager
 */
public class TopNewsViewPager extends ViewPager{

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context) {
		super(context);
	}

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/*
	 * 1.上下滑动时需要父控件拦截
	 * 2.向右滑动并且当前是第一个页面需要父控件拦截
	 * 3.向左滑动并且当前是最后一个页面需要父控件拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//子控件不希望父控件以及祖先控件拦截触摸事件时调用
		getParent().requestDisallowInterceptTouchEvent(true);
		
		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();
			
			int dx = endX- startX;
			int dy = endY- startY;
			
			if (Math.abs(dy) < Math.abs(dx)) {
				
				int currentItem = getCurrentItem();
				//左右滑动
				if (dx>0) {
					//向右滑动并且当前是第一个页面需要父控件拦截
					if (currentItem == 0) {
						//第一个页面需要父控件进行拦截触摸事件
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					//向左滑动并且当前是最后一个页面需要父控件拦截
					int count = getAdapter().getCount();
					if (currentItem ==count-1) {
						//最后一个页面需要父控件进行拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {
				//上下滑动,父控件需要拦截触摸事件
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	
}
