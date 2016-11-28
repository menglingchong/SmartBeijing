package com.heima.smartbeijing;

import java.util.ArrayList;

import com.heima.smartbeijing.utils.DensityUtils;
import com.heima.smartbeijing.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * ������������
 * @author lenovo
 *
 */
public class GuideActivity extends Activity{
	
	private ViewPager mViewPager;
	
	//����ҳͼƬid����
	private int[] mImageIds = new int[]{R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3};

	private ArrayList<ImageView> mImageViewList;

	private LinearLayout llContainer;

	private ImageView ivRedPoint;

	private Button btnStart;

	private int mPointDis;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
	
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		llContainer = (LinearLayout) findViewById(R.id.ll_container);
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		btnStart = (Button) findViewById(R.id.btn_start);
		initData();//��ʼ������
		
		mViewPager.setAdapter(new GuideAdapter());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// ĳ��ҳ�汻ѡ��ʱ����
				//��������ҳ�汻ѡ�е�ʱ����ʾ��Buttton��ť
				if (position == mImageViewList.size()-1) {
					btnStart.setVisibility(View.VISIBLE);
				} else {
					btnStart.setVisibility(View.INVISIBLE);					
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// ����ҳ��ʱ����,ҳ�滬�������б�����
				System.out.println("��ǰ��λ�ã� "+position +",��ǰ�ƶ�ƫ�Ƶİٷֱȣ�"+positionOffset);
				//����С���ľ���
				int leftMargin = (int) (mPointDis*positionOffset) +position* mPointDis ;
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
				params.leftMargin= leftMargin;//�޸���߾�
				
				//��������С���Ĳ��ֲ���
				ivRedPoint.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// ҳ�汻�ı�ʱ����
				
			}
		});
		
		//��������Բ��ľ���
		//�ƶ�����= �ڶ���Բ����߾�-��һ��Բ����߾�
		//View �Ļ������� measure->layout ->draw��activity��onCreat����ִ�н���֮��Ż��ߴ����̣�
//		mPointDis = llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();
		
		//����layout�����������¼���ȷ����λ��֮���ٻ�ȡԲ�����
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				//�Ƴ������������ظ��ص�
				ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				//layout����ִ�н����Ļص�
				mPointDis = llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();
				System.out.println("����Բ��֮��ľ��룺"+mPointDis);
			}
		});
		//����Button��ť�ĵ���¼�
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//��ת����ҳ�沢�Ҹ���sp��isFirstEnter����Ϊfalse
				PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", false);
				
				//��ת����ҳ��
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
	}
	//��ʼ������
	private void initData() {
		mImageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			//��ʼ��Ҫ��ʾ��ͼƬ����
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]);//ͨ�����ñ����ÿ����䲼��
			mImageViewList.add(imageView);
			
			//��ʼ��СԲ��ָʾ��
			ImageView pointView = new ImageView(this);
			pointView.setImageResource(R.drawable.shape_point_gray);
			//��ʼ�����ֲ�������߰������ݣ����ؼ���˭��������˭�Ĳ��ֲ���
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		
			if (i>0) {
				//�ӵڶ����㿪ʼ������߾�
				params.leftMargin=DensityUtils.dip2px(this, 10);
			}
			//����СԲ��Ĳ���
			pointView.setLayoutParams(params);
			llContainer.addView(pointView);
		}
		
	}

	public class GuideAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			//item�ĸ���
			return mImageViewList.size();
		}
		//���ظ����ж��߼���
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		//����Ҫ��ʾ����Ŀ�����ݣ�������Ŀ
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageViewList.get(position);
			container.addView(view);//��view������ӵ�container��
			return view;//��view���󷵻ظ����������˴�������д�������쳣��
		}
		//������Ŀ
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
	 
}
