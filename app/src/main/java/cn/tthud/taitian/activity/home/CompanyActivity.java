package cn.tthud.taitian.activity.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.ActivityDoingAdapter;
import cn.tthud.taitian.adapter.CompanyActivityAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.base.OnItemClickListener;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.ImageLoader;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class CompanyActivity extends ActivityBase {

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;

    private ImageView iv_companyIcon;
    private TextView tv_company_name;
    private TextView tv_fork_number;
    private TextView tv_activity_number;
    private ActivityDoingAdapter mAdapter;
    private int cid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.activity_company);
//        appendTopBody(R.layout.activity_top_text);
//        setTopBarTitle("公司详情");
//        setTopLeftDefultListener();
        ImageButton back = (ImageButton) findViewById(R.id.top_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_companyIcon = (ImageView) findViewById(R.id.headpic);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_fork_number = (TextView) findViewById(R.id.tv_fork_number);
        tv_activity_number = (TextView) findViewById(R.id.tv_activity_number);
        cid = getIntent().getIntExtra("cid",0);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        // 禁止下拉刷新
        xrvCustom.setPullRefreshEnabled(false);
        xrvCustom.setLoadingMoreEnabled(false);
        // 去掉刷新头
        xrvCustom.clearHeader();
        xrvCustom.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Log.i("刷新");
            }
            @Override
            public void onLoadMore() {
                Log.i("加载更多");
            }
        });
        xrvCustom.setLayoutManager(new LinearLayoutManager(CompanyActivity.this));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ActivityDoingAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<ActivityBean>() {
            @Override
            public void onClick(final ActivityBean activityBean, int position) {
                if (TextUtils.isEmpty(activityBean.getUrl())) {
                    return;
                }
                // 点击
                Intent intent = new Intent(CompanyActivity.this,WebViewActivity.class);
                intent.putExtra("title",activityBean.getTitle());
                intent.putExtra("url", activityBean.getUrl());
                CompanyActivity.this.startActivity(intent);
//                showProgressDialog();
//                if (TextUtils.isEmpty(SPUtils.getString(SPUtils.WX_OPEN_ID))){  // 判断微信id是否为空
//                    UMShareAPI.get(CompanyActivity.this).getPlatformInfo(CompanyActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
//                        @Override
//                        public void onStart(SHARE_MEDIA share_media) {
//
//                        }
//                        @Override
//                        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                            String openid = map.get("openid");
//                            SPUtils.putString(SPUtils.WX_OPEN_ID, openid);
//                            String profile_image_url = map.get("profile_image_url");
//                            SPUtils.putString(SPUtils.HEAD_PIC, profile_image_url);
//                            String gender = map.get("gender");
//                            if (gender.equals("男")){
//                                SPUtils.putInt(SPUtils.SEX, 1);
//                            }else if(gender.equals("女")){
//                                SPUtils.putInt(SPUtils.SEX, 2);
//                            }else{
//                                SPUtils.putInt(SPUtils.SEX, 0);
//                            }
//                            String name = map.get("name");
//                            SPUtils.putString(SPUtils.NICK_NAME, name);
//
//                            // 开始跳转
//                            dismissProgressDialog();
//                            String url = activityBean.getUrl();
//                            Intent intent = new Intent(CompanyActivity.this,WebViewActivity.class);
//                            intent.putExtra("title",activityBean.getTitle());
//                            //String url_str = addWXInfo(url);
//                            intent.putExtra("url", url);
//                            CompanyActivity.this.startActivity(intent);
//                        }
//
//                        @Override
//                        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//
//                        }
//
//                        @Override
//                        public void onCancel(SHARE_MEDIA share_media, int i) {
//
//                        }
//                    });
//                }else{
//                    dismissProgressDialog();
//                    Intent intent = new Intent(CompanyActivity.this,WebViewActivity.class);
//                    intent.putExtra("title",activityBean.getTitle());
//                    //String url_str = addWXInfo(activityBean.getUrl());
//                    intent.putExtra("url", activityBean.getUrl());
//                    CompanyActivity.this.startActivity(intent);
//                }
            }
        });
        mAdapter.setContext(this);
        xrvCustom.setAdapter(mAdapter);
    }

    private void loadData(){
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_COMPANY_DETAIL);
        requestParams.addParameter("id",cid);
        MXUtils.httpGet(requestParams, new CommonCallbackImp("公司详情",requestParams){
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

                        String title = jsonObject1.getString("title");
                        String thumb = jsonObject1.getString("thumb");
                        String collect = jsonObject1.getString("collect");
                        String act_num = jsonObject1.getString("act_num");
                        tv_company_name.setText(title);
                        tv_fork_number.setText(collect);
                        tv_activity_number.setText(act_num);
                        ImageLoader.loadCircle(thumb, iv_companyIcon);

                        String activity = jsonObject1.getString("activity");
                        JSONObject jsonObject2 = new JSONObject(activity);
                        String list = jsonObject2.getString("list");
                        Type type=new TypeToken<List<ActivityBean>>(){}.getType();
                        List<ActivityBean> acttList = GsonUtils.jsonToList(list,type);
                        mAdapter.addAll(acttList);
                        mAdapter.notifyDataSetChanged();
                        if(mAdapter.getData().size() == 0){
                            page_refresh.setVisibility(View.VISIBLE);
                            xrvCustom.setVisibility(View.GONE);
                        }else {
                            page_refresh.setVisibility(View.GONE);
                            xrvCustom.setVisibility(View.VISIBLE);
                        }
                        Log.i("ddd");
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private String addWXInfo(String url){
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
        url = url + "&html=" + "index";
        return url;
    }
}
