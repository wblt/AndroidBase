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
import android.view.KeyEvent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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
import cn.tthud.taitian.bean.WeiXinBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.GsonUtils;
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
	private View top_left;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(this, R.layout.webview_activity_main);
		appendTopBody(R.layout.activity_top_text);
		//setTopLeftDefultListener();
		initView();
		initData();
	}
	// 初始化视图
	public void initView() {
		webView = (WebView) findViewById(R.id.webView);
		bar = (ProgressBar) findViewById(R.id.bar);
		top_left = findViewById(R.id.top_left);
		top_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(webView.canGoBack()){
					webView.goBack();//返回上个页面
				} else {
					finish();
				}
			}
		});
	}


	private void initData() {
		String url = getIntent().getExtras().getString("url");
		String title = getIntent().getExtras().getString("title");
		setTopBarTitle(title);
		webViewShow(url);
	}

	private void webViewShow(String url) {
		WebSettings webSettings = webView.getSettings();
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
		//webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
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
		public void sendBtnClick(String name) {
			Log.i("++++++++++++++调起的方法"+name);
			try {
				JSONObject jsonObject = new JSONObject(name);
				String type = jsonObject.getString("type");
				if (type.equals("user")) {
					// 获取微信信息
					getWeixin();
				} else if (type.equals("pay")){
					// 去微信支付


					showMsg("我已经千辛万苦的走入支付了");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		@JavascriptInterface
		public void wechatPay(String type,String data) {
			showMsg("我已经千辛万苦的走入支付了");
			Log.i("+++++wechatPay+++++++++调起的方法"+type+ " " + data);
			try {
				JSONObject object = new JSONObject(data);
				String dataJoson = object.getString("data");
				JSONObject payjosnObjec = new JSONObject(dataJoson);
				String paydata = payjosnObjec.getString("paydata");
				JSONObject pay = new JSONObject(paydata);
				pay(pay);
			} catch (JSONException e) {
				e.printStackTrace();
			}
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

	private void getWeixin() {
		showProgressDialog();
		if (TextUtils.isEmpty(SPUtils.getString(SPUtils.WX_OPEN_ID))){  // 判断微信id是否为空
			UMShareAPI.get(WebViewActivity.this).getPlatformInfo(WebViewActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
				@Override
				public void onStart(SHARE_MEDIA share_media) {

				}
				@Override
				public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
					String openid = map.get("openid");
					SPUtils.putString(SPUtils.WX_OPEN_ID, openid);
					String profile_image_url = map.get("profile_image_url");
					SPUtils.putString(SPUtils.HEAD_PIC, profile_image_url);
					String gender = map.get("gender");
					if (gender.equals("男")){
						SPUtils.putInt(SPUtils.SEX, 1);
					}else if(gender.equals("女")){
						SPUtils.putInt(SPUtils.SEX, 2);
					}else{
						SPUtils.putInt(SPUtils.SEX, 0);
					}
					String name = map.get("name");
					SPUtils.putString(SPUtils.NICK_NAME, name);

					// 开始调起js
					calluser();
				}
				@Override
				public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

				}
				@Override
				public void onCancel(SHARE_MEDIA share_media, int i) {

				}
			});
		}else{
			// 调起js
			calluser();
		}
	}

	private void calluser() {
		dismissProgressDialog();
		String nickname = SPUtils.getString(SPUtils.NICK_NAME);
		String headimgurl = SPUtils.getString(SPUtils.HEAD_PIC);
		String openid = SPUtils.getString(SPUtils.WX_OPEN_ID);
		int sex = SPUtils.getInt(SPUtils.SEX, 1);
		String ub_id = SPUtils.getString(SPUtils.UB_ID);
		String deviceid = UUID.randomUUID().toString();
		WeiXinBean weiXinBean = new WeiXinBean();
		weiXinBean.setDeviceid(deviceid);
		weiXinBean.setNickname(nickname);
		weiXinBean.setOpenid(openid);
		weiXinBean.setSex(String.valueOf(sex));
		weiXinBean.setUb_id(ub_id);
		weiXinBean.setHeadimgurl(headimgurl);
		final String jsonStr = GsonUtils.beanToJson(weiXinBean);
		Log.i("++++++++拼接的json字符"+jsonStr);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String parms = "javascript:calluser('" + jsonStr + "')";
				Log.i("=========parms"+parms);
				webView.loadUrl(parms);
			}
		});
	}

	@Override
	protected void onDestroy() {
		webView.clearCache(true);   
		webView.clearHistory();
		webView.destroy();
		super.onDestroy();
	}

	private void pay(JSONObject jsonObject) {
		try {
			String aPackage = "Sign=WXPay";
			String appid = jsonObject.getString("appid");
			String sign = jsonObject.getString("sign");
			String partnerid = jsonObject.getString("partnerId");
			String prepayid = jsonObject.getString("prepay_id");
			String noncestr = jsonObject.getString("nonce_str");
			String timestamp = jsonObject.getString("timestamp");
			IWXAPI api = WXAPIFactory.createWXAPI(this, appid);
			api.registerApp(appid);
			PayReq payReq = new PayReq();
			payReq.appId = appid;
			payReq.partnerId = partnerid;
			payReq.prepayId = prepayid;
			payReq.packageValue = "Sign=WXPay";
			payReq.nonceStr = noncestr;
			payReq.timeStamp = timestamp;
			payReq.sign = sign;
			api.sendReq(payReq);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
			webView.goBack();//返回上个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);//退出整个应用程序
	}
}
