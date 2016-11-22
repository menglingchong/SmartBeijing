package com.heima.smartbeijing.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heima.smartbeijing.MainActivity;
import com.heima.smartbeijing.base.BaseMenuDetailPager;
import com.heima.smartbeijing.base.BasePager;
import com.heima.smartbeijing.base.impl.menu.InteractMenuDetailPager;
import com.heima.smartbeijing.base.impl.menu.NewsMenuDetailPager;
import com.heima.smartbeijing.base.impl.menu.PhotosMenuDetailPager;
import com.heima.smartbeijing.base.impl.menu.TopicMenuDetailPager;
import com.heima.smartbeijing.domain.NewsMenu;
import com.heima.smartbeijing.fragment.LeftMenuFragment;
import com.heima.smartbeijing.global.GlobalConstances;
import com.heima.smartbeijing.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mMenuDetailPagerList;//�˵�����ҳ����
	private NewsMenu newsData;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}
	
	//��ʼ������
	public void initData(){
		System.out.println("�������ĳ�ʼ����������");
		
		//��֡������䲼�ֶ���
//		TextView view = new TextView(mActivity);
//		view.setText("��������");
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
//		flContent.addView(view);
		
		//�޸�ҳ���ǩ
		tvTitle.setText("��������");
		
		//���ж���û�л��棬����л���ֱ�ӽ������������
		String cache = CacheUtils.getCache(mActivity, GlobalConstances.CATEGORY_URL);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("���ֻ��档����");
			processData(cache);
		} 
		
		//�ӷ�������ȡ����
		//ʹ�ÿ�Դ���
		getDataFromServer();//��û�л��涼Ҫ�ӷ�������ȡ���ݣ��ô�����߽�����Ѻ�Ч��,�������û���һʱ�俴������
	}

	private void getDataFromServer() {
		// �ӷ�������ȡ����
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstances.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// ����ɹ�
				String result = responseInfo.result;
				System.out.println("���������ظ������ݣ�"+result);
				
				//��������,ʹ��Gson���
				processData(result);
				//д����
				CacheUtils.setCache(mActivity, GlobalConstances.CATEGORY_URL, result);
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//  ����ʧ��
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 0).show();
			}
		});
		
	}

	//��������
	protected void processData(String json) {
		
		Gson gson = new Gson();
		newsData = gson.fromJson(json, NewsMenu.class);
		
		System.out.println("���������"+newsData);
		//�����������ݴ��������,��Ҫ�õ����������
		MainActivity mainUI = (MainActivity) mActivity;
		LeftMenuFragment leftFragment = mainUI.getLeftFragment();
		//���������������
		leftFragment.setMenuData(newsData.data);
		//��ʼ��4���˵�����ҳ
		mMenuDetailPagerList = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagerList.add(new NewsMenuDetailPager(mActivity,newsData.data.get(0).children));
		mMenuDetailPagerList.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagerList.add(new PhotosMenuDetailPager(mActivity));
		mMenuDetailPagerList.add(new InteractMenuDetailPager(mActivity));
		
		//�����Ų˵�����ҳ����ΪĬ��ҳ��
		setCurrentDetailPager(0);
	}

	//���ò˵�����ҳ�����޸������е�FrameLayou������
	public void setCurrentDetailPager(int position) {
		//���¸�frameLayout�������
		BaseMenuDetailPager pager = mMenuDetailPagerList.get(position);
		View view = pager.mRootView;//��ǰҳ��Ĳ���
		//���֮ǰ�ľɲ���
		flContent.removeAllViews();
		
		flContent.addView(view);//��֡������Ӳ��ֶ���
		pager.initData();//��ʼ��ҳ������
		//���±���
		tvTitle.setText(newsData.data.get(position).title);
	}
}
