package cn.tthud.taitian.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.FragmentBase;

/**
 * Created by bopeng on 2017/11/6.
 */

public class CompanyIntroduceFragment extends FragmentBase {
    private View view;
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view =  inflater.inflate(R.layout.fragment_company_introduce, null);
            mWebView = (WebView) view.findViewById(R.id.webView);
        }
        return view;
    }

    public void loadData(String url){
        if (TextUtils.isEmpty(url)) return;

        webViewShow(url);
    }

    private void webViewShow(String url){
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);

        mWebView.loadData(url, "text/html; charset=UTF-8", null);
    }
}
