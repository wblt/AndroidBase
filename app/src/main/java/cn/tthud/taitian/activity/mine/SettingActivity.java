package cn.tthud.taitian.activity.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.Map;

import cn.tthud.taitian.ChatManager;
import cn.tthud.taitian.DemoApplication;
import cn.tthud.taitian.MainActivity;
import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.login.LoginActivity;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.DataClearManager;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.widget.CustomAlertDialog;
import cn.tthud.taitian.widget.EaseSwitchButton;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class SettingActivity extends ActivityBase {

    @ViewInject(R.id.bingding)
    private TextView bingding;

    @ViewInject(R.id.bingding_status)
    private TextView bingding_status;

    @ViewInject(R.id.switch_bingding)
    private EaseSwitchButton switch_bingding;

    @ViewInject(R.id.iv_wx_arrow)
    private ImageView iv_wx_arrow;

    @ViewInject(R.id.tv_cache)
    private TextView tv_cache;

    private CustomAlertDialog customAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_setting);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("设置");
        setTopLeftDefultListener();

        updateView();


        try{
            updateCache();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView () {
        if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
            switch_bingding.setVisibility(View.VISIBLE);
            iv_wx_arrow.setVisibility(View.GONE);
            if (SPUtils.getBoolean(SPUtils.IS_BINDWX,false)) {
                //bingding_status.setText("已绑定");
                switch_bingding.openSwitch();
            } else {
                //bingding_status.setText("未绑定");
                switch_bingding.closeSwitch();
            }
        } else {
            //bingding_status.setText("未绑定");
            //switch_bingding.closeSwitch();
            bingding.setText("绑定手机号码");
            switch_bingding.setVisibility(View.GONE);
            iv_wx_arrow.setVisibility(View.VISIBLE);
        }

    }

    private void updateCache() throws Exception {
        try{
            String formatSize = DataClearManager.getTotalCacheSize(SettingActivity.this);
            tv_cache.setText(formatSize);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.logout,R.id.edit_phone,R.id.edit_pwd,R.id.lay_bind,R.id.lay_remove,R.id.switch_bingding},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        final int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.edit_phone:
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    intent = new Intent(this,ModifyPhoneActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.edit_pwd:
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    intent = new Intent(this,ModifyPwdActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.lay_bind:
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    if(SPUtils.getString(SPUtils.SOURCE).equals("tel")) {
                        if (!SPUtils.getBoolean(SPUtils.IS_BINDWX,false)) {
                            // 绑定微信
                            UMShareAPI.get(SettingActivity.this).getPlatformInfo(SettingActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                        } else {
                            // 弹出框
                            customAlertDialog = new CustomAlertDialog(this, R.style.dialog,"你确定要解除绑定微信？", new CustomAlertDialog.ViewClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (view.getId()) {
                                        case R.id.iv_close:
                                            customAlertDialog.dismiss();
                                            break;
                                        case R.id.tv_contain:
                                            customAlertDialog.dismiss();
                                            unbingdingwx();
                                            break;
                                    }
                                }
                            });
                            customAlertDialog.show();
                        }
                    }
                } else {
                    // 绑定号码
                    startActivity(new Intent(this, BindPhoneActivity.class));
                }
                break;
            case R.id.lay_remove:
                customAlertDialog = new CustomAlertDialog(this, R.style.dialog,"你确定要清除缓存？", new CustomAlertDialog.ViewClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.iv_close:
                                customAlertDialog.dismiss();
                                break;
                            case R.id.tv_contain:
                                customAlertDialog.dismiss();
                                showProgressDialog();

                                DataClearManager.clearAllCache(SettingActivity.this);
                                showMsg("清理完毕");
                                dismissProgressDialog();
                                try{
                                    updateCache();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                });
                customAlertDialog.show();
                break;
            case R.id.logout:
                customAlertDialog = new CustomAlertDialog(this, R.style.dialog,"你确定要退出登录？", new CustomAlertDialog.ViewClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.iv_close:
                                customAlertDialog.dismiss();
                                break;
                            case R.id.tv_contain:
                                customAlertDialog.dismiss();
                                logout();
                                break;
                        }
                    }
                });
                customAlertDialog.show();
                break;
            case R.id.switch_bingding:
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    if(SPUtils.getString(SPUtils.SOURCE).equals("tel")) {
                        if (!SPUtils.getBoolean(SPUtils.IS_BINDWX,false)) {
                            // 绑定微信
                            UMShareAPI.get(SettingActivity.this).getPlatformInfo(SettingActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                        } else {
                            // 弹出框
                            customAlertDialog = new CustomAlertDialog(this, R.style.dialog,"你确定要解除绑定微信？", new CustomAlertDialog.ViewClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (view.getId()) {
                                        case R.id.iv_close:
                                            customAlertDialog.dismiss();
                                            break;
                                        case R.id.tv_contain:
                                            customAlertDialog.dismiss();
                                            unbingdingwx();
                                            break;
                                    }
                                }
                            });
                            customAlertDialog.show();
                        }
                    } else if (SPUtils.getString(SPUtils.SOURCE).equals("wx")){
                        if (!SPUtils.getBoolean(SPUtils.IS_BINDWX,false)) {
                            // 绑定微信
                            UMShareAPI.get(SettingActivity.this).getPlatformInfo(SettingActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                        } else {
                            // 弹出框
                            customAlertDialog = new CustomAlertDialog(this, R.style.dialog,"你确定要解除绑定微信？", new CustomAlertDialog.ViewClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (view.getId()) {
                                        case R.id.iv_close:
                                            customAlertDialog.dismiss();
                                            break;
                                        case R.id.tv_contain:
                                            customAlertDialog.dismiss();
                                            unbingdingwx();
                                            break;
                                    }
                                }
                            });
                            customAlertDialog.show();
                        }
                    }
                } else {
                    // 绑定号码
                    startActivity(new Intent(this, BindPhoneActivity.class));
                }
                break;
        }
    }

    //登出
    private void logout(){
        ChatManager.getInstance().exitSocket(getApplicationContext());
        SPUtils.clearUser();
        DemoApplication.getInstance().closeActivitys();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.putExtra("extra_index",0);
        startActivity(intent);
        finish();
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            // map 直接获取字符串
            Log.i("BBBBBBBBBBBBBB获取微信信息成功返回：data:",data.toString());

            String openid = data.get("openid");
            SPUtils.putString(SPUtils.WX_OPEN_ID, openid);

            String profile_image_url = data.get("profile_image_url");
            SPUtils.putString(SPUtils.HEAD_PIC, profile_image_url);

            String gender = data.get("gender");
            if (gender.equals("男")){
                SPUtils.putInt(SPUtils.SEX, 1);
            }else if(gender.equals("女")){
                SPUtils.putInt(SPUtils.SEX, 2);
            }else{
                SPUtils.putInt(SPUtils.SEX, 0);
            }
            String name = data.get("name");
            SPUtils.putString(SPUtils.NICK_NAME, name);

            // 绑定
            bingdingwx();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.i("错误" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    private void bingdingwx() {
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_BIND_WX);
        requestParams.addParameter("isbindwx",-1);
        requestParams.addParameter("ub_id",SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("ua_id",SPUtils.getString(SPUtils.UA_ID));
        requestParams.addParameter("wx_openid",SPUtils.getString(SPUtils.WX_OPEN_ID));

        MXUtils.httpPost(requestParams, new CommonCallbackImp("绑定微信",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                        SPUtils.putBoolean(SPUtils.IS_BINDWX,true);
                        showMsg("绑定成功");
                        Log.i("ddd");
                        // 更新视图
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateView();
                            }
                        }, 500);
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void unbingdingwx() {
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_BIND_WX);
        requestParams.addParameter("isbindwx",1);
        requestParams.addParameter("ub_id",SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("ua_id",SPUtils.getString(SPUtils.UA_ID));
        requestParams.addParameter("wx_openid",SPUtils.getString(SPUtils.WX_OPEN_ID));

        MXUtils.httpPost(requestParams, new CommonCallbackImp("绑定微信",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String result = jsonObject.getString("data");
                        SPUtils.putBoolean(SPUtils.IS_BINDWX,false);
                        showMsg("解绑成功");
                        Log.i("ddd");
                        // 更新视图
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateView();
                            }
                        }, 500);
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
