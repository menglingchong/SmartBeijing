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
 * 菜单详情页-新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener{
	
	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;
	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;
	
	private ArrayList<TabDetailPager> mDetailPagerList;//页签页面集合
	private ArrayList<NewsTabData> mTabData;//页签网络数据
	
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
		//初始化页签,根据网络返回的数据来初始化页签
		mDetailPagerList = new ArrayList<TabDetailPager>();
		
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity,mTabData.get(i));
			mDetailPagerList.add(pager);
		}
		
		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		mIndicator.setViewPager(mViewPager);//将viewpager和指示器绑定在一起，必须是在viewpager设置数据后
		//设置页面滑动监听
		mIndicator.setOnPageChangeListener(this);//此处必须给指示器设置页面监听，不能给viewpager设置页面监听
		
	}
	/*
	 * 创建pager的适配器
	 */
	class NewsMenuDetailAdapter extends PagerAdapter{

		//指定指示器标题
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
		//创建Pager条目
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mDetailPagerList.get(position);
			View view = pager.mRootView;
			pager.initData();//初始化数据
			container.addView(view);
			return view;
		}
		
		//销毁条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	//页面被选中时调用该函数
	@Override
	public void onPageSelected(int position) {
		System.out.println("当前位置："+position);
		if (position==0) {
			//开启侧边栏
			setSlidingMenuEnable(true);
		}else {
			//禁用侧边栏
			setSlidingMenuEnable(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	// 开启或禁用侧边栏
	private void setSlidingMenuEnable(boolean enable) {
		// 获取侧边栏对象
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
		//跳到下个页面
		int currentItem = mViewPager.getCurrentItem();
		currentItem++;
		mViewPager.setCurrentItem(currentItem);
	}

}
