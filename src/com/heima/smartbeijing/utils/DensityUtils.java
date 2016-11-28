package com.heima.smartbeijing.utils;

import android.content.Context;

/*
 * dp和px相互转换的工具类
 */
public class DensityUtils {

	public static int dip2px(Context context, float dip){
		//获取设备密度
		float density = context.getResources().getDisplayMetrics().density;
		int px = (int) (dip * density +0.5f);
		return px;
	}
	
	public static float px2dip(Context context,int px){
		//获取设备密度
		float density = context.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}
}
