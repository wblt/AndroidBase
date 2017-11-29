package cn.tthud.taitian.activity.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class RegisterActivity2 extends ActivityBase {

    @ViewInject(R.id.register_send_code)
    private TextView register_send_code;

    @ViewInject(R.id.register_phone)
    private EditText register_phone;

    @ViewInject(R.id.code)
    private EditText code;

    @ViewInject(R.id.username_xx)
    private ImageView username_xx;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.login_btn)
    private TextView login_btn;

    private MyCount myCount;
    private String phone;       // 手机号码
    private String pwd;         // 密码
    private String pwd2;       // 密码2
    private String codeNum;     // 验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.activity_register2);

        pwd = getIntent().getStringExtra("pwd");
        pwd2 = getIntent().getStringExtra("pwd2");

        initView();
        initListener();

    }

    /*
     * 初始化监听器
	 */
    public void initListener() {
        register_phone.addTextChangedListener(tw);
        code.addTextChangedListener(tw);

    }

    // 初始化视图
    public void initView() {
        myCount = new MyCount(60000, 1000);
    }

    @Event(value = {R.id.login_send_code,R.id.username_xx,
            R.id.pwd_xx,R.id.contain_btn,R.id.login_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.login_send_code:
                getCode();
                break;
            case R.id.contain_btn:
                registerBtnClick();
                break;
            case R.id.login_btn:
                // 前往登录页面
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                intent.putExtra("pwd",pwd);
                setResult(LoginActivity.REGISTER_VIEW_CODE,intent);
                finish();
                break;
            case R.id.username_xx:
                register_phone.setText("");
                break;
            case R.id.pwd_xx:
                code.setText("");
                break;
        }
    }

    // 获取验证码
    private void getCode(){
        phone = register_phone.getText().toString();
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

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            int second = (int) (l / 1000);
            register_send_code.setText(second + "秒");
        }

        @Override
        public void onFinish() {
            register_send_code.setEnabled(true);
            register_send_code.setText("获取验证码");
        }
    }

    // 输入框内容改变的监听器
    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString();
            if (getCurrentFocus() == null) {
                return;
            }
            int current = getCurrentFocus().getId();
            if (content.equals("")) {
                if (current == R.id.register_phone) {
                    username_xx.setVisibility(View.INVISIBLE);
                } else if (current == R.id.code) {
                    pwd_xx.setVisibility(View.INVISIBLE);
                }
            } else {
                if (current == R.id.register_phone) {
                    username_xx.setVisibility(View.VISIBLE);
                } else if (current == R.id.code) {
                    pwd_xx.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private void registerBtnClick(){

        if (TextUtils.isEmpty(phone)) {
            showMsg("请输入手机号码");
            return;
        }
        if (!RegExpValidator.IsHandset(phone)) {
            showMsg("手机号码格式错误");
            return;
        }
        if (TextUtils.isEmpty(codeNum)) {
            showMsg("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showMsg("请输入密码");
            return;
        }
        if (!pwd.equals(pwd2)) {
            showMsg("两次密码不一致");
            return;
        }
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.PERSONAL_REGISTER);
        requestParams.addParameter("mobile",phone);
        requestParams.addParameter("password",pwd);
        requestParams.addParameter("msg",codeNum);
        MXUtils.httpPost(requestParams, new CommonCallbackImp("注册",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");

                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        showMsg("注册成功");
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
