package cn.tthud.taitian.utils;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.ArrayList;
import java.util.List;

import cn.tthud.taitian.DemoApplication;
import cn.tthud.taitian.bean.UserBean;
import cn.tthud.taitian.bean.VersionBean;

/**
 * Created by jingbin on 2015/2/26.
 */
public class SPUtils {
    private static final String CONFIG = "config";
    private static final String CONFIG_APP = "config_app";

    // 判断是否登录
    public static final String IS_LOGIN = "is_login";
    public static final String SOURCE = "source";

    public static final String UB_ID = "ub_id";                 // 用户id
    public static final String NICK_NAME = "nickname";          // 昵称
    public static final String HEAD_PIC = "headpic";            // 头像地址
    public static final String SEX = "sex";                     // 性别
    public static final String UA_ID = "ua_id";                 // 账号id
    public static final String REAL_NAME = "realname";          // 真实姓名
    public static final String ID_CARD = "idcard";              // 身份证号码
    public static final String EMAIL = "email";                 // 邮箱
    public static final String MOBILE = "mobile";               // 电话
    public static final String PASSWORD = "password";           // 密码
    public static final String STYLESIG = "stylesig";           // 个性签名
    public static final String ADDRESS = "address";             // 地址
    public static final String TOTALJIFEN = "totaljifen";       // 积分
    public static final String WX_OPEN_ID = "wechatOpenID";     // 微信openid
    public static final String ISVST = "isvst";                 // 游客or用户
    public static final String IS_BINDWX  = "isbindwx";         // 是否绑定微信
    public static final String H5_URL = "h5_url";               // h5地址
    public static final String BADGER_NUM = "badger_num";       // bager_num
    public static final String FIRST_MESSAGE = "first_message";       // bager_num

    /**
     * 清空SharedPreferences实例对象
     *
     */
    public static void clearUser() {
        SPUtils.putString(SOURCE,"");
        SPUtils.putString(IS_LOGIN,"");
        SPUtils.putString(UB_ID, "");
        SPUtils.putString(NICK_NAME, "");
        SPUtils.putString(HEAD_PIC, "");
        SPUtils.putInt(SEX, 0);
        SPUtils.putString(UA_ID, "");
        SPUtils.putString(REAL_NAME, "");
        SPUtils.putString(ID_CARD, "");
        SPUtils.putString(EMAIL, "");
        SPUtils.putString(STYLESIG, "");
        SPUtils.putString(ADDRESS, "");
        SPUtils.putInt(TOTALJIFEN, 0);
        //SPUtils.putString(MOBILE, "");
        //SPUtils.putString(PASSWORD, "");
        SPUtils.putBoolean(ISVST,false);
        SPUtils.putBoolean(IS_BINDWX,false);
        SPUtils.putString(H5_URL,"");
        SPUtils.putInt(BADGER_NUM,0);
        SPUtils.putBoolean(FIRST_MESSAGE,false);
    }


    /**
     * 清空SharedPreferences实例对象
     *
     */
//    public static void clear() {
//        SharedPreferences userSettings = DemoApplication.getInstance().getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = userSettings.edit();
//        editor.clear();
//        editor.commit();
//    }

