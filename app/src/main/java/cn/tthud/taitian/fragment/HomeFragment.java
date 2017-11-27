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
import android.widget.LinearLayout;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.adapter.CompanyListAdapter;
import cn.tthud.taitian.adapter.GoodIPAdapter;
import cn.tthud.taitian.adapter.StarXueyuanAdapter;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.bean.ActivityBean;
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
    private List<String> urls;

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

    private CompanyListAdapter adapter_qiyan;
    private StarXueyuanAdapter adapter_xueyuan;
    private GoodIPAdapter adapter_ip;



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



                        // 广告图
                        JSONArray markarray = jsonObject1.getJSONArray("admarket");
                        urls = new ArrayList<String>();
                        for (int i = 0;i<markarray.length();i++) {
                            JSONObject jsonObject2 = (JSONObject) markarray.get(i);
                            urls.add(jsonObject2.getString("img"));
                        }
                        setBanner();

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
                        xrvCustom_xueyuan.setVisibility(View.VISIBLE);
                        adapter_xueyuan.addAll(startList);
                        adapter_xueyuan.notifyDataSetChanged();

                        // ip
                        String activity = jsonObject1.getString("activity");
                        Type type_act =new TypeToken<List<ActivityBean>>(){}.getType();
                        List<ActivityBean> acttList = GsonUtils.jsonToList(activity,type_act);
                        xrvCustom_ip.setVisibility(View.VISIBLE);
                        adapter_ip.addAll(acttList);
                        adapter_ip.notifyDataSetChanged();
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
    private void setBanner() {
//        String url1 = "http://waxin-1251679641.file.myqcloud.com/859425427f474da3b4e77c6ce48f7447.jpg";
//        String url2 = "http://waxin-1251679641.file.myqcloud.com/13d173a342484c6cbdaca32315f13412.jpg";
//        String url3 = "http://waxin-1251679641.file.myqcloud.com/0a265766bd91453882990c8da7889d8e.jpg";
//        List<String> urls = new ArrayList<>();
//        urls.add(url1);
//        urls.add(url2);
//        urls.add(url3);
        sib_simple_usage.setSource(getBanner(urls)).startScroll();
        sib_simple_usage.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                // 进入公司详情
//                Intent intent = new Intent(HomeFragment.this.getActivity(), CompanyDetailActivity.class);
//                startActivity(intent);
                // 进入公司列表
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
        xrvCustom_xueyuan.setLayoutManager(new GridLayoutManager(getContext(),4));
        // 需加，不然滑动不流畅
        xrvCustom_xueyuan.setNestedScrollingEnabled(false);
        xrvCustom_xueyuan.setHasFixedSize(false);
        xrvCustom_xueyuan.setItemAnimator(new DefaultItemAnimator());

        adapter_xueyuan = new StarXueyuanAdapter();
        xrvCustom_xueyuan.setAdapter(adapter_xueyuan);
    }

    private void initRecyclerView_ip() {
//        xRecyclerView = LayoutInflater.from(this).inflate(R.layout.default_xrecycle_view,null);
//        xrvCustom = (XRecyclerView)xRecyclerView.findViewById(R.id.xrv_custom);
        // 禁止下拉刷新
        xrvCustom_ip.setPullRefreshEnabled(false);
        xrvCustom_ip.setLoadingMoreEnabled(false);
        // 去掉刷新头
        xrvCustom_ip.clearHeader();

        xrvCustom_ip.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        xrvCustom_ip.setLayoutManager(new LinearLayoutManager(getContext()));
        // 需加，不然滑动不流畅
        xrvCustom_ip.setNestedScrollingEnabled(false);
        xrvCustom_ip.setHasFixedSize(false);
        xrvCustom_ip.setItemAnimator(new DefaultItemAnimator());

        adapter_ip = new GoodIPAdapter();
        xrvCustom_ip.setAdapter(adapter_ip);
    }

}
