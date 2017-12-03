package cn.tthud.taitian.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.ActivityBase;
import cn.tthud.taitian.bean.UserBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.Base64Util;
import cn.tthud.taitian.utils.GsonUtils;
import cn.tthud.taitian.utils.ImageLoader;
import cn.tthud.taitian.utils.RegExpValidator;
import cn.tthud.taitian.utils.SPUtils;
import cn.tthud.taitian.widget.ActionSheet;
import cn.tthud.taitian.xutils.CommonCallbackImp;
import cn.tthud.taitian.xutils.MXUtils;

public class ModifyInfoActivity extends ActivityBase implements  ActionSheet.OnActionSheetSelected, DialogInterface.OnCancelListener {

    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;

    //临时保存拍照照片保存路径
    private String capturePath = "";
    private Bitmap bm;
    private String head_name;


    @ViewInject(R.id.et_username)
    private EditText et_username;

    @ViewInject(R.id.headpic)
    private ImageView headpic;

    @ViewInject(R.id.et_name)
    private EditText et_name;

    @ViewInject(R.id.et_number_card)
    private EditText et_number_card;

    @ViewInject(R.id.sex)
    private TextView sex;

    @ViewInject(R.id.et_email)
    private EditText et_email;

    @ViewInject(R.id.et_address)
    private EditText et_address;

    @ViewInject(R.id.et_sign)
    private EditText et_sign;



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
                        JSONObject jsonObject1 = new JSONObject(userData);

                        String nickname = jsonObject1.getString("nickname");
                        String headpicurl = jsonObject1.getString("headpic");
                        int sex_value = jsonObject1.getInt("sex");
                        String ua_id = jsonObject1.getString("ua_id");
                        String realname = jsonObject1.getString("realname");
                        String idcard = jsonObject1.getString("idcard");
                        String email = jsonObject1.getString("email");
                        String stylesig = jsonObject1.getString("stylesig");
                        String address = jsonObject1.getString("address");
                        String totaljifen = jsonObject1.getString("totaljifen");
                        String ub_id = jsonObject1.getString("ub_id");

