package cn.tthud.taitian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.home.ArtonceActivity;
import cn.tthud.taitian.activity.home.MoreIPActivity;
import cn.tthud.taitian.activity.home.MoreStarActivity;
import cn.tthud.taitian.activity.home.SearchActivity;
import cn.tthud.taitian.adapter.ActivityDoingAdapter;
import cn.tthud.taitian.adapter.CompanyListAdapter;
import cn.tthud.taitian.adapter.GoodIPAdapter;
import cn.tthud.taitian.adapter.StarXueyuanAdapter;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.bean.AdmarketBean;
import cn.tthud.taitian.bean.CompanyBean;
import cn.tthud.taitian.bean.StarXueyuanBean;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.widget.banner.BannerItem;
import cn.tthud.taitian.widget.banner.SimpleImageBanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by wb on 2017/10/8.
 */

public class HomeFragment extends FragmentBase {

    private View view;
    @ViewInject(R.id.sib_simple_usage)
    private SimpleImageBanner sib_simple_usage;

    @ViewInject(R.id.xrv_custom_qiyan)
    private XRecyclerView xrvCustom_qiyan;

    @ViewInject(R.id.page_refresh_qiyan)
    private LinearLayout page_refresh_qiyan;

    @ViewInject(R.id.xrv_custom_xueyuan)
    private XRecyclerView xrvCustom_xueyuan;

    @ViewInject(R.id.page_refresh_xueyuan)
    private LinearLayout page_refresh_xueyuan;

    @ViewInject(R.id.xrv_custom_ip)
    private XRecyclerView xrvCustom_ip;

    @ViewInject(R.id.page_refresh_ip)
    private LinearLayout page_refresh_ip;

    @ViewInject(R.id.iv_seg_line1)
    private ImageView iv_seg_line1;

    @ViewInject(R.id.iv_seg_line2)
    private ImageView iv_seg_line2;

    @ViewInject(R.id.ll_ip_lay)
    private LinearLayout ll_ip_lay;

    @ViewInject(R.id.ll_xueyuan_lay)
    private LinearLayout ll_xueyuan_lay;

    @ViewInject(R.id.ll_sousuo_lay)
    private LinearLayout ll_sousuo_lay;


