package com.heima.smartbeijing.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.heima.smartbeijing.utils.PrefUtils;
import com.heima.smartbeijing.view.PullToRefreshListView;
import com.heima.smartbeijing.view.PullToRefreshListView.OnRefreshListenr;
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

	private  NewsTabData mTabData;//����ҳǩ����������
//	private TextView view;
	@ViewInject(R.id.vp_top_news)
	private ViewPager mViewPager;
	@ViewInject(R.id.tv_title)
	private TextView mTitle;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView lvList;
	
	private String mUrl;//��������
	private ArrayList<TopNews> mTopNews;
	private ArrayList<NewsData> mNewsDataList;
	private String mMoreUrl;
	private NewsAdapter mNewsAdapter;
	
	
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;
		
		mUrl = GlobalConstances.SERVER_URL+ mTabData.url;
	}

	@Override
	public View initView() {
//		view = new TextView(mActivity);
//		view.setText(mTabData.title);//�˴���ָ���쳣,������ִ��super(actiivty),���ǻ��ȳ�ʼ��view
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
//		return view;
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);//ע��ؼ��ȼ���findviewbyId()
		
		//��listView���ͷ����
		View mHeaderView = view.inflate(mActivity,R.layout.list_item_header, null);
		ViewUtils.inject(this, mHeaderView);//�˴����뽫ͷ����ע��
		lvList.addHeaderView(mHeaderView);		
		
		//ǰ�˽������ûص�
		lvList.setOnRefreshListener(new OnRefreshListenr() {
			
			@Override
			public void onRefresh() {
				//ˢ������
				getDataFromServer();
				
				Toast.makeText(mActivity, "��������ˢ�³ɹ���", 0).show();
			}

			@Override
			public void onLoadMore() {
				//��������
				//�ж���û����һҳ���ݵ�url
				if (mMoreUrl != null) {
					//������һҳ����
					getMoreDataFromServer();
				}else {
					Toast.makeText(mActivity, "û�и��������ˡ�����", 0).show();
					//û������ʱҲҪ����ؼ�
					lvList.onRefreshComplete(false);
				}
			}
		});
		
		//ListView�ĵ���¼�
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int headerViewsCount = lvList.getHeaderViewsCount();//��ȡͷ���ֵ�����
				position = position- headerViewsCount;
				System.out.println("Item "+ position+" ������ˣ���");
				
				//��Ǳ������item,ʹ��sharedpreference����
				// read_ids:1001,1002,1003
				NewsData newsData = mNewsDataList.get(position);
				String readIds = PrefUtils.getString(mActivity, "read_ids", "");
				
				if (! readIds.contains(newsData.id +"") ) {//ֻ�в����������Ŀ��idʱ��׷�ӣ�������׷��
					
					readIds = readIds + newsData.id + ",";
					PrefUtils.setString(mActivity, "read_ids", readIds);
				}
				//���������item��������ɫ��Ϊ��ɫ�����ֲ�ˢ�£�
				TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
				tvTitle.setTextColor(Color.GRAY);
				
