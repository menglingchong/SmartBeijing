package com.heima.smartbeijing.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.heima.smartbeijing.MainActivity;
import com.heima.smartbeijing.R;
import com.heima.smartbeijing.base.BaseMenuDetailPager;
import com.heima.smartbeijing.domain.NewsMenu.NewsTabData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
/*
 * �˵�����ҳ-����
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener{
	
	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;
	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;
	
	private ArrayList<TabDetailPager> mDetailPagerList;//ҳǩҳ�漯��
	private ArrayList<NewsTabData> mTabData;//ҳǩ��������
	
	public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		mTabData=children;
	}

	@Override
	public View initView() {
		 View view = View.inflate(mActivity, R.layout.pager_news_menu_detail,null);
		 ViewUtils.inject(this, view);
		 return view;
	}
	@Override
	public void initData() {
		//��ʼ��ҳǩ,�������緵�ص���������ʼ��ҳǩ
		mDetailPagerList = new ArrayList<TabDetailPager>();
		
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity,mTabData.get(i));
			mDetailPagerList.add(pager);
		}
		
		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		mIndicator.setViewPager(mViewPager);//��viewpager��ָʾ������һ�𣬱�������viewpager�������ݺ�
		//����ҳ�滬������
		mIndicator.setOnPageChangeListener(this);//�˴������ָʾ������ҳ����������ܸ�viewpager����ҳ�����
		
	}
	/*
	 * ����pager��������
	 */
	class NewsMenuDetailAdapter extends PagerAdapter{

		//ָ��ָʾ������
		@Override
		public CharSequence getPageTitle(int position) {
			NewsTabData data = mTabData.get(position);
			return data.title;
		}
		
		@Override
		public int getCount() {
			return mTabData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		//����Pager��Ŀ
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mDetailPagerList.get(position);
			View view = pager.mRootView;
			pager.initData();//��ʼ������
			container.addView(view);
			return view;
		}
		
		//������Ŀ
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	//ҳ�汻ѡ��ʱ���øú���
	@Override
	public void onPageSelected(int position) {
		System.out.println("��ǰλ�ã�"+position);
		if (position==0) {
			//���������
			setSlidingMenuEnable(true);
		}else {
			//���ò����
			setSlidingMenuEnable(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	// ��������ò����
	private void setSlidingMenuEnable(boolean enable) {
		// ��ȡ���������
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	@OnClick(R.id.btn_next)
	public void nextPage(View view){
		//�����¸�ҳ��
		int currentItem = mViewPager.getCurrentItem();
		currentItem++;
		mViewPager.setCurrentItem(currentItem);
	}

}
