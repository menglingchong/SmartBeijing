package com.heima.smartbeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.heima.smartbeijing.base.BasePager;

public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity activity) {
		super(activity);
	}
	
	//��ʼ������
	public void initData(){
		System.out.println("�����ʼ����������");
		
		//��֡������䲼�ֶ���
		TextView view = new TextView(mActivity);
		view.setText("����");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
		
		//�޸�ҳ���ǩ
		tvTitle.setText("����");
		//���ز˵���ť
//		btnMenu.setVisibility(View.GONE);
		
	}
}
