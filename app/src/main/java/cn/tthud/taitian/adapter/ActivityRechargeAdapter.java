package cn.tthud.taitian.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.bean.RechargeBean;
import cn.tthud.taitian.databinding.ItemRechargeBinding;

/**
 * Created by bopeng on 2017/11/29.
 */

public class ActivityRechargeAdapter extends BaseRecyclerViewAdapter {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_recharge);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<RechargeBean, ItemRechargeBinding>{

        ViewHolder(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(final RechargeBean object, int position) {
            binding.executePendingBindings();

            binding.tvScore.setText(object.getJifen() + " 积分");
            binding.tvMoney.setText("¥ " + object.getMoney_real());

            if (object.isSelect()){
                binding.llAll.setBackgroundResource(R.drawable.radius_layout_orange_btn);
                binding.tvScore.setTextColor(Color.rgb(255, 255,255));
                binding.tvMoney.setTextColor(Color.rgb(255, 255,255));
            }else{
                binding.llAll.setBackgroundResource(R.drawable.shape_rectangle_white);
                binding.tvScore.setTextColor(Color.rgb(102, 102,102));
                binding.tvMoney.setTextColor(Color.rgb(102, 102,102));
            }


            binding.llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<RechargeBean> models = getData();
                    for (RechargeBean model: models) {
                        model.setSelect((model == object));
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
