package cn.tthud.taitian.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by bopeng on 2017/11/1.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> lists) {
        super(fm);

        this.list = lists;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
