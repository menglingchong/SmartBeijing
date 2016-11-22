package com.heima.smartbeijing.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heima.smartbeijing.MainActivity;
import com.heima.smartbeijing.base.BaseMenuDetailPager;
import com.heima.smartbeijing.base.BasePager;
import com.heima.smartbeijing.base.impl.menu.InteractMenuDetailPager;
import com.heima.smartbeijing.base.impl.menu.NewsMenuDetailPager;
import com.heima.smartbeijing.base.impl.menu.PhotosMenuDetailPager;
import com.heima.smartbeijing.base.impl.menu.TopicMenuDetailPager;
import com.heima.smartbeijing.domain.NewsMenu;
import com.heima.smartbeijing.fragment.LeftMenuFragment;
import com.heima.smartbeijing.global.GlobalConstances;
import com.heima.smartbeijing.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mMenuDetailPagerList;//菜单详情页集合
	private NewsMenu newsData;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}
	
	//初始化数据
	public void initData(){
		System.out.println("新闻中心初始化啦。。。");
		
		//给帧布局填充布局对象
//		TextView view = new TextView(mActivity);
//		view.setText("新闻中心");
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
//		flContent.addView(view);
		
		//修改页面标签
		tvTitle.setText("新闻中心");
		
		//先判断有没有缓存，如果有缓存直接解析缓存的数据
		String cache = CacheUtils.getCache(mActivity, GlobalConstances.CATEGORY_URL);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("发现缓存。。。");
			processData(cache);
		} 
		
		//从服务器获取数据
		//使用开源框架
		getDataFromServer();//有没有缓存都要从服务器获取数据，好处是提高界面的友好效果,可以让用户第一时间看到数据
	}

	private void getDataFromServer() {
		// 从服务器获取数据
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstances.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// 请求成功
				String result = responseInfo.result;
				System.out.println("服务器返回给的数据："+result);
				
				//解析数据,使用Gson框架
				processData(result);
				//写缓存
				CacheUtils.setCache(mActivity, GlobalConstances.CATEGORY_URL, result);
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//  请求失败
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 0).show();
			}
		});
		
	}

	//解析数据
	protected void processData(String json) {
		
		Gson gson = new Gson();
		newsData = gson.fromJson(json, NewsMenu.class);
		
		System.out.println("解析结果："+newsData);
		//将解析的数据传给侧边栏,先要拿到侧边栏对象
		MainActivity mainUI = (MainActivity) mActivity;
		LeftMenuFragment leftFragment = mainUI.getLeftFragment();
		//给侧边栏设置数据
		leftFragment.setMenuData(newsData.data);
		//初始化4个菜单详情页
		mMenuDetailPagerList = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagerList.add(new NewsMenuDetailPager(mActivity,newsData.data.get(0).children));
		mMenuDetailPagerList.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagerList.add(new PhotosMenuDetailPager(mActivity));
		mMenuDetailPagerList.add(new InteractMenuDetailPager(mActivity));
		
		//将新闻菜单详情页设置为默认页面
		setCurrentDetailPager(0);
	}

	//设置菜单详情页，即修改新闻中的FrameLayou的内容
	public void setCurrentDetailPager(int position) {
		//重新给frameLayout添加内容
		BaseMenuDetailPager pager = mMenuDetailPagerList.get(position);
		View view = pager.mRootView;//当前页面的布局
		//清除之前的旧布局
		flContent.removeAllViews();
		
		flContent.addView(view);//给帧布局添加布局对象
		pager.initData();//初始化页面数据
		//更新标题
		tvTitle.setText(newsData.data.get(position).title);
	}
}
