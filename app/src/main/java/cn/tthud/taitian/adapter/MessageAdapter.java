package cn.tthud.taitian.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.bean.MessageBean;
import cn.tthud.taitian.databinding.ItemMessageNormalBinding;
import cn.tthud.taitian.utils.ImageLoader;

/**
 * Created by bopeng on 2017/11/3.
 */

public class MessageAdapter extends BaseRecyclerViewAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_message_normal);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<MessageBean, ItemMessageNormalBinding> {

        ViewHolder(ViewGroup parent, int item_android){
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(MessageBean object, int position) {
            binding.executePendingBindings();

            String msgType = object.getMc_id();
            if (msgType.equals("系统消息")){  // 如果是系统消息
                binding.llSystemMsgTitle.setVisibility(View.VISIBLE);
            }else if(msgType.equals("广告通知")){
                binding.llSystemMsgTitle.setVisibility(View.GONE);
            }

            if(object.getIcon() != null && object.getIcon().length() != 0){
                binding.ivMsgImv.setVisibility(View.VISIBLE);
                ImageLoader.load(object.getIcon(), binding.ivMsgImv);
            }else {
                binding.ivMsgImv.setVisibility(View.GONE);
                binding.ivMsgImv.setImageResource(R.mipmap.ccccc);
            }

            binding.tvMsgContent.setText(object.getTitle());
        }
    }
}

