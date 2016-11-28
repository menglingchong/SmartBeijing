package com.heima.smartbeijing.base.impl.menu;

import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heima.smartbeijing.R;
import com.heima.smartbeijing.base.BaseMenuDetailPager;
import com.heima.smartbeijing.base.impl.GovAffairsPager;
import com.heima.smartbeijing.domain.PhotosBean;
import com.heima.smartbeijing.domain.PhotosBean.PhotoNews;
import com.heima.smartbeijing.global.GlobalConstances;
import com.heima.smartbeijing.utils.CacheUtils;
import com.heima.smartbeijing.utils.MyBitmapUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PhotosMenuDetailPager extends BaseMenuDetailPager implements OnClickListener{

	@ViewInject(R.id.lv_photo)
	private ListView lvPhoto;
	@ViewInject(R.id.gv_photo)
	private GridView gvPhoto;
	private ArrayList<PhotoNews> photoNewsList; //图片数据列表
	private ImageButton btnPhoto;
	
	public PhotosMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		btnPhoto.setOnClickListener(this);
		this.btnPhoto = btnPhoto;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
		ViewUtils.inject(this,view);
		
		return view;
	}
	@Override
	public void initData() {
		super.initData();
		String cache = CacheUtils.getCache(mActivity, GlobalConstances.PHOTOS_URL);
		if ( !TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		
		getDataFromServer();
	}

	//从服务器加载数据
	private void getDataFromServer() {

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConstances.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//请求成功
				String result = responseInfo.result;
				processData(result);
				
				//缓存数据
				CacheUtils.setCache(mActivity, GlobalConstances.PHOTOS_URL,result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//请求失败
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 0).show();
				
			}
		});
	}

	//解析数据
	protected void processData(String json) {
		Gson gson = new Gson();
		PhotosBean photoBean = gson.fromJson(json, PhotosBean.class);
		
		photoNewsList = photoBean.data.news;
		
		lvPhoto.setAdapter(new PhotoAdapter());
		gvPhoto.setAdapter(new PhotoAdapter()); // gradeview的布局结构和listview的布局结构完全一致
	}

	
	class PhotoAdapter extends BaseAdapter{

		private MyBitmapUtils mBitmapUtils;

		public PhotoAdapter(){
			mBitmapUtils = new MyBitmapUtils();
//			mBitmapUtils.configDefaultLoadFailedImage(R.drawable.pic_item_list_default);
		}
		@Override
		public int getCount() {
			return photoNewsList.size();
		}

		@Override
		public PhotoNews getItem(int position) {
			return photoNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		} 

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_photos, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			PhotoNews photoNews = getItem(position);
			holder.tvTitle.setText(photoNews.title);
			mBitmapUtils.display(holder.ivPic, GlobalConstances.SERVER_URL+photoNews.listimage);
			return convertView;
		}
		
		class ViewHolder{
			public ImageView ivPic;
			public TextView tvTitle;
		}
		
	}

	//btnPhoto的点击事件
	private boolean isListView = true; //标记当前是否是ListView展示
	@Override
	public void onClick(View v) {

		if (isListView) {
			//切换成gradeView
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
			
			isListView = false;
		} else {
			//切换成listview
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);
			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
			
			isListView = true;
		}
	}
}
