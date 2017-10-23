package cn.tthud.taitian.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import cn.tthud.taitian.activity.mine.BindPhoneActivity;
import cn.tthud.taitian.activity.mine.ModifyInfoActivity;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.bean.UserBean;
import cn.tthud.taitian.net.FlowAPI;
import cn.tthud.taitian.utils.Base64Util;
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
    @ViewInject(R.id.login_btn)
    private TextView login_btn;

    @ViewInject(R.id.username)
    private TextView username;

    @ViewInject(R.id.logout)
    private TextView logout;

    @ViewInject(R.id.headpic)
    private ImageView headpic;

    @ViewInject(R.id.msg_layout)
    private RelativeLayout msg_layout;


    @ViewInject(R.id.lay_login)
    private LinearLayout lay_login;

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
            appendTopBody(R.layout.activity_top_icon);
            ((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            setTopBarTitle("我的");
            initView();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    public void initView(){
        if(TextUtils.isEmpty(SPUtils.getString(SPUtils.UB_ID))){
            msg_layout.setVisibility(View.GONE);
            lay_login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }else {
            msg_layout.setVisibility(View.VISIBLE);
            lay_login.setVisibility(View.GONE);
            ImageLoader.loadCircle(SPUtils.getString(SPUtils.HEAD_PIC),headpic);
            username.setText(SPUtils.getString(SPUtils.NICK_NAME));
            logout.setVisibility(View.VISIBLE);
        }
    }

    @Event(value = {R.id.logout,R.id.lay_qianbao,R.id.lay_advatar_upload,
            R.id.lay_person_info,R.id.lay_change_phone,R.id.lay_bind_phone,
            R.id.login_btn},type = View.OnClickListener.class)

    private void onEvenOnclick(View view){
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.login_btn:            // 登录
                LoginActivity.navToLogin(this.getContext());
                break;
            case R.id.lay_qianbao:          // 我的钱包
                break;
            case R.id.lay_advatar_upload:   // 头像上传
                ActionSheet.showSheet(getActivity(),
                        MineFragment.this, MineFragment.this, "1");
                break;
            case R.id.lay_person_info:      // 完善个人信息
                startActivity(new Intent(this.getContext(), ModifyInfoActivity.class));
                break;
            case R.id.lay_change_phone:     // 修改手机号码
                startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                break;
            case R.id.lay_bind_phone:       // 绑定手机号码
                startActivity(new Intent(this.getContext(), BindPhoneActivity.class));
                break;
            case R.id.logout:               // 退出登录
                logout();
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

    //登出
    private void logout(){
        SPUtils.clearUser();
        LoginActivity.navToLogin(MineFragment.this.getContext());
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

}