    private CompanyListAdapter adapter_qiyan;
    private StarXueyuanAdapter adapter_xueyuan;
    private ActivityDoingAdapter adapter_ip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this, R.layout.home_fragment);
            appendTopBody(R.layout.activity_top_icon);
            ((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            setTopBarTitle("首页");
            initRecyclerView_qiyan();
            initRecyclerView_xueyuan();
            initRecyclerView_ip();
            loadData();
        }
        return view;
    }

    @Event(value = {R.id.ll_more_ip,R.id.ll_more_star,R.id.ll_sousuo_lay},type = View.OnClickListener.class)
    private void onEvenOnclick(View view){
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.ll_more_ip:
                intent = new Intent(getContext(), MoreIPActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_more_star:
                intent = new Intent(getContext(), MoreStarActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sousuo_lay:
                intent = new Intent(getContext(),SearchActivity.class);
                intent.putExtra("type","home");
                startActivity(intent);
                break;

        }
    }

    private void loadData(){
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_HOME_LIST);
        MXUtils.httpGet(requestParams, new CommonCallbackImp("首页",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(result);

                        String admarket = jsonObject1.getString("admarket");
                        Type ad_type = new TypeToken<List<AdmarketBean>>(){}.getType();
                        List<AdmarketBean> admarketList = GsonUtils.jsonToList(admarket, ad_type);
                        setBanner(admarketList);

                        ll_sousuo_lay.setVisibility(View.VISIBLE);

                        // 企业
                        String company = jsonObject1.getString("company");
                        Type type=new TypeToken<List<CompanyBean>>(){}.getType();
                        List<CompanyBean> companyList = GsonUtils.jsonToList(company,type);
                        xrvCustom_qiyan.setVisibility(View.VISIBLE);
                        adapter_qiyan.addAll(companyList);
                        adapter_qiyan.notifyDataSetChanged();

                        // star
                        String star = jsonObject1.getString("star");
                        Type type_star =new TypeToken<List<StarXueyuanBean>>(){}.getType();
                        List<StarXueyuanBean> startList = GsonUtils.jsonToList(star,type_star);
                        adapter_xueyuan.addAll(startList);
                        adapter_xueyuan.notifyDataSetChanged();
                        if (adapter_xueyuan.getData().size() != 0) {
                            page_refresh_xueyuan.setVisibility(View.GONE);
                            iv_seg_line1.setVisibility(View.VISIBLE);
                            ll_xueyuan_lay.setVisibility(View.VISIBLE);
                            xrvCustom_xueyuan.setVisibility(View.VISIBLE);
                        }

                        // ip
                        String activity = jsonObject1.getString("activity");
                        Type type_act =new TypeToken<List<ActivityBean>>(){}.getType();
                        List<ActivityBean> acttList = GsonUtils.jsonToList(activity,type_act);

                        adapter_ip.addAll(acttList);
                        adapter_ip.notifyDataSetChanged();
                        if (adapter_ip.getData().size() != 0) {
                            page_refresh_ip.setVisibility(View.GONE);
                            iv_seg_line2.setVisibility(View.VISIBLE);
                            ll_ip_lay.setVisibility(View.VISIBLE);
                            xrvCustom_ip.setVisibility(View.VISIBLE);
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


    public static List<BannerItem> getBanner(List<String> alist) {
        ArrayList<BannerItem> list = new ArrayList<BannerItem>();
        for (int i = 0; i < alist.size(); i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = alist.get(i);
            list.add(item);
        }
        return list;
    }

    // 顶部广告
    private void setBanner(final List<AdmarketBean> model) {

        if (model.size() == 0){
            return;
        }

        List<String> urls = new ArrayList<String>();
        for (int i = 0;i<model.size();i++) {
            AdmarketBean bean = model.get(i);
            urls.add(bean.getImg());
        }

        sib_simple_usage.setSource(getBanner(urls)).startScroll();
        sib_simple_usage.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                AdmarketBean adBean = model.get(position);

                if (adBean.getIshref().equals("2")) return;

                if (TextUtils.isEmpty(adBean.getUrl())){
                    if (TextUtils.isEmpty(adBean.getModule())) return;

                    if (adBean.getModule().equals("artonce")){
                        Intent intent = new Intent(HomeFragment.this.getActivity(), ArtonceActivity.class);
                        intent.putExtra("cid", adBean.getModule_id());
                        intent.putExtra("title", "单页详情");
                        startActivity(intent);
                    }else if (adBean.getModule().equals("admarket")){

                    }else if (adBean.getModule().equals("article")){

                    }
                }else{
                    Intent intent = new Intent(HomeFragment.this.getActivity(),WebViewActivity.class);
                    intent.putExtra("title","单页详情");
                    intent.putExtra("url", adBean.getUrl());
                    startActivity(intent);
                }
            }
        });
    }

    private void initRecyclerView_qiyan() {
//        xRecyclerView = LayoutInflater.from(this).inflate(R.layout.default_xrecycle_view,null);
//        xrvCustom = (XRecyclerView)xRecyclerView.findViewById(R.id.xrv_custom);
        // 禁止下拉刷新
        xrvCustom_qiyan.setPullRefreshEnabled(false);
        xrvCustom_qiyan.setLoadingMoreEnabled(false);
        // 去掉刷新头
        xrvCustom_qiyan.clearHeader();

        xrvCustom_qiyan.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                adapter.clear();
//                mPage = 1;
//                loadCustomData();
                //adapter.clear();
                //queryId = "0";
                //loadData("1");
            }

            @Override
            public void onLoadMore() {
                //loadData("2");
            }
        });
        xrvCustom_qiyan.setLayoutManager(new GridLayoutManager(getContext(),4));
        // 需加，不然滑动不流畅
        xrvCustom_qiyan.setNestedScrollingEnabled(false);
        xrvCustom_qiyan.setHasFixedSize(false);
        xrvCustom_qiyan.setItemAnimator(new DefaultItemAnimator());

        adapter_qiyan = new CompanyListAdapter();
        xrvCustom_qiyan.setAdapter(adapter_qiyan);
    }


    private void initRecyclerView_xueyuan() {
//        xRecyclerView = LayoutInflater.from(this).inflate(R.layout.default_xrecycle_view,null);
//        xrvCustom = (XRecyclerView)xRecyclerView.findViewById(R.id.xrv_custom);
        // 禁止下拉刷新
        xrvCustom_xueyuan.setPullRefreshEnabled(false);
        xrvCustom_xueyuan.setLoadingMoreEnabled(false);
        // 去掉刷新头
        xrvCustom_xueyuan.clearHeader();

        xrvCustom_xueyuan.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                adapter.clear();
//                mPage = 1;
//                loadCustomData();
                //adapter.clear();
                //queryId = "0";
                //loadData("1");
            }

            @Override
            public void onLoadMore() {
                //loadData("2");
            }
        });
        xrvCustom_xueyuan.setLayoutManager(new GridLayoutManager(getContext(),2));
        // 需加，不然滑动不流畅
        xrvCustom_xueyuan.setNestedScrollingEnabled(false);
        xrvCustom_xueyuan.setHasFixedSize(false);
        xrvCustom_xueyuan.setItemAnimator(new DefaultItemAnimator());

        adapter_xueyuan = new StarXueyuanAdapter();
        xrvCustom_xueyuan.setAdapter(adapter_xueyuan);
    }

    private void initRecyclerView_ip() {
        // 禁止下拉刷新
        xrvCustom_ip.setPullRefreshEnabled(false);
        xrvCustom_ip.setLoadingMoreEnabled(false);
        // 去掉刷新头
        xrvCustom_ip.clearHeader();
        xrvCustom_ip.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
            }
        });
        xrvCustom_ip.setLayoutManager(new LinearLayoutManager(getContext()));
        // 需加，不然滑动不流畅
        xrvCustom_ip.setNestedScrollingEnabled(false);
        xrvCustom_ip.setHasFixedSize(false);
        xrvCustom_ip.setItemAnimator(new DefaultItemAnimator());

        adapter_ip = new ActivityDoingAdapter();
        adapter_ip.setContext(getContext());
        xrvCustom_ip.setAdapter(adapter_ip);
    }
}
