package com.heima.smartbeijing.fragment;

import java.util.ArrayList;

import com.heima.smartbeijing.MainActivity;
import com.heima.smartbeijing.R;
import com.heima.smartbeijing.base.impl.NewsCenterPager;
import com.heima.smartbeijing.domain.NewsMenu;
import com.heima.smartbeijing.domain.NewsMenu.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
//侧边栏fragment
public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_listview)//使用注解的方式初始化控件
	private ListView listView;
	private ArrayList<NewsMenuData> mNewsMenuData;//侧边栏网络数据对象
	private int mCurrentPos;//当前被选中的tiem的位置
	private LeftMenuAdapter mAdapter;
	@Override
	public View initView() {
		
		View view = View.inflate(mActivity,R.layout.fragment_left_menu, null);
//		listView = (ListView) view.findViewById(R.id.lv_listview);
		ViewUtils.inject(this, view);//注入view和事件
		return view;
	}

	@Override
	public void initData() {

	}
	//添加数据给侧边栏
	public void setMenuData(ArrayList<NewsMenuData> data) {
		//当前选中的位置归零
		mCurrentPos = 0;
		
		mNewsMenuData = data;
		mAdapter = new LeftMenuAdapter();
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentPos = position;//更新当前被选中的位置
				mAdapter.notifyDataSetChanged();
				
				//收起侧边栏
				toggle();
				//侧边栏点击之后修改新闻中心的FrameLayout的内容
				setCurrentDetailPager(position);
			}
		});
		
	}
	/*
	 * 设置当前的菜单详情页
	 */
	protected void setCurrentDetailPager(int position) {
		// 获取新闻中心的对象
		MainActivity mainUI = (MainActivity) mActivity;
		ContentFragment contentFragment = mainUI.getContentFragment();
		NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
		//修改新闻中心的FrameLayout的布局
		newsCenterPager.setCurrentDetailPager(position);
	}

	//打开或关闭侧边栏
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();//如果状态是开，调用后关闭，反之亦然
	}
	//给侧边栏设置适配器
	class LeftMenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mNewsMenuData.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return mNewsMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		//返回条目的view对象
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_leftmenu);
			NewsMenuData item = getItem(position);
			tvMenu.setText(item.title);
			
			if (position == mCurrentPos) {
				//被选中
				tvMenu.setEnabled(true);//文字变成红色
			} else {
				//未选中
				tvMenu.setEnabled(false);//文字变成白色
			}
			return view;
		}
	}
}
