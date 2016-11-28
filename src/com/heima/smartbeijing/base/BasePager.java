package com.heima.smartbeijing.base;

import com.heima.smartbeijing.MainActivity;
import com.heima.smartbeijing.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/*
 * �����ǩҳ�Ļ���
 */
public class BasePager {

	public Activity mActivity;
	public TextView tvTitle;
	public ImageButton btnMenu;
	public FrameLayout flContent; //��ǰ֡���ֶ���
	public View mRootView; //��ǰҳ�沼�ֶ���
	public ImageButton btnPhoto;//��ͼ���л���ť
	
	public BasePager(Activity activity){
		mActivity =activity;
		mRootView = initView();
		
	}
	//��ʼ��view����
	public View initView(){
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
		
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity mainUI = (MainActivity) mActivity;
				SlidingMenu slidingMenu = mainUI.getSlidingMenu();
				slidingMenu.toggle();//���״̬�ǿ������ú�رգ���֮��Ȼ
			}
		});
		return view;
	}
	//��ʼ������
	public void initData(){
		
	}
}
