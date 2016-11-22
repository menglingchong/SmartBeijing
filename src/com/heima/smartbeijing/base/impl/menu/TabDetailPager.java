package com.heima.smartbeijing.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heima.smartbeijing.R;
import com.heima.smartbeijing.base.BaseMenuDetailPager;
import com.heima.smartbeijing.domain.NewsMenu.NewsTabData;
import com.heima.smartbeijing.domain.NewsTabBean;
import com.heima.smartbeijing.domain.NewsTabBean.NewsData;
import com.heima.smartbeijing.domain.NewsTabBean.TopNews;
import com.heima.smartbeijing.global.GlobalConstances;
import com.heima.smartbeijing.utils.CacheUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

public class TabDetailPager extends BaseMenuDetailPager {

	private  NewsTabData mTabData;//单个页签的网络数据
//	private TextView view;
	@ViewInject(R.id.vp_top_news)
	private ViewPager mViewPager;
	@ViewInject(R.id.tv_title)
	private TextView mTitle;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;
	@ViewInject(R.id.lv_list)
	private ListView lvList;
	
	private String mUrl;//网络连接
	private ArrayList<TopNews> mTopNews;
	private ArrayList<NewsData> mNewsDataList;
	
	
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;
		
		mUrl = GlobalConstances.SERVER_URL+ mTabData.url;
	}

	@Override
	public View initView() {
//		view = new TextView(mActivity);
//		view.setText(mTabData.title);//此处空指针异常,代码先执行super(actiivty),但是会先初始化view
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
//		return view;
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);//注册控件等价于findviewbyId()
		
		//给listView添加头布局
		View mHeaderView = view.inflate(mActivity,R.layout.list_item_header, null);
		ViewUtils.inject(this, mHeaderView);//此处必须将头布局注入
		lvList.addHeaderView(mHeaderView);		
		return view;
	}
	@Override
	public void initData() {
//		view.setText(mTabData.title);
		//请求的网络数据是否进行了缓存
		String cache = CacheUtils.getCache(mActivity, mUrl);
		if (! TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		getDataFromServer();
	}
	/*
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//请求成功
				String result = responseInfo.result;
				processData(result);
				//添加缓存
				CacheUtils.setCache(mActivity, mUrl, result);
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
		NewsTabBean newsTabBean = gson.fromJson(json, NewsTabBean.class);
		
		//头条新闻填充数据
		mTopNews = newsTabBean.data.topnews;
		
		if (mTopNews != null) {
			mViewPager.setAdapter(new TopNewsAdapter()); 
			
			mIndicator.setViewPager(mViewPager);			
			mIndicator.setSnap(true);//以快照的方式展示
			
			//给头条新闻设置标题
			mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
				//页面选中的时候调用该方法
				@Override
				public void onPageSelected(int position) {
					//更新头条新闻标题
					String topNewsTitle = mTopNews.get(position).title;
					mTitle.setText(topNewsTitle);
				}
				
				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
				}
				
				@Override
				public void onPageScrollStateChanged(int state) {
					
				}
			});
			
			//更新第一个头条新闻的标题
			mTitle.setText(mTopNews.get(0).title);
			//默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
			mIndicator.onPageSelected(0);
		}
		
		//新闻列表的填充数据
		mNewsDataList = newsTabBean.data.news;
		if (mNewsDataList != null) {
			lvList.setAdapter(new NewsAdapter());
		}
		
	}

	//头条新闻的数据适配器
	class TopNewsAdapter extends PagerAdapter{

		private BitmapUtils mBitmapUtils;
		
		public TopNewsAdapter(){
			mBitmapUtils = new BitmapUtils(mActivity);
			//设置加载中的默认图片
		    mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}
		@Override
		public int getCount() {
			return mTopNews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		} 
		//创建pager页面条目
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
//			view.setImageResource(R.drawable.topnews_item_default);
			view.setScaleType(ScaleType.FIT_XY);//设置图片的缩放格式,宽高填充父控件
			
			String imageUrl =GlobalConstances.SERVER_URL+ mTopNews.get(position).topimage;//图片的url地址
			//下载图片-将图片设置给imageView-避免内存溢出-使用缓存加载图盘
			//BitmapUtils - Xutils
			mBitmapUtils.display(view, imageUrl);
			container.addView(view);
			return  view;
		}
		//销毁页面条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			 container.removeView((View)object);
		}
	}
	
	//新闻列表的数据适配器
	class NewsAdapter extends BaseAdapter{

		private BitmapUtils mBitmapUtils;

		public NewsAdapter(){
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadFailedImage(R.drawable.news_pic_default);//默认加载的图片
		}
		@Override
		public int getCount() {
			return mNewsDataList.size();
		}

		@Override
		public NewsData getItem(int position) {
			return mNewsDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_news, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvDate= (TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			//给item条目设置数据
			NewsData news = getItem(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);
			//加载图片使用BitmapUtils第三方库
			mBitmapUtils.display(holder.ivIcon, GlobalConstances.SERVER_URL+news.listimage);
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
	}

}
