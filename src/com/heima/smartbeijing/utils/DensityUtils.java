package com.heima.smartbeijing.utils;

import android.content.Context;

/*
 * dp��px�໥ת���Ĺ�����
 */
public class DensityUtils {

	public static int dip2px(Context context, float dip){
		//��ȡ�豸�ܶ�
		float density = context.getResources().getDisplayMetrics().density;
		int px = (int) (dip * density +0.5f);
		return px;
	}
	
	public static float px2dip(Context context,int px){
		//��ȡ�豸�ܶ�
		float density = context.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}
}
