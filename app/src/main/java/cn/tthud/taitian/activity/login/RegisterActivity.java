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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
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

public class RegisterActivity extends ActivityBase {

    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;

    @ViewInject(R.id.login_pwd2)
    private EditText login_pwd2;

    @ViewInject(R.id.lay_xieyi)
    private LinearLayout lay_xieyi;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.pwd_xx2)
    private ImageView pwd_xx2;

    @ViewInject(R.id.btn_mine)
    private Button btn_mine;

    @ViewInject(R.id.next_btn)
    private Button next_btn;

    boolean btn_select = true;
    private String pwd = "";
    private String pwd2 = "";

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.register_activity_main);
//        appendTopBody(R.layout.activity_top_text);
//        setTopBarTitle("注册");
//        setTopLeftListener(this);
        initView();
        initListener();
        initRxBus();
    }

    // 初始化视图
    public void initView() {
        btn_mine.setSelected(true);
    }

    /*
     * 初始化监听器
	 */
    public void initListener() {
        login_pwd.addTextChangedListener(tw);
        login_pwd2.addTextChangedListener(tw);
    }

    @Event(value = {R.id.top_left,R.id.pwd_xx,R.id.pwd_xx2,R.id.lay_xieyi,R.id.next_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.lay_xieyi:
                break;
            case R.id.pwd_xx:
                login_pwd.setText("");
                break;
            case R.id.pwd_xx2:
                login_pwd2.setText("");
                break;
            case R.id.next_btn: // 注册
                pwd = login_pwd.getText().toString();
                pwd2 = login_pwd2.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    showMsg("密码输入为空");
                    return;
                }
                if (!pwd.equals(pwd2)) {
                    showMsg("密码输入不一致");
                    return;
                }
                Intent intent = new Intent(this,RegisterActivity2.class);
                intent.putExtra("pwd",pwd);
                intent.putExtra("pwd2",pwd2);
                startActivity(intent);
                break;
            case R.id.top_left:
                finish();
                break;
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
                if (current == R.id.login_pwd) {
                    pwd_xx.setVisibility(View.INVISIBLE);
                } else if (current == R.id.login_pwd2) {
                    pwd_xx2.setVisibility(View.INVISIBLE);
                }
            } else {
                if (current == R.id.login_pwd) {
                    pwd_xx.setVisibility(View.VISIBLE);
                } else if (current == R.id.login_pwd2) {
                    pwd_xx2.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public void onTabClicked(View view) {
        if (btn_select == false) {
            btn_select = true;
            btn_mine.setSelected(true);
        } else {
            btn_select = false;
            btn_mine.setSelected(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!subscription .isUnsubscribed()) {
            subscription .unsubscribe();
        }
    }

    /**
     * 收到通知后，获取用户信息，存在内存
     */
    private void initRxBus() {
        subscription = RxBus.getDefault().toObservable(RxCodeConstants.RegisterActivity2_finsh, RxBusBaseMessage.class)
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