                        ImageLoader.loadCircle(headpicurl,headpic);
                        et_username.setText(nickname);
                        et_name.setText(realname);
                        et_number_card.setText(idcard);
                        if (sex_value == 1) {
                            sex.setText("男");
                        } else if (sex_value == 2) {
                            sex.setText("女");
                        } else {
                            sex.setText("");
                        }
                        et_email.setText(email);
                        et_address.setText(address);
                        et_sign.setText(stylesig);
                    }else{
                        showMsg(info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Event(value = {R.id.submit_btn,R.id.upload_headpic,R.id.sex_layout},type = View.OnClickListener.class)
    private void onEvenOnclick(View view){
        int id = view.getId();
        switch (id){
            case R.id.submit_btn:   // 修改资料
                modifyPersonInfo();
                break;
            case R.id.upload_headpic:
                ActionSheet.showSheet(this, this, this, "1");
                break;
            case R.id.sex_layout:
                ActionSheet.showSheet(this, this, this, "4");
                break;

        }
    }


    private void modifyPersonInfo(){
        final String nickName = et_username.getText().toString();
        if (TextUtils.isEmpty(nickName)){
            showMsg("昵称不能为空");
            return;
        }
        final String realName = et_name.getText().toString();
        if (TextUtils.isEmpty(realName)){
            showMsg("真实姓名不能为空");
            return;
        }
        final String idCard = et_number_card.getText().toString();
        if (TextUtils.isEmpty(idCard)){
            showMsg("身份证号码不能为空");
            return;
        }
        if (!RegExpValidator.IsIDcard(idCard)){
            showMsg("身份证号码格式不正确");
            return;
        }
        final String sexvalue = sex.getText().toString();
        if (TextUtils.isEmpty(sexvalue)){
            showMsg("请选择性别");
            return;
        }
        final String email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)){
            showMsg("邮箱不能为空");
            return;
        }
        if (!RegExpValidator.isEmail(email)){
            showMsg("邮箱格式不正确");
            return;
        }
        final String address = et_address.getText().toString();
        if (TextUtils.isEmpty(address)){
            showMsg("地址不能为空");
            return;
        }
        final String sign = et_sign.getText().toString();
        if (TextUtils.isEmpty(sign)){
            showMsg("个性签名不能为空");
            return;
        }
        showLoading();
        RequestParams requestParams = FlowAPI.getRequestParams(FlowAPI.PERSONAL_CHANGE_INFO);
        requestParams.addParameter("nickname",nickName);
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        requestParams.addParameter("realname", realName);
        requestParams.addParameter("idcard", idCard);
        int sex_temp = 0;
        if (sex.getText().toString().equals("男")){
            sex_temp = 1;
        }else if(sex.getText().toString().equals("女")){
            sex_temp = 2;
        }
        requestParams.addParameter("sex", sex_temp);
        requestParams.addParameter("email", email);
        requestParams.addParameter("address", address);
        requestParams.addParameter("stylesig", sign);

        final int finalSex_temp = sex_temp;
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
                        // 缓存本地信息
                        SPUtils.putString(SPUtils.NICK_NAME,nickName);
                        SPUtils.putString(SPUtils.REAL_NAME,realName);
                        SPUtils.putString(SPUtils.ID_CARD,idCard);
                        SPUtils.putInt(SPUtils.SEX, finalSex_temp);
                        SPUtils.putString(SPUtils.EMAIL,email);
                        SPUtils.putString(SPUtils.ADDRESS,address);
                        SPUtils.putString(SPUtils.STYLESIG,sign);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE  && resultCode != 0){
            Uri uri = Uri.fromFile(new File(capturePath));
            System.out.println("uri:"+ uri);
            startImageZoom(uri);
        }else if(requestCode == GALLERY_REQUEST_CODE  && resultCode != 0){
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            startImageZoom(uri);
        }else if(requestCode == CROP_REQUEST_CODE  && resultCode != 0){
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                return;
            }
            bm = extras.getParcelable("data");
            capturePath = saveBitmap(bm);

            System.out.println("xxxxxxxxxxxxxxxxxxxxxxx:" + capturePath);
            // 上传头像
            uploadHeader();
        }

    }

    private void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);

    }


    public byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private String saveBitmap(Bitmap bm) {
        head_name = System.currentTimeMillis() + "_head_small.jpg";
        String path = FlowAPI.YYW_FILE_PATH + head_name;
        File img = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onClick(int whichButton) {
        if (whichButton == 1) {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                String out_file_path = FlowAPI.YYW_FILE_PATH;
                File dir = new File(out_file_path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                capturePath = FlowAPI.YYW_FILE_PATH + System.currentTimeMillis() + "_head.jpg";
                getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
                getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(getImageByCamera, CAMERA_REQUEST_CODE);
            }
            else {
                Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
            }
        } else if (whichButton == 2) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");//相片类型
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } else if (whichButton == 8) {
            sex.setText("男");
        } else if (whichButton == 9) {
            sex.setText("女");
        }
    }

    private void uploadHeader() {
        byte[] bytes = Bitmap2Bytes(bm);
        RequestParams requestParams= FlowAPI.getRequestParams(FlowAPI.PERSONAL_UPDATE_HEDER);
        requestParams.addParameter("img", Base64Util.encode(bytes));
        requestParams.addParameter("ub_id", SPUtils.getString(SPUtils.UB_ID));
        MXUtils.httpPost(requestParams, new CommonCallbackImp("头像上传",requestParams) {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String info = jsonObject.getString("info");
                    if(FlowAPI.HttpResultCode.SUCCEED.equals(status)){
                        // 更新本地的图像地址
                        SPUtils.putString(SPUtils.HEAD_PIC,capturePath);
                        ImageLoader.loadCircle(capturePath,headpic);
                        showMsg("头像上传成功");
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
