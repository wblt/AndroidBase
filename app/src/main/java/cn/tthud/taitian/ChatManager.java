package cn.tthud.taitian;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;

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
                // 连接
                
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}
