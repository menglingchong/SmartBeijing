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
	private int mCurrentState = STATE_PULL_TO_REFRESH;//当前刷新状态
	
	private View mHeaderView;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private TextView tvTitle;
	private TextView tvTime;
	private int mHeaderViewHeight;
	private int startY =-1;
	private RotateAnimation animationUP;
	private RotateAnimation animationDown;
	private OnRefreshListenr mListener; //定义成员变量，接收监听对象
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
	
	//初始化ListView下拉刷新头布局
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
		this.addHeaderView(mHeaderView);//view对象添加头布局
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		
		//测量头布局的宽高值并隐藏头布局
		mHeaderView.measure(0, 0);//按照规定的方式进行测量
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏头布局
		
		//初始化动画 
		initAnimation();
		//初始化刷新时间 
		setCurrentTime();
	}
	//初始化ListView上拉加载的底布局
	private void initBotomView(){
		
		mBotomView = View.inflate(getContext(), R.layout.pull_to_refresh_botom, null);
		this.addFooterView(mBotomView);//添加底布局view对象
		
		//测量底布局的高度
		mBotomView.measure(0, 0);
		mBotomViewHeight = mBotomView.getMeasuredHeight();
		mBotomView.setPadding(0, -mBotomViewHeight, 0, 0);
		
		//监听滑动状态
		this.setOnScrollListener(this);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		//判断滑动的距离，给头布局设置padding
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			//当用户按住头条新闻的viewpager进行下拉时，ActionDown会被Viewpager消费掉
			//导致startY没有赋值，此处需要重新获取startY值
			if (startY ==-1) {
				startY = (int) ev.getY();
			}
			//如果正在执行刷新中，执行父类的处理
			if (mCurrentState == STATE_REFRESHING) {
				return super.onTouchEvent(ev);
			}
			
			int endY = (int) ev.getY();
			int dy = endY-startY;
			int firstVisiblePosition = getFirstVisiblePosition();
			//下拉刷新，当前显示的item是第一个item
			if (dy > 0 && firstVisiblePosition==0) {
				int padding = dy - mHeaderViewHeight;//计算当前下拉刷新的padding值
				mHeaderView.setPadding(0, padding, 0, 0);
				
				if (padding >= 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					//状态改为松开刷新模式
					mCurrentState= STATE_RELEASE_TO_REFRESH;
					refreshState();//根据最新的状态值更新布局内容
				} else if (padding <0 && mCurrentState != STATE_PULL_TO_REFRESH) {
					//状态改为下拉刷新模式
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();//根据最新的状态值更新布局内容
				}
				return true;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			//根据刚刚设置的状态
			startY = -1;
			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				mCurrentState = STATE_REFRESHING;
				refreshState();
				//完整显示头布局
				mHeaderView.setPadding(0, 0, 0, 0);
				//进行回调
//				if (mListener != null) {
//					mListener.onRefresh();
//				}
				
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				//隐藏头布局
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}
			
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	//初始化箭头动画
	private void initAnimation(){
		animationUP = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationUP.setDuration(200);
		animationUP.setFillAfter(true);
		
		animationDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationDown.setDuration(200);
		animationDown.setFillAfter(true);
	
	}
	//根据最新的状态值更新布局内容
	private void refreshState() {
		switch (mCurrentState) {
		
		//下拉刷新
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("下拉刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.startAnimation(animationDown);
			
			break;
		//松开刷新
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("松开刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.startAnimation(animationUP);
			break;
		//刷新中
		case STATE_REFRESHING:
			ivArrow.clearAnimation();//清楚箭头动画，否则无法刷新
			tvTitle.setText("刷新中。。。");
			pbProgress.setVisibility(View.VISIBLE);
			ivArrow.setVisibility(View.INVISIBLE);
			
			//设置回调监听
			if (mListener != null) {
				mListener.onRefresh();
			}
			break;
		default:
			break;
		}
	}
	
	//下拉刷新的回调接口
	public interface OnRefreshListenr{
		public void onRefresh();
		public void onLoadMore();
	}
	
	//暴露接口，设置监听
	public void setOnRefreshListener(OnRefreshListenr mListener){
		this.mListener = mListener;
	}
	
	//刷新结束，收起控件,恢复界面效果
	public void onRefreshComplete(boolean success){
		
		if (!isLoading) {
			
			mHeaderView.setPadding(0,-mHeaderViewHeight,0, 0);
			mCurrentState = STATE_PULL_TO_REFRESH;
			tvTitle.setText("下拉刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			//只有刷新成功才设置时间
			if (success) {
				setCurrentTime();
			}
		} else {
			//加载更多
			mBotomView.setPadding(0, -mBotomViewHeight, 0, 0); //隐藏底布局
			isLoading = false;
		}
		
	}
	
	// 设置刷新时间
	private void setCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());

		tvTime.setText(time);
	}
	
	private boolean isLoading;//标记是否正在加载更多
	//滑动转态改变时调用该方法
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {//滑动处于空闲状态
			int lastVisiblePosition = getLastVisiblePosition();
			if (lastVisiblePosition == getCount()-1 && !isLoading) {//当显示到最后一个并且没有正在加载更多就加载更所
				System.out.println("加载更多");
				//加载更多
				isLoading = true;

				mBotomView.setPadding(0, 0, 0, 0);
				setSelection(getCount()-1);//显示最后的一个条目上，加载更多会显示出来
				
				//设置回调监听，通知主界面加载下一页数据
				if (mListener != null) {
					mListener.onLoadMore();
				}
				
			}
			
		}
	}
	//滑动时调用该方法
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	
}
