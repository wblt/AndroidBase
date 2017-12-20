package cn.tthud.taitian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.tthud.taitian.activity.login.LoginActivity;
import cn.tthud.taitian.activity.mine.BindPhoneActivity;
import cn.tthud.taitian.base.BaseActivity;
import cn.tthud.taitian.db.dbmanager.MessageDaoUtils;
import cn.tthud.taitian.db.entity.Message;
import cn.tthud.taitian.fragment.ContactListFragment;
import cn.tthud.taitian.fragment.DiscoverFragment;
import cn.tthud.taitian.fragment.HomeFragment;
import cn.tthud.taitian.fragment.MessageFragment;
import cn.tthud.taitian.fragment.MineFragment;
import cn.tthud.taitian.net.rxbus.RxBus;
import cn.tthud.taitian.net.rxbus.RxBusBaseMessage;
import cn.tthud.taitian.net.rxbus.RxCodeConstants;
import cn.tthud.taitian.utils.CommonUtils;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.SPUtils;
import me.leolin.shortcutbadger.ShortcutBadger;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {
    // textview for unread message count
    private TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;
    private Button[] mTabs;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private Subscription subscription;
    private HomeFragment homeFragment;
    private DiscoverFragment disFragment;
    private MineFragment mineFragment;
    private MessageFragment messageFragment;
    private LinearLayout main_mask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DemoApplication.getInstance().addActivity(this);
        initView();
        homeFragment = new HomeFragment();
        disFragment = new DiscoverFragment();
        mineFragment = new MineFragment();
//        ContactListFragment contactListFragment = new ContactListFragment();
        messageFragment = new MessageFragment();
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
                .hide(homeFragment)
                .commit();
        DemoApplication.getInstance().setMainActivity(this);

        // select first tab
        int put_extra_index = getIntent().getIntExtra("extra_index",0);
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.show(fragments[put_extra_index]).commit();
        mTabs[put_extra_index].setSelected(true);
        currentTabIndex = put_extra_index;

        // 启动socket
        initSocket();

        // 初始化消息
        initRxBus();

        // 显示未读消息数字
        updateUnreadMsgLable();
    }

    /**
     * init views
     */
    private void initView() {
        main_mask = findViewById(R.id.main_mask);
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_home);
        mTabs[1] = (Button) findViewById(R.id.btn_discover);
        mTabs[2] = (Button) findViewById(R.id.btn_conversation);
//      mTabs[3] = (Button) findViewById(R.id.btn_address_list);
        mTabs[3] = (Button) findViewById(R.id.btn_mine);
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
                if (CommonUtils.checkLogin()) {
                    if (!SPUtils.getBoolean(SPUtils.ISVST,false)) {
                        index = 2;
                    } else {
                        // 绑定手机号码
                        startActivity(new Intent(this, BindPhoneActivity.class));
                    }
                } else {
                    LoginActivity.navToLogin(this);
                    return;
                }
                break;
            case R.id.btn_mine:
                if (CommonUtils.checkLogin()) {
                    index = 3;
                } else {
                    LoginActivity.navToLogin(this);
                    return;
                }
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

    /**
     * 初始化socket
     */
    private void initSocket() {
        ChatManager.getInstance().initSocket(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        ChatManager.getInstance().exitSocket(getApplicationContext());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(!subscription .isUnsubscribed()) {
            subscription .unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * update the total unread count
     */
    private void updateUnreadMsgLable() {
        // 开启sevice
        if (!CommonUtils.checkLogin()) {
            return;
        }
        if (SPUtils.getBoolean(SPUtils.ISVST,false)) {
            // 游客返回
            return;
        }
        runOnUiThread(new Runnable() {
            public void run() {
                // 查找数据库数据
                MessageDaoUtils messageDaoUtils = new MessageDaoUtils(MainActivity.this);
                List<Message> lists = messageDaoUtils.queryAllMessage();
                int num = 0;
                for (Message msg:lists) {
                    if (msg.getIsread().equals("2")) {
                        num = num + 1;
                    }
                }
                if (num == 0) {
                    unreadLabel.setVisibility(View.INVISIBLE);
                    SPUtils.putInt(SPUtils.BADGER_NUM,0);
                    ShortcutBadger.removeCount(MainActivity.this);
                } else {
                    if (num > 99) {
                        unreadLabel.setText("..");
                    } else {
                        unreadLabel.setText(num+"");
                    }
                    unreadLabel.setVisibility(View.VISIBLE);
                    SPUtils.putInt(SPUtils.BADGER_NUM,num);
                    ShortcutBadger.applyCount(MainActivity.this, num);
                }

            }
        });

    }

    /**
     * 收到通知后，获取用户信息，存在内存
     */
    private void initRxBus() {
        subscription = RxBus.getDefault().toObservable(RxCodeConstants.MainActivity_MSG, RxBusBaseMessage.class)
                .subscribe(new Action1<RxBusBaseMessage>() {
                    @Override
                    public void call(RxBusBaseMessage integer) {
                        Log.i(integer.getObject().toString());
                        String status = integer.getObject().toString();
                        String split[] = status.split(",");
                        if (split[0].equals("updateMsg")) {
                            updateUnreadMsgLable();
                            // 发送消息去
                            RxBus.getDefault().post(RxCodeConstants.MessageFragment_MSG, new RxBusBaseMessage(1,"updateMsg"));
                        } else if (split[0].equals("onClose")) {
                            handleOnCloseMsg(split[1]);
                        } else if (split[0].equals("dismiss_mask")) {
                            main_mask.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void handleOnCloseMsg(String info) {
        ChatManager.getInstance().exitSocket(getApplicationContext());
        SPUtils.clearUser();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.putExtra("onClose",info);
        startActivity(intent);
        DemoApplication.getInstance().closeActivitys();
        finish();
    }
}
