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
 * ���绺��
 */
public class NetCacheUtils {

	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils = memoryCacheUtils;
	}

	public void getBitmapFromNet(ImageView imageView, String url) {
		//AsyncTask�첽��װ�Ĺ��ߣ�����ʵ���첽�����������ĸ��£����̳߳غ�handler�ķ�װ��
		new BitmapTask().execute(imageView , url);//����Asynctask
	}
	/*
	 * �����������壺��һ�����ͣ�doInBackground��Ĳ�������
	 * �ڶ������ͣ�onProgressUpdate��Ĳ�������
	 * ���������ͣ�onPostExecute��Ĳ������ͺ�doInBackground�ķ�������
	 */
	class BitmapTask extends AsyncTask<Object, Integer, Bitmap>{

		private ImageView mImageView;
		private String mUrl;

		//��ʼ����������ִ�к�̨����ǰ���г�ʼ���������������߳�
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		//���ڼ��أ����������߳��У�����ֱ���첽����
		@Override
		protected Bitmap doInBackground(Object... params) {
			mImageView = (ImageView) params[0];
			mUrl = (String) params[1];
			
			mImageView.setTag(mUrl);//���ǣ�����ǰ��imageView��mUrl����һ��
			
			//��ʼ����ͼƬ
			Bitmap bitmap = download(mUrl);
			return bitmap;
		}
		//���º�̨���ؽ��ȣ����������߳�
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		//���ؽ�������ô˷��������������߳��У�ֱ�Ӹ���UI��
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			//����UI
			if (result != null) {
				//��imageView����ͼƬ
				//����listView�����û��Ƶ���imageview������ܱ��Ը�item����
				//�Ӷ����ܽ������ͼƬ���ø�imageview����
				//����Ҫ�ڴ˴�У�飬�ж��Ƿ�����ȷ��ͼƬ
				String url = (String) mImageView.getTag();
				//�ж�ͼƬ�󶨵�url�Ƿ�ǰ��imageview�󶨵�url
				//�����˵��ͼƬ������ȷ
				if (url.equals(mUrl)) {
					
					mImageView.setImageBitmap(result);
					System.out.println("���������ͼƬ������");
					
					//д���ػ���
					mLocalCacheUtils.setLocalCache(url, result);
					
					//д�ڴ滺��
					mMemoryCacheUtils.setMemoryCache(url, result);
				}
			}
		}
	}
	
	//����ͼƬ
	public Bitmap download(String url) {
	
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);//���ӳ�ʱ
			conn.setReadTimeout(5000);//��ȡ��ʱ
			conn.connect();
			
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				//��ȡ�ɹ�
				InputStream inputStream = conn.getInputStream();
				//��������������bitmap����
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
