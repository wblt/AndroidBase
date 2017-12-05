package cn.tthud.taitian.activity.mine;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import cn.tthud.taitian.utils.RegExpValidator;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class ModifyPhoneActivity extends ActivityBase {


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
        appendMainBody(this, R.layout.activity_modify_phone);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("修改手机号码");
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

        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.PERSONAL_CHANGE_TEL);
        requestParams.addParameter("password", pwd);
        requestParams.addParameter("mobile", phone);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        MXUtils.httpGet(requestParams, new CommonCallbackImp("获取验证码",requestParams) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        showMsg("修改成功");
                        SPUtils.putString(SPUtils.MOBILE,phone);
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
