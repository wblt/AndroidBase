package cn.tthud.taitian.adapter;

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
        public void onBindViewHolder(final TaskBean object, int position) {
            binding.executePendingBindings();



        }
    }
}
