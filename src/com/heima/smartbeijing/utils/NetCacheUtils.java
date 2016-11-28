package com.heima.smartbeijing.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/*
 * 网络缓存
 */
public class NetCacheUtils {

	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils = memoryCacheUtils;
	}

	public void getBitmapFromNet(ImageView imageView, String url) {
		//AsyncTask异步封装的工具，可以实现异步请求和主界面的更新（对线程池和handler的封装）
		new BitmapTask().execute(imageView , url);//启动Asynctask
	}
	/*
	 * 三个泛型意义：第一个泛型：doInBackground里的参数类型
	 * 第二个泛型：onProgressUpdate里的参数类型
	 * 第三个泛型：onPostExecute里的参数类型和doInBackground的返回类型
	 */
	class BitmapTask extends AsyncTask<Object, Integer, Bitmap>{

		private ImageView mImageView;
		private String mUrl;

		//初始化操作，在执行后台任务前进行初始操作，运行在主线程
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		//正在加载，运行在子线程中，可以直接异步请求
		@Override
		protected Bitmap doInBackground(Object... params) {
			mImageView = (ImageView) params[0];
			mUrl = (String) params[1];
			
			mImageView.setTag(mUrl);//打标记，将当前的imageView和mUrl绑定在一起
			
			//开始下载图片
			Bitmap bitmap = download(mUrl);
			return bitmap;
		}
		//更新后台加载进度，运行在主线程
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		//加载结束后调用此方法（运行在主线程中，直接更新UI）
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			//更新UI
			if (result != null) {
				//给imageView设置图片
				//由于listView的重用机制导致imageview对象可能被对个item共享
				//从而可能将错误的图片设置给imageview对象
				//则需要在此处校验，判断是否是正确的图片
				String url = (String) mImageView.getTag();
				//判断图片绑定的url是否当前的imageview绑定的url
				//如果是说明图片加载正确
				if (url.equals(mUrl)) {
					
					mImageView.setImageBitmap(result);
					System.out.println("从网络加载图片啦！！");
					
					//写本地缓存
					mLocalCacheUtils.setLocalCache(url, result);
					
					//写内存缓存
					mMemoryCacheUtils.setMemoryCache(url, result);
				}
			}
		}
	}
	
	//下载图片
	public Bitmap download(String url) {
	
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);//连接超时
			conn.setReadTimeout(5000);//读取超时
			conn.connect();
			
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				//读取成功
				InputStream inputStream = conn.getInputStream();
				//根据输入流生成bitmap对象
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				
				return bitmap;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}
}
