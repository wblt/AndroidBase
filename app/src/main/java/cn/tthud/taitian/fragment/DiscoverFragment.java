package cn.tthud.taitian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.home.SearchActivity;
import cn.tthud.taitian.adapter.ActivityDoingAdapter;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.base.OnItemClickListener;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by wb on 2017/10/8.
 */

public class DiscoverFragment extends FragmentBase implements RadioGroup.OnCheckedChangeListener {

    private View view;
    @ViewInject(R.id.nts_bottom)
    private NavigationTabStrip ntsBottom;
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.segmented)
    private SegmentedGroup segmented;

    @ViewInject(R.id.sousuo_btn)
    private TextView sousuo_btn;

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;

    @ViewInject(R.id.query)
    private EditText query;

    @ViewInject(R.id.search_clear)
    private ImageView search_clear;

    private int mPage = 1;
    private int mMaxPage = -1;
    private ActivityDoingAdapter mAdapter;
    private String keywords = "";
    private String type = "start";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this, R.layout.discover_fragment);
            segmented.setOnCheckedChangeListener(this);
            //appendTopBody(R.layout.activity_top_icon);
            //((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            //setTopBarTitle("活动");
            initRecyclerView();
            loadNewData();
            setListener();
        }
        return view;
    }

    public void setListener(){
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

    private void initRecyclerView(){
        // 禁止下拉刷新
        xrvCustom.setPullRefreshEnabled(true);
        xrvCustom.setLoadingMoreEnabled(true);
        //xrvCustom.clearHeader();
        xrvCustom.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mPage = 1;
                loadNewData();
            }
            @Override
            public void onLoadMore() {
                mPage += 1;
                loadNewData();
            }
        });

        xrvCustom.setLayoutManager(new LinearLayoutManager(this.getActivity()));
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
                Intent intent = new Intent(getContext(),WebViewActivity.class);
                intent.putExtra("title",activityBean.getTitle());
                intent.putExtra("url", activityBean.getUrl());
                getContext().startActivity(intent);
            }
        });
        mAdapter.setContext(getContext());
        xrvCustom.setAdapter(mAdapter);
    }

    public void loadNewData(){
        showProgressDialog();
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_ACTIVITY_LIST);
        requestParams.addParameter("type",type);
        requestParams.addParameter("p", mPage);
        requestParams.addParameter("keywords",keywords);
        MXUtils.httpGet(requestParams, new CommonCallbackImp("活动列表--进行中",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                dismissProgressDialog();
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

    @Event(value = {R.id.ll_sousuo_lay,R.id.sousuo_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.ll_sousuo_lay:

                break;
            case R.id.sousuo_btn:
                keywords = query.getText().toString();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mPage = 1;
                loadNewData();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.button1:
                // 请求数据
                dismissProgressDialog();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mPage = 1;
                type = "start";
                keywords = "";
                query.setText("");
                loadNewData();
                break;
            case R.id.button2:
                // 请求数据
                dismissProgressDialog();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mPage = 1;
                keywords = "";
                query.setText("");
                type = "notstart";
                loadNewData();
                break;
            case R.id.button3:
                // 请求数据
                dismissProgressDialog();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mPage = 1;
                query.setText("");
                keywords = "";
                type = "end";
                loadNewData();
                break;
        }
    }




}
