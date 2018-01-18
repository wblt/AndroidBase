package cn.tthud.taitian.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import cn.tthud.taitian.adapter.ActivityMyWalletAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.bean.WalletRecordBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by bopeng on 2017/11/28.
 */

public class MyWalletActivity extends ActivityBase {

    @ViewInject(R.id.top_left)
    private ImageButton backBtn;

    @ViewInject(R.id.tv_score_number)
    private TextView tv_score_number;

    @ViewInject(R.id.btn_recharge)
    private Button btn_recharge;

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;

    private int mPage = 1;
    private int mMaxPage = -1;

    private ActivityMyWalletAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.activity_my_wallet);
        initListener();
        initRecyclerView();
        loadNewData(true);
    }

    private void initListener(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        page_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNewData(true);
            }
        });

        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyWalletActivity.this, RecharegeActivity.class));
            }
        });
    }

    private void initRecyclerView(){
        xrvCustom.setPullRefreshEnabled(true);
        xrvCustom.setLoadingMoreEnabled(true);

        xrvCustom.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadNewData(true);
            }

            @Override
            public void onLoadMore() {
                loadNewData(false);
            }
        });

        xrvCustom.setLayoutManager(new LinearLayoutManager(this));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ActivityMyWalletAdapter();
        xrvCustom.setAdapter(mAdapter);
    }

    private void loadNewData(boolean isRefresh){
        if (isRefresh){
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            mPage = 1;
        }else{
            mPage += 1;
        }

        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_MY_WALLET_RECORD);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("p", mPage);

        MXUtils.httpGet(requestParams, new CommonCallbackImp("我的钱包记录",requestParams){
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

                        String totalScore = jsonObject1.getString("totaljifen");


                        tv_score_number.setText(totalScore);

                        String maxPage = jsonObject1.getString("maxPage");
                        mMaxPage = Integer.parseInt(maxPage);

                        String list = jsonObject1.getString("list");

                        Type type=new TypeToken<List<WalletRecordBean>>(){}.getType();
                        List<WalletRecordBean> beanList = GsonUtils.jsonToList(list,type);

                        mAdapter.addAll(beanList);
                        mAdapter.notifyDataSetChanged();

                        // 防止加载更多动来动去
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
}
