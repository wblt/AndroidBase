package cn.tthud.taitian.utils;

/**
 * 日志工具类
 * 
 * @author
 * 
 */
public class Log {

	private static final String TAG = "cn.tthud.taitian";

	/** 是否打开Log日志输出 ，true打开，false关闭 **/
	public static boolean logOn = true;

	/**** 5种Log日志类型 *******/
	/** 信息日志类型 **/
	public static final int VERBOSE = 101;
	/** 调试日志类型 **/
	public static final int DEBUG = 102;
	/** 详细信息日志类型 **/
	public static final int INFO = 103;
	/** 警告调试日志类型 **/
	public static final int WARN = 104;
	/** 错误日志类型 **/
	public static final int ERROR = 105;
	/**
	 * 打印日志等级LEVER，当LEVER赋值为（VERBOSE|DEBUG|INFO|WARN|ERROR）时，相应等级以及以下的日志不打印。
	 * 打印全部日志赋值0。 等级排序：VERBOSE < DEBUG < INFO < WARN < ERROR
	 **/
	public static final int LEVER = DEBUG;

	public static void showLog(String Message, int Style) {
		switch (Style) {
		case VERBOSE:
			v(TAG, Message);
			break;
		case DEBUG:
			d(TAG, Message);
			break;
		case INFO:
			i(TAG, Message);
			break;
		case WARN:
			w(TAG, Message);
			break;
		case ERROR:
			e(TAG, Message);
			break;
		}
	}

	public static void showLog(String Message) {
		showLog(Message, INFO);
	}

	public static boolean isLogOn() {
		return logOn;
	}

	public static void closeLog() {
		logOn = false;
	}

	public static void openLog() {
		logOn = true;
	}

	public static void v(String Message) {
		v(TAG, Message);
	}

	public static void d(String Message) {
		d(TAG, Message);
	}

	public static void i(String Message) {
		i(TAG, Message);
	}

	public static void w(String Message) {
		w(TAG, Message);
	}

	public static void e(String Message) {
		e(TAG, Message);
	}

	public static void v(String Tag, String Message) {
		if (logOn && (VERBOSE > LEVER)) {
			android.util.Log.v(Tag, Message);
		}
	}

	public static void d(String Tag, String Message) {
		if (logOn && (DEBUG > LEVER)) {
			android.util.Log.d(Tag, Message);
		}
	}

	public static void i(String Tag, String Message) {
		if (logOn && (INFO > LEVER)) {
			android.util.Log.i(Tag, Message);
		}
	}

	public static void w(String Tag, String Message) {
		if (logOn && (WARN > LEVER)) {
			android.util.Log.w(Tag, Message);
		}
	}

	public static void e(String Tag, String Message) {
		if (logOn && (ERROR > LEVER)) {
			android.util.Log.e(Tag, Message);
		}
	}

	public static void w(String Tag, Throwable tr) {
		if (logOn && (WARN > LEVER)) {
			android.util.Log.w(Tag, tr);
		}
	}

	public static void v(String Tag, String Message, Throwable tr) {
		if (logOn && (VERBOSE > LEVER)) {
			android.util.Log.v(Tag, Message, tr);
		}
	}

	public static void d(String Tag, String Message, Throwable tr) {
		if (logOn && (DEBUG > LEVER)) {
			android.util.Log.d(Tag, Message, tr);
		}
	}

	public static void i(String Tag, String Message, Throwable tr) {
		if (logOn && (INFO > LEVER)) {
			android.util.Log.i(Tag, Message, tr);
		}
	}

	public static void w(String Tag, String Message, Throwable tr) {
		if (logOn && (WARN > LEVER)) {
			android.util.Log.w(Tag, Message, tr);
		}
	}

	public static void e(String Tag, String Message, Throwable tr) {
		if (logOn && (ERROR > LEVER)) {
			android.util.Log.e(Tag, Message, tr);
		}
	}

}
