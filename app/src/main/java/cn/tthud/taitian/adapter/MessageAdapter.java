package cn.tthud.taitian.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.BaseRecyclerViewAdapter;
import cn.tthud.taitian.base.BaseRecyclerViewHolder;

/**
 * Created by bopeng on 2017/11/3.
 */

public class MessageAdapter extends BaseRecyclerViewAdapter {
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new ViewHolderNormalMsg(parent, R.layout.item_message_normal);
        }else {
            return new ViewHolderSystemMsg(parent, R.layout.item_message_system);
        }
    }

    private class ViewHolderNormalMsg extends BaseRecyclerViewHolder {

        ViewHolderNormalMsg(ViewGroup parent, int item_android){
            super(parent, item_android);
            this.setIsRecyclable(true);
        }

        @Override
        public void onBindViewHolder(Object object, int position) {

        }
    }

    private class ViewHolderSystemMsg extends BaseRecyclerViewHolder {

        ViewHolderSystemMsg(ViewGroup parent, int item_android){
            super(parent, item_android);
            this.setIsRecyclable(true);
        }

        @Override
        public void onBindViewHolder(Object object, int position) {

        }
    }
}

