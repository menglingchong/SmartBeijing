package com.heima.smartbeijing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/*
 * ���ػ���
 */
public class LocalCacheUtils {

	//Environment.getExternalStorageDirectory().getPath():��ȡ�ֻ������ô洢�������������ô洢������ͬ���ֻ����ܻ�ȡ�Ĳ�һ��
	private static final String LOCAL_CACHE_PATH = "storage/sdcard1/Smart_cache";//������Ӳ���룬ֱ��д�洢��ַ
	//д���ػ���
	public void setLocalCache(String url, Bitmap bitmap){
		
//		System.out.println("LOCAL_CACHE_PATH::" +LOCAL_CACHE_PATH);
	
		File dir = new File(LOCAL_CACHE_PATH);
		if (! dir.exists() || !dir.isDirectory()) { //�ļ��в����ڻ��߲����ļ��������򴴽��ļ���
			
			dir.mkdir();//�����ļ���
		}
		try { 
			String fileName = MD5Encoder.encode(url);
			
			File cacheFile = new File(dir, fileName);
			//ͼƬ����ѹ��������1��ͼƬ�ĸ�ʽ������2��ѹ������0-100 ����3���ļ������
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//�����ػ���
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
