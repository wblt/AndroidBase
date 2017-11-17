package cn.tthud.taitian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.tthud.taitian.R;
import cn.tthud.taitian.base.FragmentBase;

/**
 * Created by bopeng on 2017/11/6.
 */

public class CompanyIntroduceFragment extends FragmentBase {
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view =  inflater.inflate(R.layout.fragment_company_introduce, null);
        }
        return view;
    }
}
