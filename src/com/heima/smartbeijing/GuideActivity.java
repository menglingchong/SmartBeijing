package com.heima.smartbeijing;

import java.util.ArrayList;

import com.heima.smartbeijing.utils.DensityUtils;
import com.heima.smartbeijing.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 新手引导界面
 * @author lenovo
 *
 */
public class GuideActivity extends Activity{
	
	private ViewPager mViewPager;
	
	//引导页图片id数组
	private int[] mImageIds = new int[]{R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3};

	private ArrayList<ImageView> mImageViewList;

	private LinearLayout llContainer;

	private ImageView ivRedPoint;

	private Button btnStart;

	private int mPointDis;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
	
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		llContainer = (LinearLayout) findViewById(R.id.ll_container);
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		btnStart = (Button) findViewById(R.id.btn_start);
		initData();//初始化数据
		
		mViewPager.setAdapter(new GuideAdapter());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// 某个页面被选中时调用
				//当第三个页面被选中的时候，显示出Buttton按钮
				if (position == mImageViewList.size()-1) {
					btnStart.setVisibility(View.VISIBLE);
				} else {
					btnStart.setVisibility(View.INVISIBLE);					
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// 滑动页面时调用,页面滑动过程中被调用
				System.out.println("当前的位置： "+position +",当前移动偏移的百分比："+positionOffset);
				//更新小红点的距离
				int leftMargin = (int) (mPointDis*positionOffset) +position* mPointDis ;
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
				params.leftMargin= leftMargin;//修改左边距
				
				//重新设置小红点的布局参数
				ivRedPoint.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// 页面被改变时调用
				
			}
		});
		
		//计算两个圆点的距离
		//移动距离= 第二个圆点左边距-第一个圆点左边距
		//View 的绘制流程 measure->layout ->draw（activity的onCreat方法执行结束之后才会走此流程）
//		mPointDis = llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();
		
		//监听layout方法结束的事件，确定好位置之后再获取圆点距离
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				//移除监听，避免重复回调
				ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				//layout方法执行结束的回调
				mPointDis = llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();
				System.out.println("两个圆点之间的距离："+mPointDis);
			}
		});
		//监听Button按钮的点击事件
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转到主页面并且更新sp将isFirstEnter设置为false
				PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", false);
				
				//跳转到主页面
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
	}
	//初始化数据
	private void initData() {
		mImageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			//初始化要显示的图片对象
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]);//通过设置背景让宽高填充布局
			mImageViewList.add(imageView);
			
			//初始化小圆点指示器
			ImageView pointView = new ImageView(this);
			pointView.setImageResource(R.drawable.shape_point_gray);
			//初始化布局参数，宽高包裹内容，父控件是谁，就声明谁的布局参数
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		
			if (i>0) {
				//从第二个点开始设置左边距
				params.leftMargin=DensityUtils.dip2px(this, 10);
			}
			//设置小圆点的布局
			pointView.setLayoutParams(params);
			llContainer.addView(pointView);
		}
		
	}

	public class GuideAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			//item的个数
			return mImageViewList.size();
		}
		//返回复用判断逻辑，
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		//返回要显示的条目的内容，创建条目
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageViewList.get(position);
			container.addView(view);//吧view对象添加到container中
			return view;//将view对象返回给适配器（此处必须重写，否则报异常）
		}
		//销毁条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
	 
}
