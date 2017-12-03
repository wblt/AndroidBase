package cn.tthud.taitian.activity.login;

import android.content.Intent;
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
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class ForgetPwdActivity2 extends ActivityBase {

    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;

    @ViewInject(R.id.login_pwd2)
    private EditText login_pwd2;

    @ViewInject(R.id.pwd_xx)
    private ImageView pwd_xx;

    @ViewInject(R.id.pwd_xx2)
    private ImageView pwd_xx2;

    @ViewInject(R.id.commit_btn)
    private Button commit_btn;

    @ViewInject(R.id.lay_xieyi)
    private LinearLayout lay_xieyi;

    @ViewInject(R.id.btn_mine)
    private Button btn_mine;

    boolean btn_select = true;
    private String phone = "";
    private String pwd = "";
    private String codeNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(this,R.layout.activity_forget_pwd2);

        phone = getIntent().getStringExtra("phone");
        codeNum = getIntent().getStringExtra("code");

        initView();
        initListener();
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

    @Event(value = {R.id.top_left,R.id.pwd_xx,R.id.pwd_xx2,R.id.lay_xieyi,R.id.commit_btn},type = View.OnClickListener.class)
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
            case R.id.commit_btn: // 提交
                pwd = login_pwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    showMsg("密码输入为空");
                    return;
                }
                if (!pwd.equals(login_pwd2.getText().toString())) {
                    showMsg("密码输入不一致");
                    return;
                }
                commit();
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
                if (current == R.id.register_phone) {
                    pwd_xx.setVisibility(View.INVISIBLE);
                } else if (current == R.id.code) {
                    pwd_xx2.setVisibility(View.INVISIBLE);
                }
            } else {
                if (current == R.id.register_phone) {
                    pwd_xx.setVisibility(View.VISIBLE);
                } else if (current == R.id.code) {
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

    private void commit() {
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.APP_FORGET_PWD);
        requestParams.addParameter("mobile",phone);
        requestParams.addParameter("password",pwd);
        MXUtils.httpPost(requestParams, new CommonCallbackImp("注册",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        showMsg("提交成功");
                        RxBus.getDefault().post(RxCodeConstants.ForgetPwdActivity2_finsh, new RxBusBaseMessage(1,"finsh"));
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
