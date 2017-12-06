package cn.tthud.taitian.activity.mine;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import cn.tthud.taitian.DemoApplication;
import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.login.LoginActivity;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.fragment.MineFragment;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.widget.ActionSheet;
import cn.tthud.taitian.widget.CustomAlertDialog;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class SettingActivity extends ActivityBase {

    @ViewInject(R.id.bingding)
    private TextView bingding;

    @ViewInject(R.id.bingding_status)
    private TextView bingding_status;

    private CustomAlertDialog customAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this, R.layout.activity_setting);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("设置");
        setTopLeftDefultListener();

        updateView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView () {
        if (SPUtils.getString(SPUtils.SOURCE).equals("tel")) {
            bingding.setText("绑定微信");
        } else if (SPUtils.getString(SPUtils.SOURCE).equals("wx")){
            bingding.setText("绑定手机号");
        }
        if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
            if (SPUtils.getBoolean(SPUtils.IS_BINDWX,false)) {
                bingding_status.setText("已绑定");
            } else {
                bingding_status.setText("未绑定");
            }
        } else {
            bingding_status.setText("未绑定");
        }
    }

    @Event(value = {R.id.logout,R.id.edit_phone,R.id.edit_pwd,R.id.lay_bind,R.id.lay_remove},type = View.OnClickListener.class)
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
                                File directory = new File(FlowAPI.YYW_FILE_PATH);
                                if (directory != null && directory.exists() && directory.isDirectory()) {
                                    for (File item : directory.listFiles()) {
                                        item.delete();
                                    }
                                }
                                showMsg("清理完毕");
                                dismissProgressDialog();
                                break;
                        }
                    }
                });
                customAlertDialog.show();
                break;
            case R.id.logout:
                customAlertDialog = new CustomAlertDialog(this, R.style.dialog,"你确定要退出账号？", new CustomAlertDialog.ViewClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.iv_close:
                                customAlertDialog.dismiss();
                                break;
                            case R.id.tv_contain:
                                customAlertDialog.dismiss();
                                logout();
                                DemoApplication.getInstance().closeActivitys();
                                finish();
                                break;
                        }
                    }
                });
                customAlertDialog.show();
                break;
        }
    }

    //登出
    private void logout(){
        SPUtils.clearUser();
        LoginActivity.navToLogin(SettingActivity.this);
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
