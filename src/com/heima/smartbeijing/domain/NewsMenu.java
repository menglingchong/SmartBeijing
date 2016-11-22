package com.heima.smartbeijing.domain;

import java.util.ArrayList;

public class NewsMenu {

	/**
	 * ������Ϣ��װ
	 * ʹ��Gson����ʱ��������д���ɣ�1.��{}�������󣬷�[]��������ArrayList
	 * 2.�����ֶ�����Ҫ��json���ص��ֶθ߶�һ�£��������������ݲ��ܶԵ���
	 */
	public int retcode;
	public ArrayList<Integer> extend;
	public ArrayList<NewsMenuData> data;
	
	//������˵�����
	public class NewsMenuData{
		public int id;
		public String title;
		public int type;
		
		public ArrayList<NewsTabData> children;

		@Override
		public String toString() {
			return "NewsMenuData [id=" + id + ", title=" + title
					+ ", children=" + children + "]";
		}
		
		
	}
	
	//ҳǩ�Ķ���
	public class NewsTabData{
		public int id;
		public String title;
		public int type;
		public String url;
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
		
	}

	@Override
	public String toString() {
		return "NewsMenu [data=" + data + "]";
	}
	
}

