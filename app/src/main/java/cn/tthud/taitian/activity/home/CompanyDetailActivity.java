package cn.tthud.taitian.activity.home;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.FragmentAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.fragment.CompanyActivityFragment;
import cn.tthud.taitian.fragment.CompanyIntroduceFragment;

public class CompanyDetailActivity extends ActivityBase {

    @ViewInject(R.id.viewpager)
    ViewPager mViewPager;

    @ViewInject(R.id.nts_bottom)
    NavigationTabStrip ntsBottom;

    @ViewInject(R.id.tv_company_name)
    TextView tv_company_name;

    @ViewInject(R.id.tv_fork_number)
    TextView tv_fork_number;

    @ViewInject(R.id.tv_activity_number)
    TextView tv_activity_number;

    CompanyIntroduceFragment mIntroduceFragment;
    CompanyActivityFragment mActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_company_detail);

        ImageButton iv_back = (ImageButton) findViewById(R.id.top_left);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();
        loadData();
    }

    private void initView(){
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        mIntroduceFragment = new CompanyIntroduceFragment();
        mActivityFragment = new CompanyActivityFragment();

        fragmentList.add(mIntroduceFragment);
        fragmentList.add(mActivityFragment);

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        ntsBottom.setViewPager(mViewPager, 0);
    }

    private void loadData(){

    }
}
