package com.heima.smartbeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.heima.smartbeijing.base.BasePager;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}
	
	//��ʼ������
	public void initData(){
		System.out.println("�ǻ۷����ʼ����������");
		
		//��֡������䲼�ֶ���
		TextView view = new TextView(mActivity);
		view.setText("�ǻ۷���");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
		
		//�޸�ҳ���ǩ
		tvTitle.setText("�ǻ۷���");
		//���ز˵���ť
//		btnMenu.setVisibility(View.GONE);
		
	}
}
