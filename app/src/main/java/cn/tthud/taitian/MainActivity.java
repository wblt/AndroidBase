package cn.tthud.taitian;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.tthud.taitian.base.BaseActivity;
import cn.tthud.taitian.fragment.ContactListFragment;
import cn.tthud.taitian.fragment.DiscoverFragment;
import cn.tthud.taitian.fragment.HomeFragment;
import cn.tthud.taitian.fragment.MessageFragment;
import cn.tthud.taitian.fragment.MineFragment;
import cn.tthud.taitian.utils.CommonUtils;

public class MainActivity extends BaseActivity {
    // textview for unread message count
    private TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;
    private Button[] mTabs;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        HomeFragment homeFragment = new HomeFragment();
        DiscoverFragment disFragment = new DiscoverFragment();
        MineFragment mineFragment = new MineFragment();
//        ContactListFragment contactListFragment = new ContactListFragment();
        MessageFragment messageFragment = new MessageFragment();

        fragments = new Fragment[] {homeFragment,disFragment, messageFragment, mineFragment};

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .add(R.id.fragment_container, disFragment)
                .add(R.id.fragment_container, messageFragment)
//                .add(R.id.fragment_container, contactListFragment)
                .add(R.id.fragment_container, mineFragment)
                .hide(homeFragment)
                .hide(disFragment)
                .hide(messageFragment)
//                .hide(contactListFragment)
                .hide(mineFragment)
                .show(homeFragment)
                .commit();
    }

    /**
     * init views
     */
    private void initView() {
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_home);
        mTabs[1] = (Button) findViewById(R.id.btn_discover);
        mTabs[2] = (Button) findViewById(R.id.btn_conversation);
//        mTabs[3] = (Button) findViewById(R.id.btn_address_list);
        mTabs[3] = (Button) findViewById(R.id.btn_mine);
        // select first tab
        mTabs[0].setSelected(true);
    }

    /**
     * on tab clicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                index = 0;
                break;
            case R.id.btn_discover:
                index = 1;
                break;
            case R.id.btn_conversation:
                index = 2;
                break;
//            case R.id.btn_address_list:
//                index = 3;
//                break;
            case R.id.btn_mine:
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }
}
