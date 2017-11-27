package cn.tthud.taitian.activity.home;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.FragmentAdapter;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.bean.CompanyBean;
import cn.tthud.taitian.fragment.CompanyActivityFragment;
import cn.tthud.taitian.fragment.CompanyIntroduceFragment;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

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
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_COMPANY_DETAIL);
        requestParams.addParameter("id","1");

        MXUtils.httpGet(requestParams, new CommonCallbackImp("公司详情",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                        CompanyBean companyBean = GsonUtils.jsonToBean(result, CompanyBean.class);
                        JSONObject jsonObject1 = new JSONObject(result);
                        String activity = jsonObject1.getString("activity");
                        JSONObject activityObject = new JSONObject(activity);
                        String list = activityObject.getString("list");
                        Type type=new TypeToken<List<ActivityBean>>(){}.getType();
                        List<ActivityBean> activityList = GsonUtils.jsonToList(list,type);
                        setData(companyBean);
                        mIntroduceFragment.loadData(companyBean.getContent());
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("activityList", (ArrayList<ActivityBean>)activityList);
                        mActivityFragment.setArguments(bundle);
                        mActivityFragment.uploadUI();
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setData(CompanyBean bean){
        tv_company_name.setText(bean.getTitle());
        tv_fork_number.setText(bean.getCollect());
        tv_activity_number.setText(bean.getAct_num());
    }
}
