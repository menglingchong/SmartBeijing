package com.heima.smartbeijing.base;

import com.heima.smartbeijing.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/*
 * 五个标签页的基类
 */
public class BasePager {

	public Activity mActivity;
	public TextView tvTitle;
	public ImageButton btnMenu;
	public FrameLayout flContent; //当前帧布局对象
	public View mRootView; //当前页面布局对象
	
	public BasePager(Activity activity){
		mActivity =activity;
		mRootView = initView();
		
	}
	//初始化view布局
	public View initView(){
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		
		return view;
	}
	//初始化数据
	public void initData(){
		
	}
}
