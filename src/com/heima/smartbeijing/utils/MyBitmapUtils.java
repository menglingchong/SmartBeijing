package com.heima.smartbeijing.utils;

import com.heima.smartbeijing.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

/*
 * 自定义三级缓存图片加载工具
 */
public class MyBitmapUtils {

	private MemoryCacheUtils mMemoryCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	private NetCacheUtils mNetCacheUtils;
	private Bitmap bitmap;
	
	public MyBitmapUtils (){
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
		
	}
	
	public void display(ImageView imageView, String url) {
		
		//设置默认图片
		imageView.setImageResource(R.drawable.pic_item_list_default);
		
		//从内存中获取图片，速度最快，不消费流量
		bitmap = mMemoryCacheUtils.getMemoryCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			System.out.println("从内存加载图片!");
			return ;
		}
		
		//从本地下载图片，速度快，不消耗流量
		bitmap = mLocalCacheUtils.getLocalCache(url);
		if (bitmap != null) {
			//将图片加载给iamgeView
			imageView.setImageBitmap(bitmap);
			System.out.println("从本地加载图片啦！");
			
			//写内存缓存
			mMemoryCacheUtils.setMemoryCache(url, bitmap);
			
			return;
		}
		//从网络中下载图片，速度慢，浪费流量
		mNetCacheUtils.getBitmapFromNet(imageView,url);
	}

}
