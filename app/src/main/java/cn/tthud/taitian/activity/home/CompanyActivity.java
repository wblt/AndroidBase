package cn.tthud.taitian.activity.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.CompanyActivityAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class CompanyActivity extends ActivityBase {

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;

    private CompanyActivityAdapter mAdapter;

    private int cid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.activity_company);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("公司详情");
        setTopLeftDefultListener();
        cid = getIntent().getIntExtra("cid",0);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        // 禁止下拉刷新
        xrvCustom.setPullRefreshEnabled(false);
        xrvCustom.setLoadingMoreEnabled(false);

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
        xrvCustom.setLayoutManager(new GridLayoutManager(CompanyActivity.this, 2));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new CompanyActivityAdapter();
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
                        String activity = jsonObject1.getString("activity");
                        JSONObject jsonObject2 = new JSONObject(activity);
                        String list = jsonObject2.getString("list");
                        Type type=new TypeToken<List<ActivityBean>>(){}.getType();
                        List<ActivityBean> acttList = GsonUtils.jsonToList(list,type);
                        xrvCustom.setVisibility(View.VISIBLE);
                        mAdapter.addAll(acttList);
                        mAdapter.notifyDataSetChanged();
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
}
