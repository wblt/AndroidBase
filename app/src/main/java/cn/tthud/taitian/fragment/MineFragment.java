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
import cn.tthud.taitian.activity.mine.ChangePhoneActivity;
import cn.tthud.taitian.activity.mine.ModifyInfoActivity;
import cn.tthud.taitian.activity.mine.MyWalletActivity;
import cn.tthud.taitian.activity.mine.SettingActivity;
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

public class MineFragment extends FragmentBase implements ActionSheet.OnActionSheetSelected, DialogInterface.OnCancelListener{

    //城市选择返回码
    public static final int RESULT_CODE=100;
    //城市选择返回码
    public static final int HY_RESULT_CODE=102;
    //选择请求码
    public static final int REQUEST_CODE=101;

    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;

    private View view;

    @ViewInject(R.id.username)
    private TextView username;

    @ViewInject(R.id.headpic)
    private ImageView headpic;

    @ViewInject(R.id.img_sex)
    private ImageView img_sex;

    //临时保存拍照照片保存路径
    private String capturePath = "";
    private Bitmap bm;
    private String head_name;

    //是否更新了昵称
    private boolean hasNickname;
    //是否更新了头像
    private boolean hasHeadpic = false;
    private String headStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this,R.layout.mine_fragment);
            //appendTopBody(R.layout.activity_top_icon);
            //((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            //setTopBarTitle("我的");
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
        username.setText(SPUtils.getString(SPUtils.NICK_NAME));
        if (SPUtils.getInt(SPUtils.SEX,0) == 1) {
            img_sex.setImageResource(R.mipmap.sex_m);
        } else if (SPUtils.getInt(SPUtils.SEX,0) == 2){
            img_sex.setImageResource(R.mipmap.sex_w);
        }
    }

    @Event(value = {R.id.lay_qianbao,R.id.lay_advatar_upload,
            R.id.lay_person_info,R.id.lay_change_phone,R.id.lay_bind_phone,
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
            case R.id.lay_advatar_upload:   // 头像上传
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    ActionSheet.showSheet(getActivity(),
                            MineFragment.this, MineFragment.this, "1");
                } else {
                    startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.lay_person_info:      // 完善个人信息
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    startActivity(new Intent(this.getContext(), ModifyInfoActivity.class));
                } else {
                    startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.lay_change_phone:     // 修改手机号码
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    startActivity(new Intent(this.getContext(), ChangePhoneActivity.class));
                } else {
                    startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.lay_bind_phone:       // 绑定手机号码
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                }
                break;
            case R.id.lay_renwu:  // 任务

                break;
            case R.id.setting_lay: // 设置
                if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                    intent = new Intent(this.getContext(),SettingActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                    return;
                }
                break;
            case R.id.about_lay:  // 关于
                intent = new Intent(this.getContext(), AboutActivity.class);
                startActivity(intent);
                break;
        }
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

            // 更新本地的图像地址
            SPUtils.putString(SPUtils.HEAD_PIC,capturePath);
            ImageLoader.loadCircle(capturePath,headpic);
            hasHeadpic = true;
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
                Toast.makeText(getActivity(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
            }
        } else if (whichButton == 2) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");//相片类型
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
