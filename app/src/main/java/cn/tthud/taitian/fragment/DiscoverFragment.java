package cn.tthud.taitian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.home.SearchActivity;
import cn.tthud.taitian.activity.huodong.DoingFragment;
import cn.tthud.taitian.activity.huodong.EndFragment;
import cn.tthud.taitian.activity.huodong.UnDoFragment;
import cn.tthud.taitian.adapter.FragmentAdapter;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.utils.Log;

/**
 * Created by wb on 2017/10/8.
 */

public class DiscoverFragment extends FragmentBase {

    private View view;
    @ViewInject(R.id.nts_bottom)
    private NavigationTabStrip ntsBottom;
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    DoingFragment mDoingFragment;
    UnDoFragment mUnbeginFragment;
    EndFragment mEndFragment;
    private int tab_index = 0;

    private boolean first_tab_doing = false;
    private boolean first_tab_end = false;
    private boolean first_tab_undo = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this, R.layout.discover_fragment);
            //appendTopBody(R.layout.activity_top_icon);
            //((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            //setTopBarTitle("活动");
            initView();
        }
        return view;
    }

    private void initView(){
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        mDoingFragment = new DoingFragment();
        mUnbeginFragment = new UnDoFragment();
        mEndFragment = new EndFragment();
        fragmentList.add(mDoingFragment);
        fragmentList.add(mUnbeginFragment);
        fragmentList.add(mEndFragment);
        mViewPager.setAdapter(new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList));
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        ntsBottom.setViewPager(mViewPager, 0);
        ntsBottom.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                //Log.i("index==="+index);
            }
            @Override
            public void onEndTabSelected(String title, int index) {
                Log.i("index==="+index);
                tab_index = index;
                // 点击时第一次加载数据
                if (tab_index == 1 && first_tab_undo == false) {
                    first_tab_undo = true;
                    // 加载未开始的数据
                    mUnbeginFragment.loadNewData();
                } else if (tab_index == 2 && first_tab_end == false) {
                    // 加载已经结束的数据
                    first_tab_end = true;
                    mEndFragment.loadNewData();
                }
            }
        });
    }

    @Event(value = {R.id.ll_sousuo_lay,R.id.sousuo_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.ll_sousuo_lay:
//                intent = new Intent(getContext(),SearchActivity.class);
//                if (tab_index == 0) {
//                    intent.putExtra("type","start");
//                } else if (tab_index == 1) {
//                    intent.putExtra("type","notstart");
//                } else if (tab_index == 2) {
//                    intent.putExtra("type","end");
//                }
//                startActivity(intent);
                break;
        }
    }

    public void tab_huodong() {
        if (first_tab_doing == false) {
            first_tab_doing = true;
            mDoingFragment.loadNewData();
        }
    }

}
