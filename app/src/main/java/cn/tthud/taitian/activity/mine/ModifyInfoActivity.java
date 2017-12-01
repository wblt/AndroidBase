package cn.tthud.taitian.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.bean.UserBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.RegExpValidator;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class ModifyInfoActivity extends ActivityBase {

    @ViewInject(R.id.et_username)
    private EditText et_username;

    @ViewInject(R.id.et_name)
    private EditText et_name;

    @ViewInject(R.id.et_number_card)
    private EditText et_number_card;

    @ViewInject(R.id.radio_male)
    private RadioButton radio_male;

    @ViewInject(R.id.radio_female)
    private RadioButton radio_female;

    @ViewInject(R.id.et_email)
    private EditText et_email;

    @ViewInject(R.id.et_sign)
    private EditText et_sign;

    @ViewInject(R.id.et_address)
    private EditText et_address;

    private UserBean ub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appendMainBody(this,R.layout.activity_modify_info);
        appendTopBody(R.layout.activity_top_text);
        setTopBarTitle("完善个人信息");
        setTopLeftDefultListener();

        loadPersonInfo();
    }

    // 加载个人信息
    private void loadPersonInfo(){
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.PERSONAL_INFO);
        requestParams.addParameter("ub_id",SPUtils.getString(SPUtils.UB_ID));

        MXUtils.httpGet(requestParams, new CommonCallbackImp("获取个人信息",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        String userData = jsonObject.getString("data");
                        ub = GsonUtils.jsonToBean(userData, UserBean.class);

                        et_username.setText(ub.getNickname());
                        et_name.setText(ub.getRealname());
                        et_number_card.setText(ub.getIdcard());
                        radio_male.setChecked(ub.getSex() == 1);        // 男
                        radio_female.setChecked(ub.getSex() == 2);      // 女
                        et_email.setText(ub.getEmail());
                        et_sign.setText(ub.getStylesig());
                        et_address.setText(ub.getAddress());

                    }else{
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Event(value = {R.id.submit_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view){
        int id = view.getId();
        switch (id){
            case R.id.submit_btn:   // 修改资料
                modifyPersonInfo();
                break;
        }
    }


    private void modifyPersonInfo(){
        String nickName = et_username.getText().toString();
        if (TextUtils.isEmpty(nickName)){
            showMsg("昵称不能为空");
            return;
        }
        String realName = et_name.getText().toString();
        if (TextUtils.isEmpty(realName)){
            showMsg("真实姓名不能为空");
            return;
        }
        String idCard = et_number_card.getText().toString();
        if (TextUtils.isEmpty(idCard)){
            showMsg("身份证号码不能为空");
            return;
        }
        if (!RegExpValidator.IsIDcard(idCard)){
            showMsg("身份证号码格式不正确");
            return;
        }
        if (!radio_male.isChecked() && !radio_female.isChecked()){
            showMsg("请选择性别");
            return;
        }
        String email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)){
            showMsg("邮箱不能为空");
            return;
        }
        if (!RegExpValidator.isEmail(email)){
            showMsg("邮箱格式不正确");
            return;
        }
        String sign = et_sign.getText().toString();
        if (TextUtils.isEmpty(sign)){
            showMsg("个性签名不能为空");
            return;
        }
        String address = et_address.getText().toString();
        if (TextUtils.isEmpty(address)){
            showMsg("地址不能为空");
            return;
        }

        showLoading();

        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.PERSONAL_CHANGE_INFO);

        requestParams.addParameter("nickname",nickName);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("realname", realName);
        requestParams.addParameter("idcard", idCard);

        int sex_temp = 0;
        if (radio_male.isChecked()){
            sex_temp = 1;
        }else if(radio_female.isChecked()){
            sex_temp = 2;
        }
        requestParams.addParameter("sex", sex_temp);
        requestParams.addParameter("email", email);
        requestParams.addParameter("stylesig", sign);
        requestParams.addParameter("address", address);

        ub.setNickname(nickName);
        ub.setRealname(realName);
        ub.setIdcard(idCard);
        ub.setSex(sex_temp);
        ub.setEmail(email);
        ub.setStylesig(sign);
        ub.setAddress(address);

        MXUtils.httpPost(requestParams, new CommonCallbackImp("修改个人信息",requestParams){
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                cancelLoading();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");

                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        showMsg(info);
//                        SPUtils.setUserBean(ub);
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
}
