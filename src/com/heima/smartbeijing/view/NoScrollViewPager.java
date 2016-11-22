package com.heima.smartbeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * ����������viewpager
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
		// ��д�˷���������ʱʲô��������ʵ�ֶԻ����¼��Ľ���
		return true;//�Զ���ؼ����ص�true
//		return super.onTouchEvent(ev);����ǿؼ�����չ���ص���uper.onTouchEvent(ev);
	}

	//�¼�����
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
}
