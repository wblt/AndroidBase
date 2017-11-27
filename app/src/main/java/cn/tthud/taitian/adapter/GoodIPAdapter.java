package cn.tthud.taitian.adapter;

import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.bean.ActivityBean;
import cn.tthud.taitian.databinding.ItemGoodIpBinding;
import cn.tthud.taitian.utils.DateUtil;
import cn.tthud.taitian.utils.ImageLoader;

/**
 * Created by wb on 2017/11/27.
 */

public class GoodIPAdapter extends BaseRecyclerViewAdapter<ActivityBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_good_ip);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<ActivityBean, ItemGoodIpBinding>{
        ViewHolder(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(final ActivityBean object, int position) {
            binding.executePendingBindings();
            if(object.getThumb() != null && object.getThumb().length() != 0){
                //ImageLoader.loadRect(object.getThumb(),binding.ivImg);
            }else {
                binding.ivImg.setImageResource(R.mipmap.icon_default);
            }
            binding.tvName.setText(object.getTitle());
            binding.tvTime.setText(DateUtil.formatUnixTime(Long.valueOf(object.getStart())));
            binding.tvLocation.setText(object.getArea_title());
        }
    }
}
