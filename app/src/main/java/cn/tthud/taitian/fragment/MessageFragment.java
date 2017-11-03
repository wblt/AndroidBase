package cn.tthud.taitian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.MessageAdapter;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by wb on 2017/10/8.
 */

public class MessageFragment extends FragmentBase implements View.OnClickListener {

    private View view;
    private XRecyclerView xrvCustom;
    private LinearLayout page_refresh;
    private int mPage;
    private int mMaxPage = -1;
    private MessageAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this, R.layout.message_fragment);
            appendTopBody(R.layout.activity_top_icon);
            ((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            setTopBarTitle("消息");

            xrvCustom = view.findViewById(R.id.xrv_custom);
            page_refresh = view.findViewById(R.id.page_refresh);
            initRecyclerView();
            setListener();

            mPage = 1;
            loadNewData(true);
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
                loadNewData(true);
            }

            @Override
            public void onLoadMore() {
                loadNewData(false);
            }
        });

        xrvCustom.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MessageAdapter();
        xrvCustom.setAdapter(mAdapter);
    }

    private void setListener(){
        page_refresh.setOnClickListener(this);
    }

    private void loadNewData(boolean isRefresh){
//        if (isRefresh){
//            mAdapter.clear();
//            mAdapter.notifyDataSetChanged();
//            mPage = 1;
//        }else{
//            mPage += 1;
//        }
//
//        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_ACTIVITY_LIST);
//        requestParams.addParameter("type","start");
//        requestParams.addParameter("p", mPage);
//
//        MXUtils.httpPost(requestParams, new CommonCallbackImp("活动列表--进行中",requestParams){
//            @Override
//            public void onSuccess(String data) {
//                super.onSuccess(data);
//                try {
//                    JSONObject jsonObject = new JSONObject(data);
//                    String status = jsonObject.getString("status");
//                    String info = jsonObject.getString("info");
//
//                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
//                        xrvCustom.refreshComplete();
//                        String result = jsonObject.getString("data");
//                        JSONObject jsonObject1 = new JSONObject(result);
//
//                        String maxPage = jsonObject1.getString("maxPage");
//                        mMaxPage = Integer.parseInt(maxPage);
//
//                        String list = jsonObject1.getString("list");
//
//                        Type type=new TypeToken<List<ActivityBean>>(){}.getType();
//                        List<ActivityBean> beanList = GsonUtils.jsonToList(list,type);
//
//                        mAdapter.addAll(beanList);
//                        mAdapter.notifyDataSetChanged();
//                        if(mAdapter.getData().size() == 0){
//                            page_refresh.setVisibility(View.VISIBLE);
//                            xrvCustom.setVisibility(View.GONE);
//                        }else {
//                            page_refresh.setVisibility(View.GONE);
//                            xrvCustom.setVisibility(View.VISIBLE);
//                        }
//
//                        if(mPage >= mMaxPage){
//                            xrvCustom.noMoreLoading();
//                        }
//
//                    }else {
//                        showMsg(info);
//                    }
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.page_refresh:
                loadNewData(true);
                break;
        }
    }
}
