package com.heima.smartbeijing.utils;

import android.content.Context;

/**
 * �������ݻ��棬��url��Ϊkey����json��Ϊvalue
 * @author lenovo
 *
 */
public class CacheUtils {

	//���û���,��json���浽SharedPreference
	public static void setCache(Context context,String url,String json){
		//Ҳ����ʹ���ļ����棬��MD5(url)Ϊ�ļ�������jsonΪ�ļ�����
		PrefUtils.setString(context, url, json);
	}
	
	//��ȡ����
	public static String getCache(Context context,String url){
		return PrefUtils.getString(context, url, null);
		
	}
}
