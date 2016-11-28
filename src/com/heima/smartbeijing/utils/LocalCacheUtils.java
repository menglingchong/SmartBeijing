package com.heima.smartbeijing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/*
 * 本地缓存
 */
public class LocalCacheUtils {

	//Environment.getExternalStorageDirectory().getPath():获取手机的外置存储卡，由于有内置存储卡，不同的手机可能获取的不一样
	private static final String LOCAL_CACHE_PATH = "storage/sdcard1/Smart_cache";//这里是硬编码，直接写存储地址
	//写本地缓存
	public void setLocalCache(String url, Bitmap bitmap){
		
//		System.out.println("LOCAL_CACHE_PATH::" +LOCAL_CACHE_PATH);
	
		File dir = new File(LOCAL_CACHE_PATH);
		if (! dir.exists() || !dir.isDirectory()) { //文件夹不存在或者不是文件夹名，则创建文件夹
			
			dir.mkdir();//创建文件夹
		}
		try { 
			String fileName = MD5Encoder.encode(url);
			
			File cacheFile = new File(dir, fileName);
			//图片进行压缩，参数1：图片的格式。参数2：压缩比例0-100 参数3：文件输出流
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//读本地缓存
	public Bitmap getLocalCache(String url){
		try {
			File cacheFile = new File(LOCAL_CACHE_PATH, MD5Encoder.encode(url));
			if (cacheFile.exists()) {
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
