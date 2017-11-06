package cn.tthud.taitian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.xutils.view.annotation.ViewInject;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.FragmentBase;
import cn.tthud.taitian.widget.banner.SimpleImageBanner;

/**
 * Created by wb on 2017/10/8.
 */

public class HomeFragment extends FragmentBase {

    private View view;

    @ViewInject(R.id.sib_simple_usage)
    private SimpleImageBanner sib_simple_usage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater,container,savedInstanceState);
            appendMainBody(this, R.layout.home_fragment);
            appendTopBody(R.layout.activity_top_icon);
            ((ImageButton) view.findViewById(R.id.top_left)).setVisibility(View.INVISIBLE);
            setTopBarTitle("首页");
        }
        return view;
    }





}
