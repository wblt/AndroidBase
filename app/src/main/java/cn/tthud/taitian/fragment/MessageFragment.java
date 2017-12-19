package cn.tthud.taitian.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.home.ArtonceActivity;
import cn.tthud.taitian.adapter.MessageAdapter;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.base.OnItemClickListener;
import cn.tthud.taitian.base.OnItemLongClickListener;
import cn.tthud.taitian.base.WebViewActivity;
import cn.tthud.taitian.bean.MessageBean;
import cn.tthud.taitian.bean.WebViewBean;
import cn.tthud.taitian.db.dbmanager.MessageDaoUtils;
import cn.tthud.taitian.db.entity.Message;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.net.rxbus.RxBus;
import cn.tthud.taitian.net.rxbus.RxBusBaseMessage;
import cn.tthud.taitian.net.rxbus.RxCodeConstants;
import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.widget.ActionSheet;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by wb on 2017/10/8.
 */

public class MessageFragment extends FragmentBase implements View.OnClickListener,ActionSheet.OnActionSheetSelected, DialogInterface.OnCancelListener {

    private View view;

    @ViewInject(R.id.xrv_custom)
    private XRecyclerView xrvCustom;

    @ViewInject(R.id.page_refresh)
    private LinearLayout page_refresh;
    //private int mPage = 1;
    //private int mMaxPage = -1;
    private MessageAdapter mAdapter;
    private Message clickBean;
    private int readnum = 0;
    private MessageDaoUtils messageDaoUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this, R.layout.message_fragment);
            appendTopBody(R.layout.activity_top_icon);
            ((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            setTopBarTitle("消息");
            initRecyclerView();
            setListener();
            messageDaoUtils = new MessageDaoUtils(getContext());
            loadNewData();
        }
        return view;
    }

    private void initRecyclerView(){
        // 禁止下拉刷新
        xrvCustom.setPullRefreshEnabled(true);
        xrvCustom.setLoadingMoreEnabled(false);
        //xrvCustom.clearHeader();
        xrvCustom.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                //mPage = 1;
                loadNewData();
            }
            @Override
            public void onLoadMore() {
//                mPage += 1;
//                loadNewData();
            }
        });
        xrvCustom.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        // 需加，不然滑动不流畅
        xrvCustom.setNestedScrollingEnabled(false);
        xrvCustom.setHasFixedSize(false);
        xrvCustom.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MessageAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<Message>() {
            @Override
            public void onClick(Message messageBean, int position) {
                clickBean = messageBean;
                if (messageBean.getIsread() == 2) {
                    // 操作消息
                    operationMsg("isread");
                }
                if (messageBean.getIshref() == 2) {
                    return;
                }
                if (TextUtils.isEmpty(messageBean.getUrl())){
                    if (TextUtils.isEmpty(messageBean.getModule())) {
                        return;
                    }
                    if (messageBean.getModule().equals("artonce")){
                        Intent intent = new Intent(getActivity(), ArtonceActivity.class);
                        intent.putExtra("cid", messageBean.getModule_id());
                        intent.putExtra("title", "广告详情");
                        startActivity(intent);
                    }else if (messageBean.getModule().equals("admarket")){

                    }else if (messageBean.getModule().equals("article")){

                    }
                }else{
                    Intent intent = new Intent(getActivity(),WebViewActivity.class);
                    intent.putExtra("title","广告详情");
                    intent.putExtra("url", messageBean.getUrl());
                    startActivity(intent);
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener<Message>() {
            @Override
            public void onLongClick(Message messageBean, int position) {
                clickBean = messageBean;
                // 长按操作
                ActionSheet.showSheet(getActivity(),
                        MessageFragment.this, MessageFragment.this, "5");
            }
        });
        xrvCustom.setAdapter(mAdapter);
    }

    private void setListener(){
        page_refresh.setOnClickListener(this);
    }

    private void loadNewData(){
        if (!CommonUtils.checkLogin()) {
            return;
        }
        if (TextUtils.isEmpty(SPUtils.getString(SPUtils.UB_ID))) {
            return;
        }
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_MESSAGE_LIST);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        //requestParams.addParameter("p", mPage);
        MXUtils.httpGet(requestParams, new CommonCallbackImp("消息列表",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        xrvCustom.refreshComplete();
                        String result = jsonObject.getString("data");
                        //JSONObject jsonObject1 = new JSONObject(result);
                        //String maxPage = jsonObject1.getString("maxPage");
                        //mMaxPage = Integer.parseInt(maxPage);
                        // 未读消息总数
                        //readnum = jsonObject1.getInt("readnum");
                        //SPUtils.putInt(SPUtils.BADGER_NUM,readnum);
                        //RxBus.getDefault().post(RxCodeConstants.MainActivity_MSG, new RxBusBaseMessage(1,"http"));
                        //String list = jsonObject1.getString("list");
                        Type type = new TypeToken<List<Message>>(){}.getType();
                        List<Message> beanList = GsonUtils.jsonToList(result,type);
                        // 插入到数据库中
                        if (beanList != null && beanList.size() > 0) {
                            // 先判断是否有这个数据
                            for (Message msg : beanList) {
                                List<Message> mlists = messageDaoUtils.queryMessageByQueryBuilder(msg.getMsg_id());
                                if (mlists != null && mlists.size()>0) {

                                } else {
                                    messageDaoUtils.insertMessage(msg);
                                }
                            }

                        }
                        // 延迟显示加载
                        showMsgUI();
//                        mAdapter.addAll(beanList);
//                        mAdapter.notifyDataSetChanged();
//                        if(mAdapter.getData().size() == 0){
//                            page_refresh.setVisibility(View.VISIBLE);
//                            xrvCustom.setVisibility(View.GONE);
//                        }else {
//                            page_refresh.setVisibility(View.GONE);
//                            xrvCustom.setVisibility(View.VISIBLE);
//                        }
//                        if(mPage >= mMaxPage){
//                            xrvCustom.noMoreLoading();
//                        }
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.page_refresh:
                //loadNewData();
                break;
        }
    }

    private void operationMsg(final String type) {
        showProgressDialog();
        if (clickBean == null) {
            return;
        }
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.APP_OPERATIONMSG);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("msg_id", clickBean.getMsg_id());
        requestParams.addParameter("type", type);
        MXUtils.httpPost(requestParams, new CommonCallbackImp("消息操作",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                        // 操作成功
                        //showMsg("操作成功");
                        if (type.equals("isread")) {
                            // 修改为已读
                            List<Message> lists = messageDaoUtils.queryMessageByQueryBuilder(clickBean.getMsg_id());
                            if (lists != null && lists.size()>0) {
                                for (Message msg:lists) {
                                    msg.setIsread(1);
                                    messageDaoUtils.updateMeizi(msg);
                                }
                            }
                        } else if (type.equals("isdel")){
                            List<Message> lists = messageDaoUtils.queryMessageByQueryBuilder(clickBean.getMsg_id());
                            if (lists != null && lists.size()>0) {
                                for (Message msg:lists) {
                                    messageDaoUtils.deleteMeizi(msg);
                                }
                            }
                        }
                        // 发送消息给主页更细消息数目
                        RxBus.getDefault().post(RxCodeConstants.MainActivity_MSG, new RxBusBaseMessage(1,"updateMsg"));

                        showMsgUI();
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onClick(int whichButton) {
        if (whichButton == 10) {
            // 标记已读
            if (clickBean.getIsread() == 2) {
                operationMsg("isread");
            }
        } else if (whichButton == 11) {
            // 删除
            operationMsg("isdel");
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }


    private void showMsgUI() {
        new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               List<Message> list = messageDaoUtils.queryAllMessage();
               // 获取排序后的数组，然后拿这个展示即可。
               List<Message> lists = sort(list);
               mAdapter.clear();
               mAdapter.addAll(lists);
               mAdapter.notifyDataSetChanged();
               if(mAdapter.getData().size() == 0){
                   page_refresh.setVisibility(View.VISIBLE);
                   xrvCustom.setVisibility(View.GONE);
               }else {
                   page_refresh.setVisibility(View.GONE);
                   xrvCustom.setVisibility(View.VISIBLE);
               }
           }
        }, 500);
    }

    private List<Message> sort(List<Message> list) {
        List<Message> unReadMsgList = new ArrayList<>();
        List<Message> readMsgList = new ArrayList<>();
        List<Message> tempList = list;
        Collections.sort(tempList, new Comparator<Message>() {
            @Override
            public int compare(Message message, Message t1) {
                long deal = t1.getSuetime() - message.getSuetime();
                return (int)deal;
            }
        });
        for (Message msg: tempList){
            if (msg.getIsread() == 2){
                unReadMsgList.add(msg);
            }else if (msg.getIsread() == 1){
                readMsgList.add(msg);
            }
        }
        unReadMsgList.addAll(readMsgList);
        return unReadMsgList;
    }
}
