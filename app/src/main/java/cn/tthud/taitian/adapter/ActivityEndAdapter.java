package cn.tthud.taitian.adapter;

import android.view.View;
import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.databinding.ItemActivityBinding;
import cn.tthud.taitian.utils.ImageLoader;
import cn.tthud.taitian.utils.Log;

/**
 * Created by bopeng on 2017/11/3.
 */

public class ActivityEndAdapter extends BaseRecyclerViewAdapter {
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

            if(object.getImg() != null && object.getImg().length() != 0){
                ImageLoader.load(object.getImg(), binding.ivBannerPic);
            }else {
                binding.ivBannerPic.setImageResource(R.mipmap.icon_default);
            }

            binding.tvAddress.setText(object.getArea_title());
            binding.tvZanNum.setText(object.getPrise());
            binding.tvHongqiNum.setText(object.getArea());
            binding.tvPrice.setText(object.getCost());
            binding.tvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("点击了喜欢");
                }
            });

            binding.tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("点击了详情");
                }
            });
        }
    }
}
