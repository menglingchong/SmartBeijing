package com.heima.smartbeijing.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.heima.smartbeijing.R;

public class PullToRefreshListView extends ListView implements OnScrollListener{

	private static final int STATE_PULL_TO_REFRESH =1;
	private static final int STATE_RELEASE_TO_REFRESH =2;
	private static final int STATE_REFRESHING =3;
	private int mCurrentState = STATE_PULL_TO_REFRESH;//��ǰˢ��״̬
	
	private View mHeaderView;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private TextView tvTitle;
	private TextView tvTime;
	private int mHeaderViewHeight;
	private int startY =-1;
	private RotateAnimation animationUP;
	private RotateAnimation animationDown;
	private OnRefreshListenr mListener; //�����Ա���������ռ�������
	private View mBotomView;
	private int mBotomViewHeight;

	public PullToRefreshListView(Context context) {
		super(context);
		initHeaderView();
		initBotomView();
	}
	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initBotomView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeaderView();
		initBotomView();
	}
	
	//��ʼ��ListView����ˢ��ͷ����
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
		this.addHeaderView(mHeaderView);//view�������ͷ����
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		
		//����ͷ���ֵĿ��ֵ������ͷ����
		mHeaderView.measure(0, 0);//���չ涨�ķ�ʽ���в���
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//����ͷ����
		
		//��ʼ������ 
		initAnimation();
		//��ʼ��ˢ��ʱ�� 
		setCurrentTime();
	}
	//��ʼ��ListView�������صĵײ���
	private void initBotomView(){
		
		mBotomView = View.inflate(getContext(), R.layout.pull_to_refresh_botom, null);
		this.addFooterView(mBotomView);//��ӵײ���view����
		
		//�����ײ��ֵĸ߶�
		mBotomView.measure(0, 0);
		mBotomViewHeight = mBotomView.getMeasuredHeight();
		mBotomView.setPadding(0, -mBotomViewHeight, 0, 0);
		
		//��������״̬
		this.setOnScrollListener(this);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		//�жϻ����ľ��룬��ͷ��������padding
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			//���û���סͷ�����ŵ�viewpager��������ʱ��ActionDown�ᱻViewpager���ѵ�
			//����startYû�и�ֵ���˴���Ҫ���»�ȡstartYֵ
			if (startY ==-1) {
				startY = (int) ev.getY();
			}
			//�������ִ��ˢ���У�ִ�и���Ĵ���
			if (mCurrentState == STATE_REFRESHING) {
				return super.onTouchEvent(ev);
			}
			
			int endY = (int) ev.getY();
			int dy = endY-startY;
			int firstVisiblePosition = getFirstVisiblePosition();
			//����ˢ�£���ǰ��ʾ��item�ǵ�һ��item
			if (dy > 0 && firstVisiblePosition==0) {
				int padding = dy - mHeaderViewHeight;//���㵱ǰ����ˢ�µ�paddingֵ
				mHeaderView.setPadding(0, padding, 0, 0);
				
				if (padding >= 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					//״̬��Ϊ�ɿ�ˢ��ģʽ
					mCurrentState= STATE_RELEASE_TO_REFRESH;
					refreshState();//�������µ�״ֵ̬���²�������
				} else if (padding <0 && mCurrentState != STATE_PULL_TO_REFRESH) {
					//״̬��Ϊ����ˢ��ģʽ
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();//�������µ�״ֵ̬���²�������
				}
				return true;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			//���ݸո����õ�״̬
			startY = -1;
			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				mCurrentState = STATE_REFRESHING;
				refreshState();
				//������ʾͷ����
				mHeaderView.setPadding(0, 0, 0, 0);
				//���лص�
//				if (mListener != null) {
//					mListener.onRefresh();
//				}
				
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				//����ͷ����
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}
			
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	//��ʼ����ͷ����
	private void initAnimation(){
		animationUP = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationUP.setDuration(200);
		animationUP.setFillAfter(true);
		
		animationDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationDown.setDuration(200);
		animationDown.setFillAfter(true);
	
	}
	//�������µ�״ֵ̬���²�������
	private void refreshState() {
		switch (mCurrentState) {
		
		//����ˢ��
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("����ˢ��");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.startAnimation(animationDown);
			
			break;
		//�ɿ�ˢ��
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("�ɿ�ˢ��");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.startAnimation(animationUP);
			break;
		//ˢ����
		case STATE_REFRESHING:
			ivArrow.clearAnimation();//�����ͷ�����������޷�ˢ��
			tvTitle.setText("ˢ���С�����");
			pbProgress.setVisibility(View.VISIBLE);
			ivArrow.setVisibility(View.INVISIBLE);
			
			//���ûص�����
			if (mListener != null) {
				mListener.onRefresh();
			}
			break;
		default:
			break;
		}
	}
	
	//����ˢ�µĻص��ӿ�
	public interface OnRefreshListenr{
		public void onRefresh();
		public void onLoadMore();
	}
	
	//��¶�ӿڣ����ü���
	public void setOnRefreshListener(OnRefreshListenr mListener){
		this.mListener = mListener;
	}
	
	//ˢ�½���������ؼ�,�ָ�����Ч��
	public void onRefreshComplete(boolean success){
		
		if (!isLoading) {
			
			mHeaderView.setPadding(0,-mHeaderViewHeight,0, 0);
			mCurrentState = STATE_PULL_TO_REFRESH;
			tvTitle.setText("����ˢ��");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			//ֻ��ˢ�³ɹ�������ʱ��
			if (success) {
				setCurrentTime();
			}
		} else {
			//���ظ���
			mBotomView.setPadding(0, -mBotomViewHeight, 0, 0); //���صײ���
			isLoading = false;
		}
		
	}
	
	// ����ˢ��ʱ��
	private void setCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());

		tvTime.setText(time);
	}
	
	private boolean isLoading;//����Ƿ����ڼ��ظ���
	//����ת̬�ı�ʱ���ø÷���
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {//�������ڿ���״̬
			int lastVisiblePosition = getLastVisiblePosition();
			if (lastVisiblePosition == getCount()-1 && !isLoading) {//����ʾ�����һ������û�����ڼ��ظ���ͼ��ظ���
				System.out.println("���ظ���");
				//���ظ���
				isLoading = true;

				mBotomView.setPadding(0, 0, 0, 0);
				setSelection(getCount()-1);//��ʾ����һ����Ŀ�ϣ����ظ������ʾ����
				
				//���ûص�������֪ͨ�����������һҳ����
				if (mListener != null) {
					mListener.onLoadMore();
				}
				
			}
			
		}
	}
	//����ʱ���ø÷���
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	
}
