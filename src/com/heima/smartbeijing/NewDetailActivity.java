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
 * ���ŵ�����ҳ��
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
		settings.setBuiltInZoomControls(true);//��ʾ���Ű�ť
		settings.setUseWideViewPort(true);//֧��˫������
		settings.setJavaScriptEnabled(true);//֧��js����
		
		mWebView.setWebViewClient(new WebViewClient(){
			//��ʼ������ҳ
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("��ʼ������ҳ��");
				pbLoading.setVisibility(View.VISIBLE);
			}
			//��ҳ���ؽ���
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("��ҳ���ؽ�����");
				pbLoading.setVisibility(View.INVISIBLE);
			}
			//����������ת���ߴ˷���
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("��ת���ӣ�"+url);
				view.loadUrl(url);//����ת����ʱǿ���ڵ�ǰ��webView�м���
				return true;
			}
		});
		
		mWebView.setWebChromeClient(new WebChromeClient(){
			//���ȷ����仯
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				System.out.println("���ȣ�"+ newProgress);
			}
			//�����ҳ����
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				System.out.println("��ҳ���⣺"+title);
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
			//�޸���ҳ����Ĵ�С
			showChooseDialog();
			break;
		case R.id.btn_share:
			showShare();
			break;

		default:
			break;
		}
	}
	
	private int mTempWhich;//��¼��ʱѡ�������Ĵ�С�����ȷ��֮ǰ��
	private int mCurrentWhich =2;//��¼��ǰѡ�е�����Ĵ�С�����ȷ��֮��,Ĭ���������� 

	/*
	 * չʾѡ�������С�ĵ���
	 */
	private void showChooseDialog() {
		AlertDialog.Builder builder= new AlertDialog.Builder(this);
		builder.setTitle("��������");
		String [] items = new String[]{"���������","�������","�к�����","С������","��С������"}; 
		builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempWhich = which;
			}
		});
		
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//����ѡ����������޸���ҳ�����С
				
				WebSettings settings = mWebView.getSettings();
				switch (mTempWhich) {
				case 0:
					//��������
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					//������
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					//�к�����
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					//С������
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					//��С������
					settings.setTextSize(TextSize.SMALLEST);
					break;

				default:
					break;
				}
				
				mCurrentWhich = mTempWhich;
			} 
		});
		
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		
//		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle("����");
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("���Ƿ����ı�");
		// ��������ͼƬ������΢����������ͼƬ��Ҫͨ����˺�����߼�д��ӿڣ�������ע�͵���������΢��
		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite("ShareSDK");
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		oks.show(this);
	}
	
}
