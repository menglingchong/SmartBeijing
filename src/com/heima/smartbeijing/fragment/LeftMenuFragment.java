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
//�����fragment
public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_listview)//ʹ��ע��ķ�ʽ��ʼ���ؼ�
	private ListView listView;
	private ArrayList<NewsMenuData> mNewsMenuData;//������������ݶ���
	private int mCurrentPos;//��ǰ��ѡ�е�tiem��λ��
	private LeftMenuAdapter mAdapter;
	@Override
	public View initView() {
		
		View view = View.inflate(mActivity,R.layout.fragment_left_menu, null);
//		listView = (ListView) view.findViewById(R.id.lv_listview);
		ViewUtils.inject(this, view);//ע��view���¼�
		return view;
	}

	@Override
	public void initData() {

	}
	//������ݸ������
	public void setMenuData(ArrayList<NewsMenuData> data) {
		//��ǰѡ�е�λ�ù���
		mCurrentPos = 0;
		
		mNewsMenuData = data;
		mAdapter = new LeftMenuAdapter();
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentPos = position;//���µ�ǰ��ѡ�е�λ��
				mAdapter.notifyDataSetChanged();
				
				//��������
				toggle();
				//��������֮���޸��������ĵ�FrameLayout������
				setCurrentDetailPager(position);
			}
		});
		
	}
	/*
	 * ���õ�ǰ�Ĳ˵�����ҳ
	 */
	protected void setCurrentDetailPager(int position) {
		// ��ȡ�������ĵĶ���
		MainActivity mainUI = (MainActivity) mActivity;
		ContentFragment contentFragment = mainUI.getContentFragment();
		NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
		//�޸��������ĵ�FrameLayout�Ĳ���
		newsCenterPager.setCurrentDetailPager(position);
	}

	//�򿪻�رղ����
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();//���״̬�ǿ������ú�رգ���֮��Ȼ
	}
	//�����������������
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

		//������Ŀ��view����
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_leftmenu);
			NewsMenuData item = getItem(position);
			tvMenu.setText(item.title);
			
			if (position == mCurrentPos) {
				//��ѡ��
				tvMenu.setEnabled(true);//���ֱ�ɺ�ɫ
			} else {
				//δѡ��
				tvMenu.setEnabled(false);//���ֱ�ɰ�ɫ
			}
			return view;
		}
	}
}
