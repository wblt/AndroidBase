package cn.tthud.taitian.activity.home;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by bopeng on 2017/12/7.
 */

public class ArtonceActivity extends ActivityBase {

    @ViewInject(R.id.web_content)
    private WebView webView;

    private String cid;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_artonce);
        appendTopBody(R.layout.activity_top_text);

        cid = getIntent().getExtras().getString("cid");
        title = getIntent().getExtras().getString("title");
        if (title.length() > 4){
            title = title.substring(0, 4);
        }

        setTopBarTitle(title);
        setTopLeftDefultListener();

        loadData();
    }

    private void loadData(){



        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_ART_ONCE);
        requestParams.addParameter("id",cid);
        MXUtils.httpGet(requestParams, new CommonCallbackImp("单页接口",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(result);
                        String content = jsonObject1.getString("content");
                        String title = jsonObject1.getString("title");
                        String h5_title = "<h1 style=\"font-size: 20px;text-align: center;margin-left: 10%;width: 80%;margin-top: 10px;\">" + title + "</h1>";

                        webView.loadData(h5_title + content, "text/html; charset=UTF-8", null);
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
