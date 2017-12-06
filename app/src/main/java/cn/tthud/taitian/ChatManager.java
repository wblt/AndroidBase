package cn.tthud.taitian;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.UUID;

import cn.tthud.taitian.activity.login.LoginActivity;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by wb on 2017/12/6.
 */

public class ChatManager {

    private Intent websocketServiceIntent;
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
        websocketServiceIntent = new Intent(context, WebSocketService.class);
        context.startService(websocketServiceIntent);
    }

    public void exitSocket(Context context) {
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
                bingding(client_id);
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

}
