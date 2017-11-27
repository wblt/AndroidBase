package cn.tthud.taitian.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.databinding.ItemCompanyActivityBinding;
import cn.tthud.taitian.utils.ImageLoader;

/**
 * Created by bopeng on 2017/11/20.
 */

public class CompanyActivityAdapter extends BaseRecyclerViewAdapter {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_company_activity);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<ActivityBean, ItemCompanyActivityBinding>{

        ViewHolder(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(final ActivityBean object, int position) {
            binding.executePendingBindings();

            if(object.getThumb() != null && object.getThumb().length() != 0){
                ImageLoader.load(object.getThumb(), binding.ivImage);
            }else {
                binding.ivImage.setImageResource(R.mipmap.icon_default);
            }

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
        }
    }
}