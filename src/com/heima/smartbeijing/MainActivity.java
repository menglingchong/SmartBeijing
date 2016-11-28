package com.heima.smartbeijing;

import com.heima.smartbeijing.fragment.ContentFragment;
import com.heima.smartbeijing.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

/**
 * ������
 * @author lenovo
 *
 */
public class MainActivity extends SlidingFragmentActivity{

	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//ȫ������
//		slidingMenu.setBehindOffset(400);//��ĻԤ������
		
		//200/320 *��Ļ���
		WindowManager wm = getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width*200 / 320);
		
		initFragment();
	}

	//��ʼ��fragment
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		//��fragment�滻֡����,����3��fragment�ı�ǣ�ͨ����ǿ��Է����ҵ�fragment
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
		transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
		transaction.commit();//�ύ����
	}
	//��ȡ���������
	public LeftMenuFragment getLeftFragment(){
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment leftFragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
		return  leftFragment;
		
	}
	//��ȡContentFragment����
	public ContentFragment getContentFragment(){
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
		return contentFragment;
	}
}
