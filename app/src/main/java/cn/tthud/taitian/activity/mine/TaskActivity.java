package cn.tthud.taitian.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.TaskAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.bean.TaskBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class TaskActivity extends ActivityBase {

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;

    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_task);
//        appendTopBody(R.layout.activity_top_text);
//        setTopBarTitle("我的任务");
//        setTopLeftDefultListener();

        initRecyclerView();

        List<TaskBean> list = new ArrayList<>();
        for (int i = 0;i<10;i++) {
            TaskBean taskBean = new TaskBean();
            list.add(taskBean);
        }
        xrvCustom.setVisibility(View.VISIBLE);
        mAdapter.addAll(list);
        mAdapter.notifyDataSetChanged();
        //loadData();
    }

    private void loadData() {
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_TASK);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        MXUtils.httpPost(requestParams, new CommonCallbackImp("任务",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
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
        xrvCustom.setLayoutManager(new LinearLayoutManager(this));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new TaskAdapter();
        xrvCustom.setAdapter(mAdapter);
    }

}
