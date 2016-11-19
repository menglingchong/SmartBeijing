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
 * ��ҳ��fragment���������ǩҳ�Ĳ��ֶ�����ӵ�viewpager��
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
	//��������ǩҳ��viewpager��
	@Override
	public void initData() {
		
		mPagerList = new ArrayList<BasePager>();
		
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsCenterPager(mActivity));
		mPagerList.add(new SmartServicePager(mActivity));
		mPagerList.add(new GovAffairsPager(mActivity));
		mPagerList.add(new SettingPager(mActivity));
		
		mViewPager.setAdapter(new ContentAdapter());
		//������radioGroup�е�radiobutton���ñ�ǩ�л��ļ����¼�
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home://��ҳ
//					mViewPager.setCurrentItem(0);//����viewpager��Item��Ŀ
					mViewPager.setCurrentItem(0, false);//����2����ʾ�Ƿ���л�������
					break;
				case R.id.rb_news://��������
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_smart://�ǻ۷���
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.rb_gov://����
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.rb_setting://����
					mViewPager.setCurrentItem(4, false);
					break;

				default:
					break;
				}
			}
		});
		//����ҳ��ı�ķ���
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			//ҳ�汻ѡ��ʱ���øú���
			@Override
			public void onPageSelected(int position) {
				BasePager pager = mPagerList.get(position);
				pager.initData();//ҳ�����ݵļ���
				
				if (position == 0|| position==mPagerList.size()-1) {
					//��ҳ������ҳҪ���ò����
					setSlidingMenuEnable(false);
				}else {
					//����ҳ�濪�������
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
		
		//�ֶ����ص�һҳ����
		mPagerList.get(0).initData();
		//��ҳ���ò����
		setSlidingMenuEnable(false);
	}

	//��������ò����
	private void setSlidingMenuEnable(boolean enable) {
		// ��ȡ���������
		MainActivity mainUI=  (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	class ContentAdapter extends PagerAdapter{

		//item������
		@Override
		public int getCount() {
			return mPagerList.size();
		}

		//���ظ����ж��߼�
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		//����Ҫ��ʾ����Ŀ�����ݣ�������Ŀ
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			View view = pager.mRootView;//��ȡ��ǰҳ�����Ĳ���
			
//			pager.initData();//�ڴ˴���ʼ��ҳ������ݣ�����viewpager���������ҳ�棬�����ᵼ���������ĺ������½�,
							//Ϊ�˽�������⣬���Լ���ҳ���״̬�仯�����ҳ�汻ѡ��ʱ���ż�������
			container.addView(view);
			return view;
		}
		//����item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}


}
