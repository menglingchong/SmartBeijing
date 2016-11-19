package com.heima.smartbeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.heima.smartbeijing.base.BasePager;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}
	
	//��ʼ������
	public void initData(){
		System.out.println("��ҳ��ʼ����������");
		
		//��֡������䲼�ֶ���
		TextView view = new TextView(mActivity);
		view.setText("��ҳ");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
		
		//�޸�ҳ���ǩ
		tvTitle.setText("�ǻ۱���");
		//���ز˵���ť
		btnMenu.setVisibility(View.GONE);
		
	}
}
