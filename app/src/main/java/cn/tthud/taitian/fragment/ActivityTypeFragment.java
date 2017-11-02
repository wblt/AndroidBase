package cn.tthud.taitian.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;


public class ActivityTypeFragment extends FragmentBase {
    private static final String ARG_TYPE = "type";
    private View view;
    private String mType;
    private int mPage;

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;
    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;


    public static ActivityTypeFragment newInstance(String type) {
        ActivityTypeFragment fragment = new ActivityTypeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = super.onCreateView(inflater, container, savedInstanceState);
            appendMainBody(this, R.layout.fragment_activity_type);
            Bundle bundle = getArguments();
            if (bundle != null) {
                mType = (String) bundle.getString("type");
            }
            mPage = 1;
            initRecyclerView();
            setListener();
            loadNewData(mType);
        }
        return view;
    }

    private void initRecyclerView(){
        // 禁止下拉刷新
        xrvCustom.setPullRefreshEnabled(true);
        xrvCustom.setLoadingMoreEnabled(true);

        xrvCustom.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                loadNewData(mType);
            }

            @Override
            public void onLoadMore() {
//                loadMoreData(mType);
            }
        });

        xrvCustom.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());

    }

    private void setListener(){

    }

    private void loadNewData(String type) {
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_ACTIVITY_LIST);
        requestParams.addParameter("p",mPage + "");
        requestParams.addParameter("type", mType);

//        MXUtils.httpGet(requestParams, new CommonCallbackImp("获取活动列表",requestParams){
//            @Override
//            public void onSuccess(String data) {
//                super.onSuccess(data);
//                try {
//                    JSONObject jsonObject = new JSONObject(data);
//                    String status = jsonObject.getString("status");
//                    String info = jsonObject.getString("info");
//
//                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
//                        Log.i(jsonObject.getString("list"));
//                    }else {
//                        showMsg(info);
//                    }
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private void loadMoreData(String type){

    }
}
