package cn.tthud.taitian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.activity.LoginActivity;
import cn.tthud.taitian.base.FragmentBase;

/**
 * Created by wb on 2017/10/8.
 */

public class MineFragment extends FragmentBase {
    private View view;

    @ViewInject(R.id.login_btn)
    private TextView login_btn;

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
        }
        return view;
    }

    @Event(value = {R.id.logout,R.id.dt_lay,R.id.user_center,R.id.my_camp,R.id.account_msg,R.id.msg_layout,
            R.id.about_lay,R.id.setting_lay,R.id.clear_lay,R.id.pl_lay,
            R.id.user_contact,R.id.user_fabu,R.id.user_save,R.id.service_lay,R.id.login_btn},type = View.OnClickListener.class)
    private void onEvenOnclick(View view){
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.login_btn:
                LoginActivity.navToLogin(this.getContext());
                break;
        }
    }




}
