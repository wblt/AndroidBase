package cn.tthud.taitian.net;

import android.os.Environment;

import org.xutils.http.RequestParams;

/**
 * Created by wenshi on 2016/4/22.
 */
public class FlowAPI {
    //图片本地
    public static String YYW_FILE_PATH = Environment.getExternalStorageDirectory()
            + "/com.waxin.waxin/";
    public static String SERVER_IP = "http://139.196.188.28:9002/zbManage_intf_test/";
//    public static String SERVER_IP = "http://wx.maishenke.com/";

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
        public static final String SUCCEED="01";
        // 000001:参数为空,
        // 000002:用户不存在,
        // 000003,安全码验证失败,
        // 999999:系统错误,
        // 000000:成功
    }
    public static final String RESULT_OK="01";
}
