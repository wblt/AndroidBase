package cn.tthud.taitian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.home.CompanyDetailActivity;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.widget.banner.BannerItem;
import cn.tthud.taitian.widget.banner.SimpleImageBanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

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
            loadData();
            setBanner();
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
                        String admarket = jsonObject1.getString("admarket");
                        String company = jsonObject1.getString("company");
                        String activity = jsonObject1.getString("activity");
                        Log.i("admarket:" + admarket);
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
        String url1 = "http://waxin-1251679641.file.myqcloud.com/859425427f474da3b4e77c6ce48f7447.jpg";
        String url2 = "http://waxin-1251679641.file.myqcloud.com/13d173a342484c6cbdaca32315f13412.jpg";
        String url3 = "http://waxin-1251679641.file.myqcloud.com/0a265766bd91453882990c8da7889d8e.jpg";
        List<String> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);
        urls.add(url3);
        sib_simple_usage.setSource(getBanner(urls)).startScroll();
        sib_simple_usage.setOnItemClickL(new SimpleImageBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), CompanyDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
