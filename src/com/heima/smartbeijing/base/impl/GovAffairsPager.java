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
	
	//初始化数据
	public void initData(){
		System.out.println("政务初始化啦。。。");
		
		//给帧布局填充布局对象
		TextView view = new TextView(mActivity);
		view.setText("政务");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
		
		//修改页面标签
		tvTitle.setText("政务");
		//隐藏菜单按钮
//		btnMenu.setVisibility(View.GONE);
		
	}
}
