package com.heima.smartbeijing.utils;

import com.heima.smartbeijing.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

/*
 * �Զ�����������ͼƬ���ع���
 */
public class MyBitmapUtils {

	private MemoryCacheUtils mMemoryCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	private NetCacheUtils mNetCacheUtils;
	private Bitmap bitmap;
	
	public MyBitmapUtils (){
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
		
	}
	
	public void display(ImageView imageView, String url) {
		
		//����Ĭ��ͼƬ
		imageView.setImageResource(R.drawable.pic_item_list_default);
		
		//���ڴ��л�ȡͼƬ���ٶ���죬����������
		bitmap = mMemoryCacheUtils.getMemoryCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			System.out.println("���ڴ����ͼƬ!");
			return ;
		}
		
		//�ӱ�������ͼƬ���ٶȿ죬����������
		bitmap = mLocalCacheUtils.getLocalCache(url);
		if (bitmap != null) {
			//��ͼƬ���ظ�iamgeView
			imageView.setImageBitmap(bitmap);
			System.out.println("�ӱ��ؼ���ͼƬ����");
			
			//д�ڴ滺��
			mMemoryCacheUtils.setMemoryCache(url, bitmap);
			
			return;
		}
		//������������ͼƬ���ٶ������˷�����
		mNetCacheUtils.getBitmapFromNet(imageView,url);
	}

}
