package com.heima.smartbeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 不允许滑动的viewpager
 * @author lenovo
 *
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context) {
		super(context);
	}

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 重写此方法，触摸时什么都不做，实现对滑动事件的禁用
		return true;//自定义控件返回的true
//		return super.onTouchEvent(ev);如果是控件的扩展返回的是uper.onTouchEvent(ev);
	}

	
}
