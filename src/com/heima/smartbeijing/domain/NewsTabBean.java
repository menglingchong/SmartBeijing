package com.heima.smartbeijing.domain;

import java.util.ArrayList;

/*
 * ҳǩ�������ݶ���
 */
public class NewsTabBean {

	public NewsTab data;
	
	public class NewsTab{
		public String more;
		public ArrayList<NewsData> news;
		public ArrayList<TopNews> topnews; 
		
	}
	//�����б����
	public class NewsData{
		public int id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}

	//ͷ�����Ŷ���
	public class TopNews{
		public int id;
		public String topimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;	
	}
}
