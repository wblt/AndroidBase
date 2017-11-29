package cn.tthud.taitian.adapter;

import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.bean.WalletRecordBean;
import cn.tthud.taitian.databinding.ItemRecordRechargeBinding;

/**
 * Created by bopeng on 2017/11/28.
 */

public class ActivityMyWalletAdapter extends BaseRecyclerViewAdapter {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_record_recharge);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<WalletRecordBean, ItemRecordRechargeBinding>{

        ViewHolder(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(final WalletRecordBean object, int position) {
            binding.executePendingBindings();

            binding.tvTitle.setText(object.getTitle());
            if (object.getType().equals("充值")){
                binding.tvSubTitle.setBackgroundResource(R.drawable.radius_layout_blue_btn);
            }else if(object.getType().equals("抽奖")){
                binding.tvSubTitle.setBackgroundResource(R.drawable.radius_layout_orange_btn);
            }else{
                binding.tvSubTitle.setBackgroundResource(R.drawable.radius_layout_blue_btn);
            }
            binding.tvSubTitle.setText(object.getType());
            binding.tvScore.setText("+" + object.getUsefee());
            binding.tvTime.setText(object.getAdddate());
        }
    }
}
