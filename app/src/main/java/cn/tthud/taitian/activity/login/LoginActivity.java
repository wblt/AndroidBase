package cn.tthud.taitian.activity.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.tthud.taitian.DemoApplication;
import cn.tthud.taitian.MainActivity;
import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.bean.UserBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.RegExpValidator;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.widget.ActionSheet;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class LoginActivity extends ActivityBase {


    public static final int REGISTER_VIEW_CODE = 1002;

    @ViewInject(R.id.login_phone)
    private EditText login_phone;

    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;

    @ViewInject(R.id.register_btn)
    private TextView register_btn;

    @ViewInject(R.id.forget_pwd)
    private TextView forget_pwd;

    @ViewInject(R.id.username_xx)
    private ImageView username_xx;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.login_btn)
    private TextView login_btn;

    @ViewInject(R.id.wechat_login_btn)
    private ImageView wechat_login_btn;

    private String phone;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.login_activity_main);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("登录");
        setTopLeftDefultListener();
        initListener();
    }

    /*
         * 初始化监听器
         */
    public void initListener() {
        login_phone.addTextChangedListener(tw);
        login_pwd.addTextChangedListener(tw);
    }

    // 输入框内容改变的监听器
    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString();
            if (getCurrentFocus() == null) {
                return;
            }
            int current = getCurrentFocus().getId();
            if (content.equals("")) {
                if (current == R.id.login_phone) {
                    username_xx.setVisibility(View.INVISIBLE);
                } else if (current == R.id.login_pwd) {
                    pwd_xx.setVisibility(View.INVISIBLE);
                }
            } else {
                if (current == R.id.login_phone) {
                    username_xx.setVisibility(View.VISIBLE);
                } else if (current == R.id.login_pwd) {
                    pwd_xx.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Event(value = {R.id.register_btn,R.id.forget_pwd,R.id.login_btn,R.id.username_xx,R.id.pwd_xx,R.id.wechat_login_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view){
        int id = view.getId();
        Intent intent ;
        switch (id){
            case R.id.login_btn:
                login();
                break;
            case R.id.username_xx:
                login_phone.setText("");
                break;
            case R.id.pwd_xx:
                login_pwd.setText("");
                break;
            case R.id.register_btn:
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_pwd:
                intent = new Intent(LoginActivity.this,ForgetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.wechat_login_btn:
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                break;
        }
    }


    public static void navToLogin(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            for (String key : data.keySet()) {
                if (key.equals("openid")){
                    String openID = data.get(key);
                    SPUtils.putString(SPUtils.WX_OPEN_ID, openID);
                }
                if (key.equals("profile_image_url")){       // 头像地址
                    String headImageUrl = data.get(key);
                    SPUtils.putString(SPUtils.HEAD_PIC, headImageUrl);
                }
                if (key.equals("gender")){                  // 性别
                    String gender = data.get(key);
                    if (gender.equals("男")){
                        SPUtils.putInt(SPUtils.SEX, 1);
                    }else if(gender.equals("女")){
                        SPUtils.putInt(SPUtils.SEX, 2);
                    }else{
                        SPUtils.putInt(SPUtils.SEX, 0);
                    }
                }
                if (key.equals("name")){                    // 昵称
                    String nickname = data.get(key);
                    SPUtils.putString(SPUtils.NICK_NAME, nickname);
                }
            }
            showMsg("微信登录成功");
            personCenter("");
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            result.setText("错误" + t.getMessage());
            Log.i("错误" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REGISTER_VIEW_CODE){
            phone = data.getStringExtra("phone");
            pwd = data.getStringExtra("pwd");
            login_phone.setText(phone);
            login_pwd.setText(pwd);
        }

        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    private void login() {
        phone = login_phone.getText().toString();
        pwd = login_pwd.getText().toString();
        if (TextUtils.isEmpty(phone)){
            showMsg("请输入手机号码");
            return;
        }
        if (!RegExpValidator.IsHandset(phone)) {
            showMsg("手机号格式错误");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showMsg("请输入密码");
            return;
        }

        showLoading();
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.PERSONAL_LOGIN);
        requestParams.addParameter("username", phone);
        requestParams.addParameter("password", pwd);
        MXUtils.httpPost(requestParams, new CommonCallbackImp("登录",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");

                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        showMsg("登录成功");
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
                        String nickname = jsonObject1.getString("nickname");
                        String ub_id = jsonObject1.getString("ub_id");
                        UserBean ub = new UserBean();
                        ub.setUb_id(ub_id);
                        ub.setNickname(nickname);
                        SPUtils.setUserBean(ub);

                        SPUtils.putString(SPUtils.MOBILE,phone);
                        SPUtils.putString(SPUtils.PASSWORD,pwd);


                        // 请求个人中心
                        personCenter(ub_id);

//                        DemoApplication.getInstance().closeActivitys();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    // 个人中心
    private void personCenter(String ub_id) {
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.PERSONAL_CENTER);
        if (TextUtils.isEmpty(ub_id)){
            requestParams.addParameter("islogin", "2");
        }else{
            requestParams.addParameter("islogin", "1"); // 已登录
        }

        requestParams.addParameter("ub_id", ub_id);
        requestParams.addParameter("act_url", "");
        requestParams.addParameter("openid", SPUtils.getString(SPUtils.WX_OPEN_ID));
        requestParams.addParameter("headimgurl", SPUtils.getString(SPUtils.HEAD_PIC));
        requestParams.addParameter("sex", String.valueOf(SPUtils.getInt(SPUtils.SEX, 0)));
        requestParams.addParameter("nickname", SPUtils.getString(SPUtils.NICK_NAME));

        MXUtils.httpPost(requestParams, new CommonCallbackImp("个人中心",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                cancelLoading();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String userData = jsonObject.getString("data");
                        UserBean ub = GsonUtils.jsonToBean(userData, UserBean.class);
                        SPUtils.setUserBean(ub);

                        // 进入主页
                        DemoApplication.getInstance().closeActivitys();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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
