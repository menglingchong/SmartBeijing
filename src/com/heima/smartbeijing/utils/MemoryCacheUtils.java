package com.heima.smartbeijing.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/*
 * 内存缓存
 */
public class MemoryCacheUtils {

private LruCache<String, Bitmap> mMemoryCache;

//	private HashMap<String , Bitmap> mMemoryCache = new HashMap<String, Bitmap>();

	//对bitmap对象使用软引用包装起来
//	private HashMap<String , SoftReference<Bitmap> > mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();
	
	//使用LruCache对内存图片进行缓存
	public MemoryCacheUtils(){
		//LruCache可以将最近最少使用的对象回收，从而保证内存不会超出范围
		//Lru : least recentlly used 最近最少使用算法
		
		long maxMemory = Runtime.getRuntime().maxMemory();//获取分配给app的内存大小,一般分配给app的大小为16M
		System.out.println("maxMemory:"+maxMemory);
		
		mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)){
			//返回每个对象的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				
				int byteCount = value.getByteCount();//计算图片的大小
				return byteCount;
			}
		};
		
		 
	}
	
	//写内存缓存
	public void setMemoryCache(String url,Bitmap bitmap){
//		mMemoryCache.put(url, bitmap);
		
		//使用软引用将bitmap包装起来
//		SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
//		mMemoryCache.put(url, softBitmap);
		
		mMemoryCache.put(url, bitmap);
	}
	
	//读内存缓存
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
