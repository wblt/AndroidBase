package cn.tthud.taitian.activity.home;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
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
import cn.tthud.taitian.adapter.CompanyListAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.bean.CompanyBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by bopeng on 2017/11/20.
 */

public class CompanyListActivity extends ActivityBase {

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;

    private CompanyListAdapter mCompanyListAdapter;

    private int mPage;
    private int mMaxPage = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_company_list);

        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("企业列表");
        setTopLeftDefultListener();

        initRecyclerView();
        mPage = 1;
        loadNewData(true);
    }

    private void initRecyclerView(){
        xrvCustom.setPullRefreshEnabled(true);
        xrvCustom.setLoadingMoreEnabled(false);

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


        xrvCustom.setLayoutManager(new GridLayoutManager(this, 3));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());

        mCompanyListAdapter = new CompanyListAdapter();
        xrvCustom.setAdapter(mCompanyListAdapter);
    }

    private void loadNewData(boolean isRefresh){
        if (isRefresh){
            mCompanyListAdapter.clear();
            mCompanyListAdapter.notifyDataSetChanged();
            mPage = 1;
        }else{
            mPage += 1;
        }

        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_COMPANY_LIST);
        requestParams.addParameter("p", mPage);

        MXUtils.httpGet(requestParams, new CommonCallbackImp("公司查看更多",requestParams){
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

                        Type type=new TypeToken<List<CompanyBean>>(){}.getType();
                        List<CompanyBean> beanList = GsonUtils.jsonToList(list,type);

                        mCompanyListAdapter.addAll(beanList);
                        mCompanyListAdapter.notifyDataSetChanged();
                        if(mCompanyListAdapter.getData().size() == 0){
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
