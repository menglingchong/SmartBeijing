package com.heima.smartbeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/*
 * ����ͷ�����Զ���Viewpager
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
	 * 1.���»���ʱ��Ҫ���ؼ�����
	 * 2.���һ������ҵ�ǰ�ǵ�һ��ҳ����Ҫ���ؼ�����
	 * 3.���󻬶����ҵ�ǰ�����һ��ҳ����Ҫ���ؼ�����
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//�ӿؼ���ϣ�����ؼ��Լ����ȿؼ����ش����¼�ʱ����
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
				//���һ���
				if (dx>0) {
					//���һ������ҵ�ǰ�ǵ�һ��ҳ����Ҫ���ؼ�����
					if (currentItem == 0) {
						//��һ��ҳ����Ҫ���ؼ��������ش����¼�
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					//���󻬶����ҵ�ǰ�����һ��ҳ����Ҫ���ؼ�����
					int count = getAdapter().getCount();
					if (currentItem ==count-1) {
						//���һ��ҳ����Ҫ���ؼ���������
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {
				//���»���,���ؼ���Ҫ���ش����¼�
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
