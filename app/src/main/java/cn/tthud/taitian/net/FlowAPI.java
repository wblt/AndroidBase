package cn.tthud.taitian.net;

import android.os.Environment;

import org.xutils.http.RequestParams;

/**
 * Created by wenshi on 2016/4/22.
 */
public class FlowAPI {
    //图片本地
    public static String YYW_FILE_PATH = Environment.getExternalStorageDirectory()
            + "/cn.tthud.taitian/";
//    public static String SERVER_IP = "http://139.196.188.28:9002/zbManage_intf_test/";

    public static String SERVER_IP = "https://res.hnltou.com/api.php";

    /** 最大图片数 */
    public static final int DIARY_IMGS_MAX = 9;
    /** 最大图片数 */
    public static final int DIARY_IMGS_MAX_QITA = 20;
    /**新加的图片ID*/
    public static final int NEW_IMG_ID = 0X0022;
    /**图片地址*/
    public static final String IMG_PATH = SERVER_IP + "/";

    public static RequestParams getRequestParams(String uri){
        RequestParams requestParams=new RequestParams(uri);
        requestParams.setConnectTimeout(150000);
        return requestParams;
    }

    /**
     * 服务器返回
     */
    public class HttpResultCode{
        public static final String SUCCEED="1";
        // 000001:参数为空,
        // 000002:用户不存在,
        // 000003,安全码验证失败,
        // 999999:系统错误,
        // 000000:成功
    }
    public static final String RESULT_OK="01";

    //个人主页中心
    public static String PERSONAL_CENTER  = SERVER_IP + "/appIndex";
    // 注册
    public static String PERSONAL_REGISTER = SERVER_IP + "/appReg";
    // 获取验证码
    public static String REGISTER_SEND_CODE = SERVER_IP + "/appMsg";
    // 登录
    public static String PERSONAL_LOGIN = SERVER_IP + "/appLogin";

    // 修改个人头像
    public static String PERSONAL_UPDATE_HEDER = SERVER_IP + "/appHeadpicSbt";

    // 修改手机号码
    public static String PERSONAL_CHANGE_TEL = SERVER_IP + "/appTelSbt";

    // 获取个人信息
    public static String PERSONAL_INFO = SERVER_IP + "/appInfo";
    // 修改个人资料
    public static String PERSONAL_CHANGE_INFO = SERVER_IP + "/appInfoSbt";

    // 微信登录
    public static String PERSONAL_WX_LOGIN = SERVER_IP + "/appWxLogin";

    // 活动列表
    public static String APP_ACTIVITY_LIST = SERVER_IP + "/appActList";
    // 首页
    public static String APP_HOME_LIST = SERVER_IP + "/appIndexlist";
    // 公司详情
    public static String APP_COMPANY_DETAIL = SERVER_IP + "/appComDetail";
    // 公司列表
    public static String APP_COMPANY_LIST = SERVER_IP + "/appComMore";

}
