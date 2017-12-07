package cn.tthud.taitian.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.databinding.ItemActivityBinding;
import cn.tthud.taitian.databinding.ItemCompanyActivityBinding;
import cn.tthud.taitian.utils.ImageLoader;
import cn.tthud.taitian.widget.banner.BannerItem;
import cn.tthud.taitian.widget.banner.SimpleImageBanner;

/**
 * Created by bopeng on 2017/11/20.
 */

public class CompanyActivityAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    public void setContext(Context context){
        this.mContext = context;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_activity);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<ActivityBean, ItemActivityBinding>{

        ViewHolder(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(final ActivityBean object, int position) {
            binding.executePendingBindings();

            binding.tvTitle.setText(object.getTitle());
            binding.tvAddress.setText(object.getArea_title());
            binding.tvNumber.setText(object.getTotal());

            binding.llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = object.getUrl();
                    if (TextUtils.isEmpty(url)){
                        return;
                    }
                    Intent intent = new Intent(view.getContext(),WebViewActivity.class);
                    intent.putExtra("title",object.getTitle());
                    intent.putExtra("url", url);
                    view.getContext().startActivity(intent);
                }
            });

            if (object.getImg() != null && object.getImg().size() != 0){
                binding.sibSimpleUsage.setSource(getBanner(object.getImg())).startScroll();
                binding.sibSimpleUsage.setOnItemClickL(new SimpleImageBanner.OnItemClickL(){
                    @Override
                    public void onItemClick(int position) {
                        String url = object.getUrl();
                        if (TextUtils.isEmpty(url)) {
                            return;
                        }
                        Intent intent = new Intent(mContext,WebViewActivity.class);
                        intent.putExtra("title",object.getTitle());
                        intent.putExtra("url", url);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
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
}
