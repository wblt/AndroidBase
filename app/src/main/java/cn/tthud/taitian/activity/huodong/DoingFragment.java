package cn.tthud.taitian.activity.huodong;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.ActivityDoingAdapter;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by bopeng on 2017/11/2.
 */

public class DoingFragment extends FragmentBase implements View.OnClickListener {
    private View view;

    private TextView sousuo_btn;
    private XRecyclerView xrvCustom;
    private LinearLayout page_refresh;
    //private TextView up_tip;
    private int mPage = 1;
    private int mMaxPage = -1;
    private ActivityDoingAdapter mAdapter;
    private String keywords = "";
    private EditText query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view =  inflater.inflate(R.layout.fragment_activity_doing, null);
            sousuo_btn = view.findViewById(R.id.sousuo_btn);
            xrvCustom = view.findViewById(R.id.xrv_custom);
            page_refresh = view.findViewById(R.id.page_refresh);
            query = view.findViewById(R.id.query);
            initRecyclerView();
            setListener();
            //loadNewData();
        }
        return view;
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
        mAdapter.setContext(getContext());
        xrvCustom.setAdapter(mAdapter);
    }

    private void setListener(){
        page_refresh.setOnClickListener(this);
        sousuo_btn.setOnClickListener(this);
    }

    public void loadNewData(){
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_ACTIVITY_LIST);
        requestParams.addParameter("type","start");
        requestParams.addParameter("p", mPage);
        requestParams.addParameter("keywords",keywords);
        MXUtils.httpGet(requestParams, new CommonCallbackImp("活动列表--进行中",requestParams){
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

                        //xrvCustom.loadMoreComplete();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.page_refresh:
//                mAdapter.clear();
//                mPage = 1;
//                loadNewData();
                break;
            case R.id.sousuo_btn:
                keywords = query.getText().toString();
                mPage = 1;
                mAdapter.clear();
                loadNewData();
                break;
        }
    }
}
