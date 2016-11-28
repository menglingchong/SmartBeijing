package com.heima.smartbeijing.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/*
 * �ڴ滺��
 */
public class MemoryCacheUtils {

private LruCache<String, Bitmap> mMemoryCache;

//	private HashMap<String , Bitmap> mMemoryCache = new HashMap<String, Bitmap>();

	//��bitmap����ʹ�������ð�װ����
//	private HashMap<String , SoftReference<Bitmap> > mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();
	
	//ʹ��LruCache���ڴ�ͼƬ���л���
	public MemoryCacheUtils(){
		//LruCache���Խ��������ʹ�õĶ�����գ��Ӷ���֤�ڴ治�ᳬ����Χ
		//Lru : least recentlly used �������ʹ���㷨
		
		long maxMemory = Runtime.getRuntime().maxMemory();//��ȡ�����app���ڴ��С,һ������app�Ĵ�СΪ16M
		System.out.println("maxMemory:"+maxMemory);
		
		mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)){
			//����ÿ������Ĵ�С
			@Override
			protected int sizeOf(String key, Bitmap value) {
				
				int byteCount = value.getByteCount();//����ͼƬ�Ĵ�С
				return byteCount;
			}
		};
		
		 
	}
	
	//д�ڴ滺��
	public void setMemoryCache(String url,Bitmap bitmap){
//		mMemoryCache.put(url, bitmap);
		
		//ʹ�������ý�bitmap��װ����
//		SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
//		mMemoryCache.put(url, softBitmap);
		
		mMemoryCache.put(url, bitmap);
	}
	
	//���ڴ滺��
	public Bitmap getMemoryCache(String url){
 //		return mMemoryCache.get(url);
		
//		SoftReference<Bitmap> softReference = mMemoryCache.get(url);
//		if (softReference != null) {
//			Bitmap bitmap = softReference.get();
//			return bitmap;
//		}
//		return null;
		return mMemoryCache.get(url);
	}
}
