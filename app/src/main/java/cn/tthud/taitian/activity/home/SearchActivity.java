package cn.tthud.taitian.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.ActivityDoingAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.base.OnItemClickListener;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class SearchActivity extends ActivityBase {

    @ViewInject(R.id.query)
    private EditText query;

    @ViewInject(R.id.search_clear)
    private ImageView search_clear;

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;
    private ActivityDoingAdapter mAdapter;
    private int mPage;
    private int mMaxPage = -1;
    private String  keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.activity_search);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("搜索");
        setTopLeftDefultListener();
        initRecyclerView();
        setListener();
    }

    public void setListener(){
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // adapter.getFilter().filter(s);
                if (s.length() > 0) {
                    search_clear.setVisibility(View.VISIBLE);
                } else {
                    search_clear.setVisibility(View.INVISIBLE);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
            }
        });
    }

    @Event(value = {R.id.sousuo_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view){
        switch (view.getId()) {
            case R.id.sousuo_btn:
                keywords = query.getText().toString();
                // 先清除上次搜索的数据
                mAdapter.clear();
                xrvCustom.refreshComplete();
                mAdapter.notifyDataSetChanged();
                loadData();
                break;
        }
    }

    private void initRecyclerView(){
        // 禁止下拉刷新
        xrvCustom.setPullRefreshEnabled(true);
        xrvCustom.setLoadingMoreEnabled(true);
        // 去掉刷新头
        //xrvCustom.clearHeader();
        xrvCustom.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Log.i("刷新");
                mPage = 1;
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                loadData();
            }
            @Override
            public void onLoadMore() {
                Log.i("加载更多");
                mPage += 1;
                loadData();
            }
        });
        xrvCustom.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intent = new Intent(SearchActivity.this,WebViewActivity.class);
                intent.putExtra("title",activityBean.getTitle());
                intent.putExtra("url", activityBean.getUrl());
                SearchActivity.this.startActivity(intent);
            }
        });
        xrvCustom.setAdapter(mAdapter);
    }

    private void loadData() {
        if (TextUtils.isEmpty(keywords)) {
            xrvCustom.refreshComplete();
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            return;
        }
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_SEARCH_ACT);
        requestParams.addParameter("keywords",keywords);
        String type = getIntent().getStringExtra("type");
        if (!type.equals("home")) {
            requestParams.addParameter("type",type);
        }
        requestParams.addParameter("p", mPage);
        MXUtils.httpGet(requestParams, new CommonCallbackImp("活动搜索",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");

                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        xrvCustom.refreshComplete();
                        String result = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(result);
                        String maxPage = jsonObject1.getString("maxPage");
                        mMaxPage = Integer.parseInt(maxPage);
                        String list = jsonObject1.getString("list");
                        Type type=new TypeToken<List<ActivityBean>>(){}.getType();
                        List<ActivityBean> beanList = GsonUtils.jsonToList(list,type);
                        mAdapter.addAll(beanList);
                        mAdapter.notifyDataSetChanged();
                        if(mAdapter.getData().size() == 0){
                            page_refresh.setVisibility(View.VISIBLE);
                            xrvCustom.setVisibility(View.GONE);
                        }else {
                            page_refresh.setVisibility(View.GONE);
                            xrvCustom.setVisibility(View.VISIBLE);
                        }
                        if(mPage >= mMaxPage){
                            xrvCustom.noMoreLoading();
                        }
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
