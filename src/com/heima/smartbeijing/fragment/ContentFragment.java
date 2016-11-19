package com.heima.smartbeijing.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.heima.smartbeijing.MainActivity;
import com.heima.smartbeijing.R;
import com.heima.smartbeijing.base.BasePager;
import com.heima.smartbeijing.base.impl.GovAffairsPager;
import com.heima.smartbeijing.base.impl.HomePager;
import com.heima.smartbeijing.base.impl.NewsCenterPager;
import com.heima.smartbeijing.base.impl.SettingPager;
import com.heima.smartbeijing.base.impl.SmartServicePager;
import com.heima.smartbeijing.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
/**
 * 主页面fragment，将五个标签页的布局对象添加到viewpager中
 * @author lenovo
 *
 */
public class ContentFragment extends BaseFragment {

	private View view;
	private NoScrollViewPager mViewPager;
	private RadioGroup rgGroup;
	private ArrayList<BasePager> mPagerList;

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.fragment_content, null);
		mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		return view;
	}
	//添加五个标签页到viewpager中
	@Override
	public void initData() {
		
		mPagerList = new ArrayList<BasePager>();
		
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsCenterPager(mActivity));
		mPagerList.add(new SmartServicePager(mActivity));
		mPagerList.add(new GovAffairsPager(mActivity));
		mPagerList.add(new SettingPager(mActivity));
		
		mViewPager.setAdapter(new ContentAdapter());
		//给底栏radioGroup中的radiobutton设置标签切换的监听事件
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home://首页
//					mViewPager.setCurrentItem(0);//设置viewpager的Item条目
					mViewPager.setCurrentItem(0, false);//参数2：表示是否具有滑动动画
					break;
				case R.id.rb_news://新闻中心
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_smart://智慧服务
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.rb_gov://政务
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.rb_setting://设置
					mViewPager.setCurrentItem(4, false);
					break;

				default:
					break;
				}
			}
		});
		//监听页面改变的方法
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			//页面被选中时调用该函数
			@Override
			public void onPageSelected(int position) {
				BasePager pager = mPagerList.get(position);
				pager.initData();//页面数据的加载
				
				if (position == 0|| position==mPagerList.size()-1) {
					//首页和设置页要禁用侧边栏
					setSlidingMenuEnable(false);
				}else {
					//其他页面开启侧边栏
					setSlidingMenuEnable(true);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
		//手动加载第一页数据
		mPagerList.get(0).initData();
		//首页禁用侧边栏
		setSlidingMenuEnable(false);
	}

	//开启或禁用侧边栏
	private void setSlidingMenuEnable(boolean enable) {
		// 获取侧边栏对象
		MainActivity mainUI=  (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	class ContentAdapter extends PagerAdapter{

		//item的数量
		@Override
		public int getCount() {
			return mPagerList.size();
		}

		//返回复用判断逻辑
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		//返回要显示的条目的内容，创建条目
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			View view = pager.mRootView;//获取当前页面对象的布局
			
//			pager.initData();//在此处初始化页面的数据，由于viewpager会加载两个页面，这样会导致流量消耗和性能下降,
							//为了解决该问题，可以监听页面的状态变化，如果页面被选中时，才加载数据
			container.addView(view);
			return view;
		}
		//销毁item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}


}
