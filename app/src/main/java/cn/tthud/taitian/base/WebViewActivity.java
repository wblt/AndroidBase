package cn.tthud.taitian.base;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.tthud.taitian.R;


@SuppressLint("JavascriptInterface")
public class WebViewActivity extends ActivityBase {
	
	private static final int RECHARGE_CODE = 50001;
	private static final int ADDRESS_CODE = 50002;
	
	private int backType = 0;  // 0 表示由自己控制，1表示由history
	
	public static final int URL_TYPE_NEWURL = 3001;
	
	private LinearLayout top_left;
	private TextView close;
	private WebView webView;
	private ProgressBar bar;
	public static final int WEBVIEW_RELOAD_RESULTCODE = 4;
	
	ValueCallback<Uri> mUploadMessage;
	public static final int FILECHOOSER_RESULTCODE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(this, R.layout.webview_activity_main);
		appendTopBody(R.layout.activity_top_text);
		setTopLeftDefultListener();
		initView();
		initData();
	}

	 
	// 初始化视图
	public void initView() {
		webView = (WebView) findViewById(R.id.webView);
		bar = (ProgressBar) findViewById(R.id.bar);
	}
	
	private void initData() {
		String url = getIntent().getExtras().getString("url");
		String title = getIntent().getExtras().getString("title");
		setTopBarTitle(title);
		webViewShow(url);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void webViewShow(String url) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);     
		webSettings.setSupportZoom(true);
		
		/**
		 * 解决webview不支持localstorage问题
		 */
		webSettings.setDomStorageEnabled(true);   
		webSettings.setDatabaseEnabled(true); 
		webSettings.setGeolocationEnabled(true);
		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
		webSettings.setAppCachePath(appCachePath);
		webSettings.setAllowFileAccess(true);  
		webSettings.setAppCacheEnabled(true); 
		
		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDisplayZoomControls(false);
		webSettings.setAllowUniversalAccessFromFileURLs(true);
		
		webSettings.setDefaultTextEncodingName("UTF-8");
//		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
		
		webView.requestFocusFromTouch();
		//设置本地调用对象及其接口  
		webView.setWebViewClient(new WebPageClient());
		webView.setWebChromeClient(new WebChromeClient() {
			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
			}

			// For Android < 3.0
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				openFileChooser(uploadMsg, "");
			}

			// For Android > 4.1.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
				openFileChooser(uploadMsg, acceptType);
			}
			
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				callback.invoke(origin, true, false); 
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
			@Override
			public void onProgressChanged(WebView view, int newProgress) {

			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});
		webView.loadUrl(url);
	}
	
	private class WebPageClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}


	@Override
	protected void onDestroy() {
		webView.clearCache(true);   
		webView.clearHistory();
		webView.destroy();
		super.onDestroy();
	}
}
