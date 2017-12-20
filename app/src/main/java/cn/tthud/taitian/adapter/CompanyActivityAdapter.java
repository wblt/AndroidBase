package cn.tthud.taitian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.bean.WebViewBean;
import cn.tthud.taitian.databinding.ItemActivityBinding;
import cn.tthud.taitian.databinding.ItemLoopActivityBinding;
import cn.tthud.taitian.utils.DateUtil;
import cn.tthud.taitian.widget.banner.BannerItem;
import cn.tthud.taitian.widget.banner.SimpleImageBanner;

/**
 * Created by bopeng on 2017/11/20.
 */

public class CompanyActivityAdapter extends BaseRecyclerViewAdapter<ActivityBean> {
    private Context mContext;
    public void setContext(Context context){
        this.mContext = context;
    }
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolder1(parent, R.layout.item_loop_activity);
        } else if (viewType == 2){
            return new ViewHolder2(parent, R.layout.item_activity);
        } else {
            return new ViewHolder2(parent, R.layout.item_activity);
        }
    }
    @Override
    public int getItemViewType(int position) {
        ActivityBean bean = getData().get(position);
        if (bean.getImg() !=null && bean.getImg().size() > 1) {
            return 1;
        } else {
            return 2;
        }
    }

    private class ViewHolder1 extends BaseRecyclerViewHolder<ActivityBean, ItemLoopActivityBinding>{
        ViewHolder1(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }
        @Override
        public void onBindViewHolder(final ActivityBean object, int position) {
            binding.executePendingBindings();

            binding.tvTitle.setText(object.getTitle());
            binding.tvTime.setText(DateUtil.formatUnixTime(Long.valueOf(object.getStart())));
            binding.tvAddress.setText(object.getArea_title());
            binding.tvNumber.setText(object.getTotal());

            binding.llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //jumpToWebViewActivity(object, view.getContext());
                }
            });

            if (object.getImg() != null && object.getImg().size() != 0){
                binding.sibSimpleUsage.setAutoScrollEnable(true);
                binding.sibSimpleUsage.setIndicatorShow(true);
                binding.sibSimpleUsage.setSource(getBanner(object.getImg())).startScroll();
//                binding.sibSimpleUsage.setOnItemClickL(new SimpleImageBanner.OnItemClickL(){
//                    @Override
//                    public void onItemClick(int position) {
//                        jumpToWebViewActivity(object, mContext);
//                    }
//                });

            }
        }
    }

    private class ViewHolder2 extends BaseRecyclerViewHolder<ActivityBean, ItemActivityBinding>{
        ViewHolder2(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }
        @Override
        public void onBindViewHolder(final ActivityBean object, int position) {
            binding.executePendingBindings();

            binding.tvTitle.setText(object.getTitle());
            binding.tvTime.setText(DateUtil.formatUnixTime(Long.valueOf(object.getStart())));
            binding.tvAddress.setText(object.getArea_title());
            binding.tvNumber.setText(object.getTotal());

            binding.llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //jumpToWebViewActivity(object, view.getContext());
                }
            });

            if (object.getImg() != null && object.getImg().size() != 0){
                binding.sibSimpleUsage.setAutoScrollEnable(false);
                binding.sibSimpleUsage.setIndicatorShow(false);
                binding.sibSimpleUsage.setSource(getBanner(object.getImg())).startScroll();
//                binding.sibSimpleUsage.setOnItemClickL(new SimpleImageBanner.OnItemClickL(){
//                    @Override
//                    public void onItemClick(int position) {
//                        jumpToWebViewActivity(object, mContext);
//                    }
//                });

            }
        }
    }

//    private void jumpToWebViewActivity(ActivityBean object, Context localContext){
//        WebViewBean bean = new WebViewBean();
//        bean.setUrl(object.getUrl());
//        bean.setTitle(object.getTitle());
//        WebViewActivity.navToWebView(localContext, bean);
//    }

    public static List<BannerItem> getBanner(List<String> alist) {
        ArrayList<BannerItem> list = new ArrayList<BannerItem>();
        for (int i = 0; i < alist.size(); i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = alist.get(i);
            list.add(item);
        }
        return list;
    }
}