//				mNewsAdapter.notifyDataSetChanged();//ȫ��ˢ��
			}
		});
		return view;
	}

	@Override
	public void initData() {
//		view.setText(mTabData.title);
		//��������������Ƿ�����˻���
		String cache = CacheUtils.getCache(mActivity, mUrl);
		if (! TextUtils.isEmpty(cache)) {
			processData(cache,false);
		}
		getDataFromServer();
	}
	/*
	 * �ӷ�������ȡ����
	 */
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//����ɹ�
				String result = responseInfo.result;
				processData(result , false);
				//��ӻ���
				CacheUtils.setCache(mActivity, mUrl, result);
				
				//�������ˢ�£���������ˢ�ؼ�
				lvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//  ����ʧ��
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 0).show();
				
				//δ�������ˢ�£���������ˢ�ؼ�
				lvList.onRefreshComplete(false);
			}
		});
		
	}
	/*
	 * �ӷ�����������һҳ����
	 */
	protected void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//����ɹ�
				String result = responseInfo.result;
				processData(result,true);
				
				//����������أ�����ؼ�
				lvList.onRefreshComplete(false);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//  ����ʧ��
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 0).show();
				
				//δ����������أ�����ؼ�
				lvList.onRefreshComplete(false);
			}
		});
		
	}
	//��������
	protected void processData(String json,boolean isMore) {
		Gson gson = new Gson();
		NewsTabBean newsTabBean = gson.fromJson(json, NewsTabBean.class);
		
		String moreUrl = newsTabBean.data.more;
		if (!TextUtils.isEmpty(moreUrl)) {
			//�������ݵ�url
			mMoreUrl = GlobalConstances.SERVER_URL+ moreUrl;
		}else {
			mMoreUrl = null; 
		} 
		
		if (!isMore) {
			
			//ͷ�������������
			mTopNews = newsTabBean.data.topnews;
			 
			if (mTopNews != null) {
				mViewPager.setAdapter(new TopNewsAdapter()); 
				
				mIndicator.setViewPager(mViewPager);			
				mIndicator.setSnap(true);//�Կ��յķ�ʽչʾ
				
				//��ͷ���������ñ���
				mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
					//ҳ��ѡ�е�ʱ����ø÷���
					@Override
					public void onPageSelected(int position) {
						//����ͷ�����ű���
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
				
				//���µ�һ��ͷ�����ŵı���
				mTitle.setText(mTopNews.get(0).title);
				//Ĭ���õ�һ��ѡ��(���ҳ�����ٺ����³�ʼ��ʱ,Indicator��Ȼ�����ϴ�Բ��λ�õ�bug)
				mIndicator.onPageSelected(0);
			}
			
			//�����б���������
			mNewsDataList = newsTabBean.data.news;
			if (mNewsDataList != null) {
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);
			}
		} else {
			//���ظ�������
			ArrayList<NewsData> moreNews = newsTabBean.data.news;
			mNewsDataList.addAll(moreNews);//������׷����ԭ���ļ�����
			//ˢ��listView
			mNewsAdapter.notifyDataSetChanged();
		}
		
	}

	//ͷ�����ŵ�����������
	class TopNewsAdapter extends PagerAdapter{

		private BitmapUtils mBitmapUtils;
		
		public TopNewsAdapter(){
			mBitmapUtils = new BitmapUtils(mActivity);
			//���ü����е�Ĭ��ͼƬ
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
		//����pagerҳ����Ŀ
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
//			view.setImageResource(R.drawable.topnews_item_default);
			view.setScaleType(ScaleType.FIT_XY);//����ͼƬ�����Ÿ�ʽ,�����丸�ؼ�
			
			String imageUrl =GlobalConstances.SERVER_URL+ mTopNews.get(position).topimage;//ͼƬ��url��ַ
			//����ͼƬ-��ͼƬ���ø�imageView-�����ڴ����-ʹ�û������ͼ��
			//BitmapUtils - Xutils
			mBitmapUtils.display(view, imageUrl);
			container.addView(view);
			return  view;
		}
		//����ҳ����Ŀ
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			 container.removeView((View)object);
		}
	}
	
	//�����б������������
	class NewsAdapter extends BaseAdapter{

		private BitmapUtils mBitmapUtils;

		public NewsAdapter(){
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadFailedImage(R.drawable.news_pic_default);//Ĭ�ϼ��ص�ͼƬ
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
			//��item��Ŀ��������
			NewsData news = getItem(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);
			//����ͼƬʹ��BitmapUtils��������
			mBitmapUtils.display(holder.ivIcon, GlobalConstances.SERVER_URL+news.listimage);
			
			//���ݱ��ؼ�¼���ȱ���ĸ���Ŀ�Ѷ���δ��
			String readIds = PrefUtils.getString(mActivity, "read_ids", "");
			if (readIds.contains(news.id + "")) {
				holder.tvTitle.setTextColor(Color.GRAY);
			}else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
	}

}
