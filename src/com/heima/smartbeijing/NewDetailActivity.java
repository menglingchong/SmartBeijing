package com.heima.smartbeijing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
/*
 * 新闻的详情页面
 */
public class NewDetailActivity extends Activity implements OnClickListener{

	@ViewInject(R.id.ll_control)
	private LinearLayout llControl;
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;
	@ViewInject(R.id.btn_textszie)
	private ImageButton btnTextSize;
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	@ViewInject(R.id.btn_menu)
	private ImageButton btnMenu;
	@ViewInject(R.id.mv_news_detail)
	private WebView mWebView;
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;
	
	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		
		ViewUtils.inject(this);
		llControl.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);
		
		btnBack.setOnClickListener(this);
		btnTextSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		
		mUrl = getIntent().getStringExtra("url");
		mWebView.loadUrl(mUrl);
//		mWebView.loadUrl("http://www.baidu.com");
		
		WebSettings settings = mWebView.getSettings();
		settings.setBuiltInZoomControls(true);//显示缩放按钮
		settings.setUseWideViewPort(true);//支持双击缩放
		settings.setJavaScriptEnabled(true);//支持js功能
		
		mWebView.setWebViewClient(new WebViewClient(){
			//开始加载网页
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("开始加载网页了");
				pbLoading.setVisibility(View.VISIBLE);
			}
			//网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("网页加载结束了");
				pbLoading.setVisibility(View.INVISIBLE);
			}
			//所有连接跳转会走此方法
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("跳转连接："+url);
				view.loadUrl(url);//在跳转连接时强制在当前的webView中加载
				return true;
			}
		});
		
		mWebView.setWebChromeClient(new WebChromeClient(){
			//进度发生变化
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				System.out.println("进度："+ newProgress);
			}
			//获得网页标题
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				System.out.println("网页标题："+title);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_textszie:
			//修改网页字体的大小
			showChooseDialog();
			break;
		case R.id.btn_share:
			showShare();
			break;

		default:
			break;
		}
	}
	
	private int mTempWhich;//记录临时选择的字体的大小（点击确定之前）
	private int mCurrentWhich =2;//记录当前选中的字体的大小（点击确定之后）,默认正常字体 

	/*
	 * 展示选择字体大小的弹窗
	 */
	private void showChooseDialog() {
		AlertDialog.Builder builder= new AlertDialog.Builder(this);
		builder.setTitle("字体设置");
		String [] items = new String[]{"超大号字体","大号字体","中号字体","小号字体","超小号字体"}; 
		builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempWhich = which;
			}
		});
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//根据选择的字体来修改网页字体大小
				
				WebSettings settings = mWebView.getSettings();
				switch (mTempWhich) {
				case 0:
					//超大字体
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					//大字体
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					//中号字体
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					//小号字体
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					//超小号字体
					settings.setTextSize(TextSize.SMALLEST);
					break;

				default:
					break;
				}
				
				mCurrentWhich = mTempWhich;
			} 
		});
		
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		
//		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("标题");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("ShareSDK");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
	
}
