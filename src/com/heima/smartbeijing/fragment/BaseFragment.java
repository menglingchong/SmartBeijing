package com.heima.smartbeijing.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 创建fragment的基类
 * @author lenovo
 *
 */
public abstract class BaseFragment extends Fragment {

	public Activity mActivity;

	//Fragment的创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();//获取fragment所依赖的activity
	}
	//初始化fragment的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= initView();
		return view;
	}
	//fragment所依赖的activity的oncreat方法执行结束
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//初始化数据
		initData();
	}
	//初始化数据，必须由子类实现
	public abstract void initData();
		
	//初始化布局，必须由子类实现
	public abstract View initView();
}
