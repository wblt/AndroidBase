package cn.tthud.taitian;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;
import java.util.UUID;

import cn.tthud.taitian.activity.login.LoginActivity;
import cn.tthud.taitian.db.dbmanager.MessageDaoUtils;
import cn.tthud.taitian.db.entity.Message;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.net.rxbus.RxBus;
import cn.tthud.taitian.net.rxbus.RxBusBaseMessage;
import cn.tthud.taitian.net.rxbus.RxCodeConstants;
import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by wb on 2017/12/6.
 */

public class ChatManager {

    private NotificationManager manger;
    public static final int TYPE_Normal = 1;
    private Intent websocketServiceIntent;
    private Context context;
    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     *
     * @author xuzhaohu
     *
     */
    private static class SingletonHolder {
        private static ChatManager instance = new ChatManager();
    }

    /**
     * 私有的构造函数
     */
    private ChatManager() {

    }

    public static ChatManager getInstance() {
        return SingletonHolder.instance;
    }

    protected void method() {
        System.out.println("SingletonInner");
    }

    public void initSocket(Context context) {
        // 开启sevice
        if (!CommonUtils.checkLogin()) {
            return;
        }
        if (SPUtils.getBoolean(SPUtils.ISVST,false)) {
            // 游客返回
            return;
        }
        this.context = context;
        manger = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        websocketServiceIntent = new Intent(context, WebSocketService.class);
        context.startService(websocketServiceIntent);
    }

    public void exitSocket(Context context) {
        // 开启sevice
        if (!CommonUtils.checkLogin()) {
            return;
        }
        if (SPUtils.getBoolean(SPUtils.ISVST,false)) {
            // 游客返回
            return;
        }
        WebSocketService.closeWebsocket(true);
        context.stopService(websocketServiceIntent);
    }


    public void messgeHandle (String message) {
        Log.i("^^^^^^^^^"+message);
        try {
            JSONObject jsonObject = new JSONObject(message);
            String type = jsonObject.getString("type");
            if (type.equals("onConnect")) {
                // 连接 绑定
                String client_id = jsonObject.getString("client_id");
                if (TextUtils.isEmpty(client_id)) {
                    Log.i("client_id" + "为空");
                    return;
                }
                bingding(client_id);
            } else if (type.equals("message")){
                String list = jsonObject.getString("list");
                // 解析消息，然后保存到数据库中
                Message messagebean = GsonUtils.jsonToBean(list,Message.class);
                MessageDaoUtils messageDaoUtils = new MessageDaoUtils(context);
                List<Message> mlists = messageDaoUtils.queryMessageByQueryBuilder(messagebean.getMsg_id());
                if (mlists != null && mlists.size()>0) {

                } else {
                    messageDaoUtils.insertMessage(messagebean);
                }
                messageDaoUtils.insertMessage(messagebean);
                // 发送消息去主页
                RxBus.getDefault().post(RxCodeConstants.MainActivity_MSG, new RxBusBaseMessage(1,"socket"));
                // 弹出通知栏
                simpleNotify(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void bingding(String client_id) {
        String uuid = UUID.randomUUID().toString();
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.APP_BIND_UID);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("client_id", client_id);
        requestParams.addParameter("device_id", uuid);
        MXUtils.httpPost(requestParams, new CommonCallbackImp("绑定设备",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                        Log.i("绑定设备成功");
                    }else {
                        Log.i(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private  void simpleNotify(String messsage){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Ticker是状态栏显示的提示
        builder.setTicker("上位互动");
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle("你收到一条新的消息");
        //第二行内容 通常是通知正文
        builder.setContentText(messsage);
        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
        //builder.setSubText("这里显示的是通知第三行内容！");
        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
        //builder.setContentInfo("2");
        builder.setAutoCancel(true);
        //builder.setNumber(2);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.logo));
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context,1,intent,0);
        builder.setContentIntent(pIntent);
        //设置震动
        //long[] vibrate = {100,200,100,200};
        //builder.setVibrate(vibrate);

        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manger.notify(TYPE_Normal,notification);
    }

}
