package cn.tthud.taitian.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
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
import cn.tthud.taitian.utils.ImageLoader;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class TaskActivity extends ActivityBase {

    @ViewInject(R.id.headpic)
    private ImageView headpic;

    @ViewInject(R.id.username)
    private TextView username;

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;

    @ViewInject(R.id.num)
    private TextView num;

    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_task);
        initRecyclerView();
        showLoading();
        loadData();
    }

    private void loadData() {
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_TASK);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        MXUtils.httpGet(requestParams, new CommonCallbackImp("任务",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                cancelLoading();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(result);
                        String user = jsonObject1.getString("user");
                        JSONObject userJson = new JSONObject(user);
                        String headpicurl = userJson.getString("headpic");
                        String nickname = userJson.getString("nickname");
                        int sex = userJson.getInt("sex");
                        ImageLoader.loadCircle(headpicurl,headpic);
                        if (TextUtils.isEmpty(nickname)) {
                            nickname = SPUtils.getString(SPUtils.NICK_NAME);
                        }
                        if (sex == 1) {
                            username.setText(nickname+" · "+"男");
                        } else if (sex == 2){
                            username.setText(nickname+" · "+"女");
                        }else{
                            username.setText(nickname+" . "+"");
                        }
                        JSONArray taskArr = jsonObject1.getJSONArray("data");
                        if (taskArr.length() > 0) {
                            JSONObject taskObject = taskArr.getJSONObject(0);
                            String task = taskObject.getString("task");
                            Type type=new TypeToken<List<TaskBean>>(){}.getType();
                            List<TaskBean> acttList = GsonUtils.jsonToList(task,type);
                            num.setText("( 共"+acttList.size()+"个 )");
                            mAdapter.addAll(acttList);
                            mAdapter.notifyDataSetChanged();
                            if(mAdapter.getData().size() == 0){
                                page_refresh.setVisibility(View.VISIBLE);
                                xrvCustom.setVisibility(View.GONE);
                            }else {
                                page_refresh.setVisibility(View.GONE);
                                xrvCustom.setVisibility(View.VISIBLE);
                            }
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
