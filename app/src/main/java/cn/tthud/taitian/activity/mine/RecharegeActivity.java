package cn.tthud.taitian.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Button;
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
import cn.tthud.taitian.activity.home.CompanyActivity;
import cn.tthud.taitian.adapter.ActivityRechargeAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.base.OnItemClickListener;
import cn.tthud.taitian.bean.RechargeBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by bopeng on 2017/11/29.
 */

public class RecharegeActivity extends ActivityBase {

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;

    @ViewInject(R.id.tv_name)
    private TextView tv_name;

    @ViewInject(R.id.tv_score)
    private TextView tv_score;

    @ViewInject(R.id.btn_recharge)
    private Button btn_recharge;

    private ActivityRechargeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_recharge);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("充值");
        setTopLeftDefultListener();

        initRecyclerView();
        initListener();
        loadData();
    }

    private void initListener(){
        page_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
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
        xrvCustom.setLayoutManager(new GridLayoutManager(RecharegeActivity.this, 3));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ActivityRechargeAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(Object o, int position) {
                // 这里就是回调,对象就为当前选中的
            }
        });
        xrvCustom.setAdapter(mAdapter);
    }

    private void loadData(){
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_RECHARGE);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        MXUtils.httpGet(requestParams, new CommonCallbackImp("充值界面",requestParams){
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

                        String totalScore = jsonObject1.getString("totaljifen");
                        String realName = jsonObject1.getString("realname");
                        tv_name.setText(realName);
                        tv_score.setText(totalScore + "积分");

                        String list = jsonObject1.getString("list");
                        Type type=new TypeToken<List<RechargeBean>>(){}.getType();
                        List<RechargeBean> acttList = GsonUtils.jsonToList(list,type);
                        xrvCustom.setVisibility(View.VISIBLE);
                        mAdapter.addAll(acttList);
                        mAdapter.notifyDataSetChanged();

                        if(mAdapter.getData().size() == 0){
                            page_refresh.setVisibility(View.VISIBLE);
                            xrvCustom.setVisibility(View.GONE);
                            btn_recharge.setVisibility(View.GONE);
                        }else {
                            page_refresh.setVisibility(View.GONE);
                            xrvCustom.setVisibility(View.VISIBLE);
                            btn_recharge.setVisibility(View.VISIBLE);
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
