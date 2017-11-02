package cn.tthud.taitian.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.FragmentAdapter;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.utils.Log;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by wb on 2017/10/8.
 */

public class DiscoverFragment extends FragmentBase implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{

    private View view;
    private ImageButton top_right_button;
    private ImageButton top_left_button;

    ActivityTypeFragment mDoingFragment;
    ActivityTypeFragment mUnbeginFragment;
    ActivityTypeFragment mEndFragment;

    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;

    @ViewInject(R.id.segmented)
    private SegmentedGroup mSegment;

    @ViewInject(R.id.rb_doing_btn)
    private RadioButton rb_doing_btn;

    @ViewInject(R.id.rb_unbegin_btn)
    private RadioButton rb_unbegin_btn;

    @ViewInject(R.id.rb_end_btn)
    private RadioButton rb_end_btn;

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
            setTopBarTitle("活动列表");

            initNaviBarView();
            initViews();
        }
        return view;
    }

    public void initNaviBarView(){
        top_right_button = (ImageButton) view.findViewById(R.id.top_right_icon);
        top_right_button.setImageResource(R.drawable.search);
        top_right_button.setOnClickListener(this);

        top_left_button = (ImageButton) view.findViewById(R.id.top_left);
        top_left_button.setImageResource(R.drawable.mine_setting);
        top_left_button.setOnClickListener(this);
    }
    public void initViews(){
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        mDoingFragment = ActivityTypeFragment.newInstance("start");         // 进行中
        mUnbeginFragment = ActivityTypeFragment.newInstance("notstart");    // 未开始
        mEndFragment = ActivityTypeFragment.newInstance("end");             // 已结束

        fragmentList.add(mDoingFragment);
        fragmentList.add(mUnbeginFragment);
        fragmentList.add(mEndFragment);

        mViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), fragmentList));
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mViewPager.setCurrentItem(0);

        mSegment.setOnCheckedChangeListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:         // 进行中
                        rb_doing_btn.setChecked(true);
                        rb_unbegin_btn.setChecked(false);
                        rb_end_btn.setChecked(false);
                        break;
                    case 1:         // 未开始
                        rb_doing_btn.setChecked(false);
                        rb_unbegin_btn.setChecked(true);
                        rb_end_btn.setChecked(false);
                        break;
                    case 2:         // 已结束
                        rb_doing_btn.setChecked(false);
                        rb_unbegin_btn.setChecked(false);
                        rb_end_btn.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.top_right_icon:
                Log.i("点击了右侧按钮");
                break;
            case R.id.top_left:
                Log.i("点击左侧按钮");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_doing_btn:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rb_unbegin_btn:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rb_end_btn:
                mViewPager.setCurrentItem(2);
                break;
        }
    }
}
