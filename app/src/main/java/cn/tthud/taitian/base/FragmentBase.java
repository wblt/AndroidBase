package cn.tthud.taitian.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.xutils.x;

import cn.tthud.taitian.R;
import cn.tthud.taitian.widget.CustomProgressDialog;

/**
 * Created by wenshi on 2016/4/26.
 */
public abstract class FragmentBase extends Fragment {
    private LinearLayout base_main;
    private LinearLayout base_top;
    protected View mainview;
    private LayoutInflater inflater;
    private ViewGroup container;

    private CustomProgressDialog m_ProgressDialog;
    private boolean isRun;

    // 布局view
    protected ViewDataBinding bindingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview =  inflater.inflate(R.layout.activity_base_frame, null);
        base_main = (LinearLayout)mainview.findViewById(R.id.base_main);
        base_top = (LinearLayout) mainview.findViewById(R.id.base_top);
        this.inflater = inflater;
        this.container = container;
        return mainview;
    }

    protected void appendMainBody(Object object, int pResID) {
        View view = inflater.inflate(pResID, null);
        x.view().inject(object,view);
        RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        base_main.addView(view,_LayoutParams);
    }

    protected void appendMainBody2(Object object, int pResID) {
//        View view = inflater.inflate(pResID, null);
//        x.view().inject(object,view);
//        RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        base_main.addView(view,_LayoutParams);

        bindingView = DataBindingUtil.inflate(inflater, pResID, null, false);

        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        base_main.addView(bindingView.getRoot(),params);
    }

    /**
     * 包含返回和设置操作顶部栏
     */
    protected void appendTopBody(int pResID) {
        base_top.setVisibility(View.VISIBLE);
        View view = inflater.inflate(pResID,null);
        base_top.addView(view);
    }

    protected void setTopBarTitle(String pText) {
        TextView tvTitle = (TextView)mainview.findViewById(R.id.top_center_text);
        if (tvTitle!=null) {
            tvTitle.setText(pText);
        }
    }


    /**
     * 打开新的页面 带打开效果
     */
    public void startActivityForResultSlide(Intent intent, int code){
        startActivityForResult(intent,code);
        getActivity().overridePendingTransition(R.anim.push_right_in_0100_0,
                R.anim.push_no_out);
    }

    /**
     * 弹出提示信息
     */
    public void showMsg(String pMsg) {
        Toast t = Toast.makeText(this.getContext(),pMsg, Toast.LENGTH_LONG);
        t.show();
    }

    /**
     * 弹出加载框 不带文字
     */
    public CustomProgressDialog showProgressDialog() {
        if (isRun) {
            return null;
        }
        if(m_ProgressDialog==null ){
            m_ProgressDialog = CustomProgressDialog.createDialog(this.getContext());
            m_ProgressDialog.setMessage("请稍等");
            m_ProgressDialog.setCanceledOnTouchOutside(false);
            m_ProgressDialog.show();
        }
        if(m_ProgressDialog!=null && !m_ProgressDialog.isShowing()){
            m_ProgressDialog.setMessage("请稍等");
            m_ProgressDialog.setCanceledOnTouchOutside(false);
            m_ProgressDialog.show();
        }
        return m_ProgressDialog ;
    }

    /**
     * 隐藏加载框
     */
    public void dismissProgressDialog() {
        if (m_ProgressDialog != null) {
            m_ProgressDialog.dismiss();
        }
    }
}
