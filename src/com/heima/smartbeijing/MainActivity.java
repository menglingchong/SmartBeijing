package com.heima.smartbeijing;

import com.heima.smartbeijing.fragment.ContentFragment;
import com.heima.smartbeijing.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

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
		slidingMenu.setBehindOffset(200);//��ĻԤ������
		
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
}
