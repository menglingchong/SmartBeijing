package com.heima.smartbeijing.domain;

import java.util.ArrayList;

public class NewsMenu {

	/**
	 * 分类信息封装
	 * 使用Gson解析时，对象书写技巧：1.封{}创建对象，逢[]创建集合ArrayList
	 * 2.所有字段名称要和json返回的字段高度一致，这样解析的数据才能对得上
	 */
	public int retcode;
	public ArrayList<Integer> extend;
	public ArrayList<NewsMenuData> data;
	
	//侧边栏菜单对象
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
	
	//页签的对象
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

