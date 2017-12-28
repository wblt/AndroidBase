package cn.tthud.taitian.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.bean.TaskBean;
import cn.tthud.taitian.databinding.ItemTaskBinding;

/**
 * Created by wb on 2017/12/2.
 */

public class TaskAdapter extends BaseRecyclerViewAdapter<TaskBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_task);
    }
    private class ViewHolder extends BaseRecyclerViewHolder<TaskBean, ItemTaskBinding>{
        ViewHolder(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }
        @Override
        public void onBindViewHolder(final TaskBean object, final int position) {
            binding.executePendingBindings();
            binding.title.setText(object.getTk_title());
            String tl_plan = object.getTl_plan();
            if (TextUtils.isEmpty(tl_plan)) {
                tl_plan = "0";
            }
            binding.tvJingdu.setText(tl_plan+"/"+object.getPlan());
            //   [1进行中|2任务完成|3已领取]
            if (object.getStatus().equals("1")) {
                binding.lingqu.setBackgroundResource(R.drawable.radius_layout_gray_btn);
                binding.lingqu.setText("进行中");
            } else if (object.getStatus().equals("2")) {
                binding.lingqu.setBackgroundResource(R.drawable.radius_layout_orange_btn);
                binding.lingqu.setText("领取");
            } else if (object.getStatus().equals("3")) {
                binding.lingqu.setBackgroundResource(R.drawable.radius_layout_gray_btn);
                binding.lingqu.setText("已领取");
            } else if (object.getStatus().equals("4")) {
                binding.lingqu.setBackgroundResource(R.drawable.radius_layout_gray_btn);
                binding.lingqu.setText("已完成");
            } else if (TextUtils.isEmpty(object.getStatus())) {
                binding.lingqu.setBackgroundResource(R.drawable.radius_layout_dark_green_btn);
                binding.lingqu.setText("去完成");
            }
            binding.lingqu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        // 领取
                        if (object.getStatus().equals("2")) {
                            listener.onClick(object,position);
                        }

                    }
                }
            });

        }
    }
}
