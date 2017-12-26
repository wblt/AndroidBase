package cn.tthud.taitian.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.login.LoginActivity;
import cn.tthud.taitian.activity.mine.BindPhoneActivity;
import cn.tthud.taitian.bean.WebViewBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.widget.CustomAlertImgDialog;
import cn.tthud.taitian.widget.CustomAlertMsgDialog;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;


@SuppressLint("JavascriptInterface")
public class WebViewActivity extends ActivityBase {
	private WebView webView;
	private ProgressBar bar;
	private CustomAlertMsgDialog customAlertMsgDialog;
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

	private void webViewShow(String url) {
		WebSettings webSettings = webView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		webSettings.setSupportZoom(true);
//		/**
//		 * 解决webview不支持localstorage问题
//		 */
//		webSettings.setDomStorageEnabled(true);
//		webSettings.setDatabaseEnabled(true);
//		webSettings.setGeolocationEnabled(true);
//		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//		webSettings.setAppCachePath(appCachePath);
//		webSettings.setAllowFileAccess(true);
//		webSettings.setAppCacheEnabled(true);
//		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
//		webSettings.setLoadWithOverviewMode(true);
//		webSettings.setBuiltInZoomControls(true);
//		webSettings.setDisplayZoomControls(false);
//		webSettings.setAllowUniversalAccessFromFileURLs(true);
//		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹出窗口
//		webSettings.setDefaultTextEncodingName("UTF-8");
//		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//		webView.requestFocusFromTouch();
//		webView.setWebChromeClient(new WebChromeClient() {
//
//		});
//
//
//		//在js中调用本地java方法
//		webView.addJavascriptInterface(new JavaScriptinterface(this), "android");
//
////		//设置本地调用对象及其接口
//		webView.setWebViewClient(new WebViewClient(){
//
//		});
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		webView.addJavascriptInterface(new JavaScriptinterface(this), "android");
		//隐藏滚动条
		webView.setVerticalScrollBarEnabled(false);
		//触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件
		webView.requestFocus();
		//获取手势焦点
		webView.requestFocusFromTouch();
		//支持JavaScript
		webSettings.setJavaScriptEnabled(true);
		//支持访问文件
		webSettings.setAllowFileAccess(true);
		//将所有HTML放入webview组件中,固定宽度
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		//支持缩放
		webSettings.setBuiltInZoomControls(true);
		//支持自动加载图片
		webSettings.setLoadsImagesAutomatically(true);
		//支持自适应屏幕尺寸
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		//优先使用缓存:
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		//不使用缓存:
//        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		//设置默认编码:
		webSettings.setDefaultTextEncodingName("utf-8");
		//设置字体大小
//        getSettings().setTextZoom(140);
		webSettings.setSupportZoom(true);
		webView.loadUrl(url);
	}

	private class JavaScriptinterface {
		private Context mContext;
		public JavaScriptinterface(Context context){
			this.mContext = context;
		}

		@JavascriptInterface
		public void sendBtnClick(final String  name) {
			Log.i("++++++++++++++调起的方法"+name);
//			Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl("javascript:callJS('" + name + "')");
				}
			});

		}
	}

	public class WebViewClient extends android.webkit.WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			return super.shouldOverrideUrlLoading(view, request);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		/**
		 * alert弹框
		 *
		 * @return
		 */
		@Override
		public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
			Log.d("main", "onJsAlert:" + message);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					customAlertMsgDialog = new CustomAlertMsgDialog(WebViewActivity.this, R.style.dialog, message, new CustomAlertMsgDialog.ViewClickListener() {
						@Override
						public void onClick(View view) {
							customAlertMsgDialog.dismiss();
						}
					});
					customAlertMsgDialog.show();
				}
			});
			result.confirm();//这里必须调用，否则页面会阻塞造成假死
			return true;
		}
		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
									String acceptType) {
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
	}


	@Override
	protected void onDestroy() {
		webView.clearCache(true);   
		webView.clearHistory();
		webView.destroy();
		super.onDestroy();
	}
}
