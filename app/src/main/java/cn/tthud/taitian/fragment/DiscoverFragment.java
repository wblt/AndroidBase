package cn.tthud.taitian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.utils.Log;

/**
 * Created by wb on 2017/10/8.
 */

public class DiscoverFragment extends FragmentBase implements View.OnClickListener {

    private View view;
    private ImageButton top_right_button;
    private ImageButton top_left_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this, R.layout.discover_fragment);
            appendTopBody(R.layout.activity_top_icon);
            setTopBarTitle("活动列表");

            initNaviBarView();
            initView();
        }
        return view;
    }

    public void initNaviBarView(){
        top_right_button = (ImageButton) view.findViewById(R.id.top_right_icon);
        top_right_button.setImageResource(R.drawable.search);
        top_right_button.setOnClickListener(this);

        top_left_button = (ImageButton) view.findViewById(R.id.top_left);
        top_left_button.setImageResource(R.drawable.mine_setting);
        top_left_button.setOnClickListener(this);
    }

    public void initView(){

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.top_right_icon:
                Log.i("点击了右侧按钮");
                break;
            case R.id.top_left:
                Log.i("点击左侧按钮");
                break;
        }
    }
}
