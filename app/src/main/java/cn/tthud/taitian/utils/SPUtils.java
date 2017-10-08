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

    /**
     * 清空SharedPreferences实例对象
     *
     */
    public static void clearUser() {
        SPUtils.putString("userId","");
        SPUtils.putString("nickName","");
        SPUtils.putString("sex","");
        SPUtils.putString("mobile","");
        SPUtils.putString("nowCity","");
        SPUtils.putString("liveVeinNum","");
        SPUtils.putString("headUrl","");
        SPUtils.putString("goldNum","");
        SPUtils.putString("promotionNum","");
        SPUtils.putString("token","");
        SPUtils.putString("incomeTotalMoney","");
        SPUtils.putString("withdrawalTotalMoney","");
        SPUtils.putString("state","");
        SPUtils.putString("vipLevel","");
        SPUtils.putString("vipStart","");
        SPUtils.putString("vipEnd","");
        SPUtils.putString("accountMoney","");
        SPUtils.putString("effect","");
        SPUtils.putString("rechargeTotalMoney","");
        SPUtils.putString("promotionPeopleNum","");
        SPUtils.putString("dreamMentor","");
    }


    /**
     * 清空SharedPreferences实例对象
     *
     */
    public static void clear() {
        SharedPreferences userSettings = DemoApplication.getInstance().getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.clear();
        editor.commit();
    }

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
     * 清空对应key数据
     */
    public static void setUserBean(UserBean userBean) {
        SPUtils.putString("userId",userBean.getUserId());
        SPUtils.putString("nickName",userBean.getNickName());
        SPUtils.putString("sex",userBean.getSex());
        SPUtils.putString("mobile",userBean.getMobile());
        SPUtils.putString("nowCity",userBean.getNowCity());
        SPUtils.putString("liveVeinNum",userBean.getLiveVeinNum());
        SPUtils.putString("headUrl",userBean.getHeadUrl());
        SPUtils.putString("goldNum",userBean.getGoldNum());
        SPUtils.putString("promotionNum",userBean.getPromotionNum());
        SPUtils.putString("individualSign",userBean.getIndividualSign());
        SPUtils.putString("token",userBean.getToken());
        SPUtils.putString("incomeTotalMoney",userBean.getIncomeTotalMoney());
        SPUtils.putString("withdrawalTotalMoney",userBean.getWithdrawalTotalMoney());
        SPUtils.putString("state",userBean.getState());
        SPUtils.putString("vipLevel",userBean.getVipLevel());
        SPUtils.putString("vipStart",userBean.getVipStart());
        SPUtils.putString("vipEnd",userBean.getVipEnd());
        SPUtils.putString("accountMoney",userBean.getAccountMoney());
        SPUtils.putString("effect",userBean.getEffect());
        SPUtils.putString("rechargeTotalMoney",userBean.getRechargeTotalMoney());
        SPUtils.putString("promotionPeopleNum",userBean.getPromotionNum());
        SPUtils.putString("dreamMentor",userBean.getDreamMentor());
    }

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
