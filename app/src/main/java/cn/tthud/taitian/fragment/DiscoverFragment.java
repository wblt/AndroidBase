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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this, R.layout.discover_fragment);
            appendTopBody(R.layout.activity_top_icon);
            ((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            setTopBarTitle("活动");
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
    }

    @Event(value = {R.id.ll_sousuo_lay},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.ll_sousuo_lay:
                intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
                break;
        }
    }



}
