package com.heima.smartbeijing.utils;

import android.content.Context;

/**
 * 网络数据缓存，以url作为key，以json作为value
 * @author lenovo
 *
 */
public class CacheUtils {

	//设置缓存,将json缓存到SharedPreference
	public static void setCache(Context context,String url,String json){
		//也可以使用文件缓存，以MD5(url)为文件名，以json为文件内容
		PrefUtils.setString(context, url, json);
	}
	
	//读取缓存
	public static String getCache(Context context,String url){
		return PrefUtils.getString(context, url, null);
		
	}
}