    /**
     * 获取SharedPreferences实例对象
     *
     * @param fileName
     */
    private static SharedPreferences getSharedPreference(String fileName) {
        return DemoApplication.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 保存一个String类型的值！
     */
    public static void putString(String key, String value) {
        if(key.equals("choseCity")){
            Log.i("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCHOSE CITY=" + value);
        }
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.putString(key, value).apply();
    }

    /**
     * 保存一个String类型的值！
     */
    public static void putAppString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG_APP).edit();
        editor.putString(key, value).apply();
    }

    /**
     * 获取String的value
     */
    public static String getString(String key, String defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getString(key, defValue);
    }

    /**
     * 获取String的value
     */
    public static String getString(String key) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getString(key, "");
    }

    /**
     * 保存一个Boolean类型的值！
     */
    public static void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.putBoolean(key, value).apply();
    }

    /**
     * 获取boolean的value
     */
    public static boolean getBoolean(String key, Boolean defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getBoolean(key, defValue);
    }

    /**
     * 保存一个int类型的值！
     */
    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.putInt(key, value).apply();
    }

    /**
     * 获取int的value
     */
    public static int getInt(String key, int defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getInt(key, defValue);
    }

    /**
     * 保存一个float类型的值！
     */
    public static void putFloat(String fileName, String key, float value) {
        SharedPreferences.Editor editor = getSharedPreference(fileName).edit();
        editor.putFloat(key, value).apply();
    }

    /**
     * 获取float的value
     */
    public static float getFloat(String key, Float defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getFloat(key, defValue);
    }

    /**
     * 保存一个long类型的值！
     */
    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.putLong(key, value).apply();
    }

    /**
     * 获取long的value
     */
    public static long getLong(String key, long defValue) {
        SharedPreferences sharedPreference = getSharedPreference(CONFIG);
        return sharedPreference.getLong(key, defValue);
    }

    /**
     * 取出List<String>
     *
     * @param key     List<String> 对应的key
     * @return List<String>
     */
    public static List<String> getStrListValue(String key) {
        List<String> strList = new ArrayList<String>();
        int size = getInt(key + "size", 0);
        //Log.d("sp", "" + size);
        for (int i = 0; i < size; i++) {
            strList.add(getString(key + i, null));
        }
        return strList;
    }

    /**
     * 存储List<String>
     *
     * @param key     List<String>对应的key
     * @param strList 对应需要存储的List<String>
     */
    public static void putStrListValue(String key,
                                       List<String> strList) {
        if (null == strList) {
            return;
        }
        // 保存之前先清理已经存在的数据，保证数据的唯一性
        removeStrList(key);
        int size = strList.size();
        putInt(key + "size", size);
        for (int i = 0; i < size; i++) {
            putString(key + i, strList.get(i));
        }
    }

    /**
     * 清空List<String>所有数据
     *
     * @param key     List<String>对应的key
     */
    public static void removeStrList(String key) {
        int size = getInt(key + "size", 0);
        if (0 == size) {
            return;
        }
        remove(key + "size");
        for (int i = 0; i < size; i++) {
            remove(key + i);
        }
    }

    /**
     * 清空对应key数据
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = getSharedPreference(CONFIG).edit();
        editor.remove(key).apply();
    }

    /**
     * 保存对应key数据
     */
//    public static void setUserBean(UserBean userBean) {
//        SPUtils.putString(UB_ID, userBean.getUb_id());
//        SPUtils.putString(NICK_NAME, userBean.getNickname());
//        SPUtils.putString(HEAD_PIC, userBean.getHeadpic());
//        SPUtils.putInt(SEX, userBean.getSex());
//        SPUtils.putString(UA_ID, userBean.getUa_id());
//        SPUtils.putString(REAL_NAME, userBean.getRealname());
//        SPUtils.putString(ID_CARD, userBean.getIdcard());
//        SPUtils.putString(EMAIL, userBean.getEmail());
//        SPUtils.putString(STYLESIG, userBean.getStylesig());
//        SPUtils.putString(ADDRESS, userBean.getAddress());
//        SPUtils.putInt(TOTALJIFEN, userBean.getTotaljifen());
//        SPUtils.putString(MOBILE, userBean.getMobile());
//        SPUtils.putString(PASSWORD, userBean.getPassword());
//    }

    /**
     * 设置版本数据
     */
    public static void setVersionBean(VersionBean userBean) {
        SPUtils.putString("id",userBean.getId());
        SPUtils.putString("isNew",userBean.getIsNew());
        SPUtils.putString("srevePhone",userBean.getSrevePhone());
        SPUtils.putString("vAddr",userBean.getvAddr());
        SPUtils.putString("vContent",userBean.getvContent());
        SPUtils.putString("vName",userBean.getvName());
        SPUtils.putString("vNumber",userBean.getvNumber());
        SPUtils.putString("vState",userBean.getvState());
        SPUtils.putString("vTime",userBean.getvTime());
        SPUtils.putString("vType",userBean.getvType());
        SPUtils.putString("vVersionCode",userBean.getvVersionCode());
    }

}
