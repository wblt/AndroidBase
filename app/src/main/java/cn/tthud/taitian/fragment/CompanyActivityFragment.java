package cn.tthud.taitian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.CompanyActivityAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.utils.Log;

/**
 * Created by bopeng on 2017/11/6.
 */

public class CompanyActivityFragment extends FragmentBase {
    private View view;

    private XRecyclerView xrvCustom;
    private LinearLayout page_refresh;
    private CompanyActivityAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view =  inflater.inflate(R.layout.fragment_company_activity, null);
            xrvCustom = view.findViewById(R.id.xrv_custom);
            page_refresh = view.findViewById(R.id.page_refresh);
            initRecyclerView();
        }
        return view;
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


        xrvCustom.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new CompanyActivityAdapter();
        xrvCustom.setAdapter(mAdapter);
    }


    public void uploadUI() {
        loadData();
    }

    public void loadData(){
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle == null){
            return;
        }

        ArrayList<ActivityBean> activityBeanList = bundle.getParcelableArrayList("activityList");

        mAdapter.addAll(activityBeanList);
        mAdapter.notifyDataSetChanged();
        if(mAdapter.getData().size() == 0){
            page_refresh.setVisibility(View.VISIBLE);
            xrvCustom.setVisibility(View.GONE);
        }else {
            page_refresh.setVisibility(View.GONE);
            xrvCustom.setVisibility(View.VISIBLE);
        }
    }
}
