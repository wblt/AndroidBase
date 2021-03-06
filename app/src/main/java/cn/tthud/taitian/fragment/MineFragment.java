package cn.tthud.taitian.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.tthud.taitian.DemoApplication;
import cn.tthud.taitian.MainActivity;
import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.login.LoginActivity;
import cn.tthud.taitian.activity.mine.AboutActivity;
import cn.tthud.taitian.activity.mine.BindPhoneActivity;
import cn.tthud.taitian.activity.mine.ModifyInfoActivity;
import cn.tthud.taitian.activity.mine.MyWalletActivity;
import cn.tthud.taitian.activity.mine.SettingActivity;
import cn.tthud.taitian.activity.mine.TaskActivity;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.bean.UserBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.Base64Util;
import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.ImageLoader;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.widget.ActionSheet;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

/**
 * Created by wb on 2017/10/8.
 */

public class MineFragment extends FragmentBase{

    private View view;
    @ViewInject(R.id.username)
    private TextView username;

    @ViewInject(R.id.headpic)
    private ImageView headpic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this,R.layout.mine_fragment);
            updateView();
            personCenter();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    public void updateView(){
        ImageLoader.loadCircle(SPUtils.getString(SPUtils.HEAD_PIC),headpic);
        String nickname = SPUtils.getString(SPUtils.NICK_NAME);;
        if (SPUtils.getInt(SPUtils.SEX,0) == 1) {
            // 男
            username.setText(nickname+" · "+"男");
        } else if (SPUtils.getInt(SPUtils.SEX,0) == 2){
            // 女
            username.setText(nickname+" · "+"女");

        } else {
            // 未知
            username.setText(nickname+" . "+"");
        }
    }

    @Event(value = {R.id.lay_renwu,R.id.lay_qianbao,R.id.lay_advatar_upload,
            R.id.lay_person_info,
            R.id.setting_lay,R.id.about_lay},type = View.OnClickListener.class)
    private void onEvenOnclick(View view){
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.lay_qianbao:          // 我的钱包
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    startActivity(new Intent(this.getContext(), MyWalletActivity.class));
                } else {
                    startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.lay_renwu:  // 我的任务
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    intent = new Intent(this.getContext(), TaskActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.lay_person_info: // 完善个人信息
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    startActivity(new Intent(this.getContext(), ModifyInfoActivity.class));
                } else {
                    startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.setting_lay: // 设置
                intent = new Intent(this.getContext(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.about_lay:  // 关于
                intent = new Intent(this.getContext(), AboutActivity.class);
                startActivity(intent);
                break;
        }
    }




    // 个人中心
    private void personCenter() {
        if (!CommonUtils.checkLogin()) {
            return;
        }
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.PERSONAL_CENTER);
        if (TextUtils.isEmpty(SPUtils.getString(SPUtils.UB_ID))){
            requestParams.addParameter("islogin", "2");
        }else{
            requestParams.addParameter("islogin", "1"); // 已登录
        }
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("act_url","");
        requestParams.addParameter("openid", SPUtils.getString(SPUtils.WX_OPEN_ID));
        requestParams.addParameter("headimgurl", SPUtils.getString(SPUtils.HEAD_PIC));
        requestParams.addParameter("sex", String.valueOf(SPUtils.getInt(SPUtils.SEX, 0)));
        requestParams.addParameter("nickname", SPUtils.getString(SPUtils.NICK_NAME));
        MXUtils.httpPost(requestParams, new CommonCallbackImp("个人中心",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String userData = jsonObject.getString("data");
                        Log.i("AAAAAAAAAAAAAAAAAAAAAAAAAAa"+userData);
                        JSONObject jsonObject1 = new JSONObject(userData);
                        String ub_id = jsonObject1.getString("ub_id");
                        String nickname = jsonObject1.getString("nickname");
                        String headpic = jsonObject1.getString("headpic");
                        int sex = jsonObject1.getInt("sex");
                        boolean isvst = jsonObject1.getBoolean("isvst");
                        boolean isbindwx = jsonObject1.getBoolean("isbindwx");
                        String h5_url = jsonObject1.getString("h5_url");

                        if (!isvst) {
                            // 用户
                            String ua_id = jsonObject1.getString("ua_id");
                            String realname = jsonObject1.getString("realname");
                            String idcard = jsonObject1.getString("idcard");
                            String email = jsonObject1.getString("email");
                            String stylesig = jsonObject1.getString("stylesig");
                            String address = jsonObject1.getString("address");
                            int totaljifen = jsonObject1.getInt("totaljifen");

                            SPUtils.putString(SPUtils.UA_ID,ua_id);
                            SPUtils.putString(SPUtils.REAL_NAME,realname);
                            SPUtils.putString(SPUtils.ID_CARD,idcard);
                            SPUtils.putString(SPUtils.STYLESIG,stylesig);
                            SPUtils.putString(SPUtils.EMAIL,email);
                            SPUtils.putString(SPUtils.ADDRESS,address);
                            SPUtils.putInt(SPUtils.TOTALJIFEN,totaljifen);
                        } else {
                            String wx_openid = jsonObject1.getString("wx_openid");
                            SPUtils.putString(SPUtils.WX_OPEN_ID,wx_openid);
                        }
                        // 缓存本地信息
                        SPUtils.putString(SPUtils.UB_ID,ub_id);
                        SPUtils.putString(SPUtils.NICK_NAME,nickname);
                        SPUtils.putString(SPUtils.HEAD_PIC,headpic);
                        SPUtils.putInt(SPUtils.SEX,sex);
                        SPUtils.putBoolean(SPUtils.ISVST,isvst);
                        SPUtils.putBoolean(SPUtils.IS_BINDWX,isbindwx);
                        SPUtils.putString(SPUtils.H5_URL,h5_url);

                        // 更新视图
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("BBBBBBBBBBBBBB"+SPUtils.getString(SPUtils.WX_OPEN_ID));
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
