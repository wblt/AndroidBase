package cn.tthud.taitian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.MessageBean;
import cn.tthud.taitian.bean.WebViewBean;
import cn.tthud.taitian.databinding.ItemMessageNormalBinding;
import cn.tthud.taitian.utils.ImageLoader;

/**
 * Created by bopeng on 2017/11/3.
 */

public class MessageAdapter extends BaseRecyclerViewAdapter<MessageBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_message_normal);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<MessageBean, ItemMessageNormalBinding> {
        ViewHolder(ViewGroup parent, int item_android){
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(final MessageBean object, final int position) {
            binding.executePendingBindings();
            String msgType = object.getMc_id();
            if (msgType.equals("系统消息")){
                binding.llSystemMsgTitle.setVisibility(View.VISIBLE);
                binding.tvTitle.setText("系统消息");
            }else if(msgType.equals("广告通知")){
                binding.llSystemMsgTitle.setVisibility(View.VISIBLE);
                binding.tvTitle.setText("广告通知");
            }
            if(object.getIcon() != null && object.getIcon().length() != 0){
                ImageLoader.loadCircle(object.getIcon(),binding.headIcon);
            }else {
                binding.headIcon.setImageResource(R.mipmap.logowenzi);
            }
            binding.msgTime.setText(object.getSuetime());
            binding.tvMsgContent.setText(object.getTitle());
            binding.llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(object,position);
                    }
                }
            });
            binding.llAll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onLongClick(object,position);
                    }
                    return true;
                }
            });
        }
    }


}

