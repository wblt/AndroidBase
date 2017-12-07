package cn.tthud.taitian.activity.login;

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
import cn.tthud.taitian.net.rxbus.RxBus;
import cn.tthud.taitian.net.rxbus.RxBusBaseMessage;
import cn.tthud.taitian.net.rxbus.RxCodeConstants;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.RegExpValidator;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;
import rx.Subscription;
import rx.functions.Action1;

public class ForgetPwdActivity extends ActivityBase {


    @ViewInject(R.id.forget_phone)
    private EditText forget_phone;

    @ViewInject(R.id.code)
    private EditText code;

    @ViewInject(R.id.forget_send_code)
    private TextView forget_send_code;

    @ViewInject(R.id.login_btn)
    private TextView login_btn;


    @ViewInject(R.id.username_xx)
    private ImageView username_xx;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.contain_btn)
    private Button contain_btn;

    private MyCount myCount;
    private String phone;       // 手机号码
    private String codeNum;     // 验证码

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.forget_pwd_activity_main);
//        appendTopBody(R.layout.activity_top_text);
//        setTopBarTitle("找回密码");
//        setTopLeftListener(this);
        initView();
        initListener();
        initRxBus();
    }

    // 初始化视图
    public void initView() {
        forget_phone.addTextChangedListener(tw);
        code.addTextChangedListener(tw);
    }

    /*
     * 初始化监听器
     */
    public void initListener() {
        myCount = new MyCount(60000, 1000);
    }

    @Event(value = {R.id.top_left,R.id.forget_send_code,R.id.username_xx,R.id.pwd_xx,R.id.login_btn,R.id.contain_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.forget_send_code:
                getCode();
                break;
            case R.id.username_xx:
                forget_phone.setText("");
                break;
            case R.id.pwd_xx:
                code.setText("");
                break;
            case R.id.login_btn:
                // 前往登录页面
                finish();
                break;
            case R.id.contain_btn:
                if (TextUtils.isEmpty(phone)){
                    showMsg("请输入手机号码");
                    return;
                }
                if (!RegExpValidator.IsHandset(phone)) {
                    showMsg("手机号格式错误");
                    return;
                }
                if (TextUtils.isEmpty(code.getText().toString())) {
                    showMsg("验证码输入为空");
                    return;
                }
                if (!codeNum.equals(code.getText().toString())) {
                    showMsg("验证输入错误");
                    return;
                }
                Intent intent = new Intent(this,ForgetPwdActivity2.class);
                intent.putExtra("phone",phone);
                intent.putExtra("code",codeNum);
                startActivity(intent);
                break;
            case R.id.top_left:
                finish();
                break;

        }
    }

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            int second = (int) (l / 1000);
            forget_send_code.setText(second + "秒");
        }

        @Override
        public void onFinish() {
            forget_send_code.setEnabled(true);
            forget_send_code.setText("获取验证码");
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
                if (current == R.id.forget_phone) {
                    username_xx.setVisibility(View.INVISIBLE);
                } else if (current == R.id.code) {
                    //pwd_xx.setVisibility(View.INVISIBLE);
                }
            } else {
                if (current == R.id.forget_phone) {
                    username_xx.setVisibility(View.VISIBLE);
                } else if (current == R.id.code) {
                    //pwd_xx.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    // 获取验证码
    private void getCode(){
        phone = forget_phone.getText().toString();
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
                        codeNum = jsonObject.getString("data");
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

    @Override
    protected void onDestroy() {
        if(!subscription .isUnsubscribed()) {
            subscription .unsubscribe();
        }
        super.onDestroy();

    }

    /**
     * 收到通知后，获取用户信息，存在内存
     */
    private void initRxBus() {
        subscription = RxBus.getDefault().toObservable(RxCodeConstants.ForgetPwdActivity2_finsh, RxBusBaseMessage.class)
                .subscribe(new Action1<RxBusBaseMessage>() {
                    @Override
                    public void call(RxBusBaseMessage integer) {
                        Log.i(integer.getObject().toString());
                        String status = integer.getObject().toString();
                        if (status.equals("finsh")) {
                            RxBus.getDefault().post(RxCodeConstants.RegisterActivity1_finsh, new RxBusBaseMessage(1,"logininfo"));
                            finish();
                        }
                    }
                });
    }
}
