package cn.tthud.taitian.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;


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

//	private static Context mContext;

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


		String nickname = SPUtils.getString(SPUtils.NICK_NAME);
		String headimgurl = SPUtils.getString(SPUtils.HEAD_PIC);
		String openid = SPUtils.getString(SPUtils.WX_OPEN_ID);
		int sex = SPUtils.getInt(SPUtils.SEX, 1);
		String ub_id = SPUtils.getString(SPUtils.UB_ID);
		String source = "app";
		String deviceid = UUID.randomUUID().toString();

		int index = url.indexOf("?");
		if (index == -1){		// 不存在
			url = url + "?source=" + source;
		}else{
			url = url + "&source=" + source;
		}

		url = url + "&deviceid=" + deviceid;
		url = url + "&sex=" + sex;

		if (nickname != null){
			url = url + "&nickname=" + URLEncoder.encode(nickname);
		}

		if (headimgurl != null){
			url = url + "&headimgurl=" + headimgurl;
		}
		if (openid != null){
			url = url + "&openid=" + openid;
		}

		if (ub_id != null){
			url = url + "&ub_id=" + ub_id;
		}

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

	static UMAuthListener authListener = new UMAuthListener() {
		@Override
		public void onStart(SHARE_MEDIA platform) {

		}
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

			String openid = data.get("openid");
			SPUtils.putString(SPUtils.WX_OPEN_ID, openid);

			String profile_image_url = data.get("profile_image_url");
			SPUtils.putString(SPUtils.HEAD_PIC, profile_image_url);

			String gender = data.get("gender");
			if (gender.equals("男")){
				SPUtils.putInt(SPUtils.SEX, 1);
			}else if(gender.equals("女")){
				SPUtils.putInt(SPUtils.SEX, 2);
			}else{
				SPUtils.putInt(SPUtils.SEX, 0);
			}
			String name = data.get("name");
			SPUtils.putString(SPUtils.NICK_NAME, name);

			// 绑定
			bingdingwx();
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Log.i("错误" + t.getMessage());
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {

		}
	};

	private static void bingdingwx(){
		RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_BIND_WX);
		requestParams.addParameter("isbindwx",-1);
		requestParams.addParameter("ub_id",SPUtils.getString(SPUtils.UB_ID));
		requestParams.addParameter("ua_id",SPUtils.getString(SPUtils.UA_ID));
		requestParams.addParameter("wx_openid",SPUtils.getString(SPUtils.WX_OPEN_ID));

		MXUtils.httpPost(requestParams, new CommonCallbackImp("绑定微信",requestParams){
			@Override
			public void onSuccess(String data) {
				super.onSuccess(data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					String status = jsonObject.getString("status");
					String info = jsonObject.getString("info");
					if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
						String result = jsonObject.getString("data");
						SPUtils.putBoolean(SPUtils.IS_BINDWX,true);
						//Toast.makeText(mContext, "绑定成功", Toast.LENGTH_LONG);
					}else {
						//Toast.makeText(mContext, info, Toast.LENGTH_LONG);
					}
				}catch (JSONException e){
					e.printStackTrace();
				}
			}
		});
	}

	public static void navToWebView(Context context, WebViewBean object){
		if (CommonUtils.checkLogin()) {  // 已登录
			if (!SPUtils.getBoolean(SPUtils.ISVST, false)) { // 非游客
				if (SPUtils.getBoolean(SPUtils.IS_BINDWX, false)){  // 绑定微信
					String url = object.getUrl();
					if (TextUtils.isEmpty(url)){
						return;
					}
					Intent intent = new Intent(context,WebViewActivity.class);
					intent.putExtra("title",object.getTitle());
					intent.putExtra("url", url);
					context.startActivity(intent);
				}else{
					UMShareAPI.get(context).getPlatformInfo((Activity) context, SHARE_MEDIA.WEIXIN, authListener);
				}
			} else {
				context.startActivity(new Intent(context, BindPhoneActivity.class));
			}
		} else {
			LoginActivity.navToLogin(context);
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
