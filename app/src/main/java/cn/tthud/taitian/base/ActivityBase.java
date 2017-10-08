package cn.tthud.taitian.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.xutils.x;

import cn.tthud.taitian.R;
import cn.tthud.taitian.widget.CustomProgressDialog;
import cn.tthud.taitian.widget.ZProgressHUD;

/**
 * Created by wanglei on 2016/4/21.
 */
public abstract class ActivityBase extends FragmentActivity {

    private LinearLayout base_main;
    private LinearLayout base_top;
    private CustomProgressDialog customProgressDialog;
    private boolean isRun;
    // 布局view
    protected ViewDataBinding bindingView;



    ZProgressHUD progressHUD;
    private boolean mIsShowLoading;//是否显示加载loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_frame);
        progressHUD = ZProgressHUD.getInstance(this);
        initBaseView();
//        checkData();
    }

//    private void checkData(){
//        if(DataProvider.pinpai_list0.size()==0 && !TextUtils.isEmpty(SPUtils.getString("pinpai_list"))){
//            DataProvider.setData();
//            Type type=new TypeToken<List<ConfigBean>>(){}.getType();
//            List<ConfigBean> diaryPojoList1= GsonUtils.jsonToList(SPUtils.getString("pinpai_list"),type);
//            DataProvider.pinpai_list.addAll(diaryPojoList1);
//            DataProvider.pinpai_list0.addAll(diaryPojoList1);
//
//            List<ConfigBean> diaryPojoList2= GsonUtils.jsonToList(SPUtils.getString("price_list"),type);
//            DataProvider.price_list.addAll(diaryPojoList2);
//            DataProvider.price_list0.addAll(diaryPojoList2);
//
//            List<ConfigBean> diaryPojoList3= GsonUtils.jsonToList(SPUtils.getString("leixing_list"),type);
//            DataProvider.leixing_list.addAll(diaryPojoList3);
//            DataProvider.leixing_list0.addAll(diaryPojoList3);
//        }
//    }

    protected void initBaseView(){
        base_main = (LinearLayout) findViewById(R.id.base_main);
        base_top = (LinearLayout) findViewById(R.id.base_top);
    }

    public ZProgressHUD getProgressHUD() {
        return progressHUD;
    }

    public void setProgressHUD(ZProgressHUD progressHUD) {
        this.progressHUD = progressHUD;
    }

   /**
    * 设置页面主体内容
    */
    protected void appendMainBody(Object object, int pResID) {
        View view = LayoutInflater.from(this).inflate(pResID, null);
        x.view().inject(object,view);
        RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        base_main.addView(view,_LayoutParams);

//        bindingView = DataBindingUtil.inflate(getLayoutInflater(), pResID, null, false);
//
//        // content
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        bindingView.getRoot().setLayoutParams(params);
//        base_main.addView(bindingView.getRoot(),params);
    }

    /**
     * 设置页面主体内容
     */
    protected void appendMainBody2(Object object, int pResID) {
//        View view = LayoutInflater.from(this).inflate(pResID, null);
//        x.view().inject(object,view);
//        RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        base_main.addView(view,_LayoutParams);

        bindingView = DataBindingUtil.inflate(getLayoutInflater(), pResID, null, false);

        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        base_main.addView(bindingView.getRoot(),params);
    }

    /**
     * 设置页面头部内容
     */
    protected void appendTopBody(int pResID) {
        base_top.setVisibility(View.VISIBLE);
        View view = LayoutInflater.from(this).inflate(pResID,null);
        base_top.addView(view);
    }

    /**
     * 设置页面头部标题
     */
    protected void setTopBarTitle(String pText) {
        TextView tvTitle = (TextView) findViewById(R.id.top_center_text);
        if (tvTitle!=null) {
            tvTitle.setText(pText);
        }
    }

    /**
     * 弹出提示信息
     */
    public void showMsg(String pMsg) {
        Toast t = Toast.makeText(this,pMsg, Toast.LENGTH_LONG);
        t.show();
    }

    /**
     * 可在此处统一显示loading view
     */
    public void showLoading() {
//        if (mIsShowLoading) {
            if(progressHUD == null){
                progressHUD = ZProgressHUD.getInstance(this);
            }
            progressHUD.show();
//        }
    }

    public void cancelLoading() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }

    /**
     * 弹出加载框 不带文字
     */
    public CustomProgressDialog showProgressDialog() {
        if (isRun) {
            return null;
        }
        if(customProgressDialog==null ){
            customProgressDialog = CustomProgressDialog.createDialog(this);
            customProgressDialog.setMessage("请稍等");
            customProgressDialog.setCanceledOnTouchOutside(false);
        }
        customProgressDialog.show();
        return customProgressDialog ;
    }

    public CustomProgressDialog setProgressDialogMessage(int resId){
        String message=getResources().getString(resId);
        return setProgressDialogMessage(message);
    }
    public CustomProgressDialog setProgressDialogMessage(String message){
        if(customProgressDialog!=null){
            customProgressDialog.setMessage(message);
        }
        return customProgressDialog;
    }
    /**
     * 隐藏加载框
     */
    public void dismissProgressDialog() {
        if(customProgressDialog!=null){
            customProgressDialog.dismiss();
        }
    }

    /**
     * 设置返回事件
     */
    public void setTopLeftListener(final Activity activity){
        ImageButton top_left = (ImageButton) findViewById(R.id.top_left);
        top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    /**
     *  默认监听
     */
    protected void setTopLeftDefultListener(){
        View leftView=findViewById(R.id.top_left);
        if(leftView!=null){
            leftView.setVisibility(View.VISIBLE);
            leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if(customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        super.onDestroy();
    }
}
