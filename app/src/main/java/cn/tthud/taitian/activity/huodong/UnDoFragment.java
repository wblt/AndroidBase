package cn.tthud.taitian.activity.huodong;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;

import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.FragmentBase;

/**
 * Created by bopeng on 2017/11/2.
 */

public class UnDoFragment extends FragmentBase {
    private View view;

    private XRecyclerView xrvCustom;
    private LinearLayout page_refresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view =  inflater.inflate(R.layout.fragment_activity_undo, null);
            xrvCustom = view.findViewById(R.id.xrv_custom);
            page_refresh = view.findViewById(R.id.page_refresh);

            initRecyclerView();
            setListener();
            loadNewData();
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

    private void loadNewData(){

    }
}
