package cn.tthud.taitian.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.RegExpValidator;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class BindPhoneActivity extends ActivityBase {

    @ViewInject(R.id.login_phone)
    private EditText login_phone;

    @ViewInject(R.id.username_xx)
    private ImageView username_xx;

    @ViewInject(R.id.code)
    private EditText code;

    @ViewInject(R.id.login_send_code)
    private TextView login_send_code;

    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.submit_btn)
    private Button submit_btn;

    private String codeNum;     // 验证码
    private MyCount myCount;
    private String phone;       // 手机号码
    private String pwd;         // 密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appendMainBody(this,R.layout.activity_bind_phone);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("绑定手机号码");
        setTopLeftDefultListener();

        initView();
        initListener();
    }

    private void initView() {
        myCount = new MyCount(60000, 1000);
    }

    /*
         * 初始化监听器
         */
    public void initListener() {
        login_phone.addTextChangedListener(tw);
        login_pwd.addTextChangedListener(tw);
    }

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            int second = (int) (l / 1000);
            login_send_code.setText(second + "秒");
        }

        @Override
        public void onFinish() {
            login_send_code.setEnabled(true);
            login_send_code.setText("获取验证码");
        }
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

    @Event(value = {R.id.login_send_code,R.id.submit_btn,R.id.username_xx,R.id.pwd_xx},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.login_send_code:
                getCode();
                break;
            case R.id.submit_btn:
                submit();
                break;
            case R.id.username_xx:
                login_phone.setText("");
                break;
            case R.id.pwd_xx:
                login_pwd.setText("");

                break;
        }
    }

    // 获取验证码
    private void getCode(){
        phone = login_phone.getText().toString();
        if (TextUtils.isEmpty(phone)){
            showMsg("请输入手机号码");
            return;
        }
        if (!RegExpValidator.IsHandset(phone)){
            showMsg("手机号码格式错误");
            return;
        }
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.REGISTER_SEND_CODE);
        requestParams.addParameter("value", phone);
        MXUtils.httpGet(requestParams, new CommonCallbackImp("获取验证码",requestParams) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        codeNum = jsonObject.getString("data");
                        showMsg("验证码发送成功");
                        myCount.start();
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void submit() {
        phone = login_phone.getText().toString();
        pwd = login_pwd.getText().toString();
        if (TextUtils.isEmpty(phone)){
            showMsg("请输入手机号码");
            return;
        }
        if (!RegExpValidator.IsHandset(phone)){
            showMsg("手机号码格式错误");
            return;
        }

        if (TextUtils.isEmpty(pwd)){
            showMsg("请输入密码");
            return;
        }

        if (TextUtils.isEmpty(code.getText().toString())){
            showMsg("请输入手机验证码");
            return;
        }
        if (!codeNum.equals(code.getText().toString())) {
            showMsg("验证码输入错误");
            return;
        }
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.APP_BIND_PHONE);
        requestParams.addParameter("mobile", phone);
        requestParams.addParameter("password", pwd);
        requestParams.addParameter("openid", SPUtils.getString(SPUtils.WX_OPEN_ID));
        requestParams.addParameter("headpic", SPUtils.getString(SPUtils.HEAD_PIC));
        String sex_value = "0";
        if (SPUtils.getInt(SPUtils.SEX,0) == 1) {
            // 男
            sex_value = "1";
        } else if (SPUtils.getInt(SPUtils.SEX,0) == 2){
            // 女
            sex_value = "2";
        }
        requestParams.addParameter("sex", sex_value);
        requestParams.addParameter("nickname", SPUtils.getString(SPUtils.NICK_NAME));
        MXUtils.httpPost(requestParams, new CommonCallbackImp("绑定手机号码",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        showMsg("绑定成功");
                        String result = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(result);
                        String nickname = jsonObject1.getString("nickname");
                        String ub_id = jsonObject1.getString("ub_id");
                        SPUtils.putString(SPUtils.NICK_NAME,nickname);
                        SPUtils.putString(SPUtils.UB_ID,ub_id);
                        // 个人中心
                        personCenter();
                    }else {
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }

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
                                finish();
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
