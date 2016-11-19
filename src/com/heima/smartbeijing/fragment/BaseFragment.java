package com.heima.smartbeijing.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * ����fragment�Ļ���
 * @author lenovo
 *
 */
public abstract class BaseFragment extends Fragment {

	public Activity mActivity;

	//Fragment�Ĵ���
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();//��ȡfragment��������activity
	}
	//��ʼ��fragment�Ĳ���
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= initView();
		return view;
	}
	//fragment��������activity��oncreat����ִ�н���
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//��ʼ������
		initData();
	}
	//��ʼ�����ݣ�����������ʵ��
	public abstract void initData();
		
	//��ʼ�����֣�����������ʵ��
	public abstract View initView();
}
