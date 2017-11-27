package cn.tthud.taitian.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.home.CompanyDetailActivity;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.bean.CompanyBean;
import cn.tthud.taitian.databinding.ItemCompanyListBinding;
import cn.tthud.taitian.utils.ImageLoader;

/**
 * Created by bopeng on 2017/11/20.
 */

public class CompanyListAdapter extends BaseRecyclerViewAdapter {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_company_list);
    }
    private class ViewHolder extends BaseRecyclerViewHolder<CompanyBean, ItemCompanyListBinding>{
        ViewHolder(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(final CompanyBean object, int position) {
            binding.executePendingBindings();
            if(object.getThumb() != null && object.getThumb().length() != 0){
                //ImageLoader.load(object.getThumb(), binding.ivCompanyImg);
                ImageLoader.loadRect(object.getThumb(),binding.ivCompanyImg);
            }else {
                binding.ivCompanyImg.setImageResource(R.mipmap.icon_default);
            }
            binding.tvCompanyName.setText(object.getAbbtion());
        }
    }
}