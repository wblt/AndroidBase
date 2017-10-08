package cn.tthud.taitian.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
	/**
	 * 读取文本数据
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名
	 * @return String, 读取到的文本内容，失败返回null
	 */
	public static String readAssets(Context context, String fileName) {
		InputStream is = null;
		String content = null;
		try {
			is = context.getAssets().open(fileName);
			if (is != null) {
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true) {
					int readLength = is.read(buffer);
					if (readLength == -1)
						break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				is.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			content = null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return content;
	}

	// 判断该文件是否存在
	public static boolean isExist(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 初始化SD卡视频设备抓拍路径
	public static String initSDCardDirBySnapShot(Context ctx) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(ctx)
				.getPath() : ctx.getCacheDir().getPath();
		String account = SharepreferenceUtil.readString(ctx,
				SharepreferenceUtil.fileName, "account");
		File PHOTO_DIR = new File(cachePath + File.separator + account
				+ File.separator + "SnapShot");
		PHOTO_DIR.mkdirs();
		String savePath = PHOTO_DIR.getAbsolutePath();
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}

	// 初始化SD卡视频设备录像路径
	public static String initSDCardDirByRecordVideo(Context ctx) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(ctx)
				.getPath() : ctx.getCacheDir().getPath();
		String account = SharepreferenceUtil.readString(ctx,
				SharepreferenceUtil.fileName, "account");
		File PHOTO_DIR = new File(cachePath + File.separator + account
				+ File.separator + "RecordVideo");
		PHOTO_DIR.mkdirs();
		String savePath = PHOTO_DIR.getAbsolutePath();
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return savePath;
	}

	/**
	 * 获取程序外部的缓存目录
	 * 
	 * @param context
	 * @return
	 */
	public static File getExternalCacheDir(Context context) {
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	// 录像视频保存
	public static boolean writeFile(Context context, String fileName,
                                    byte[] data, int count) {
		if (count <= 0 || data == null) {
			return true;
		}
		String path = initSDCardDirByRecordVideo(context) + fileName;
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data, 0, count);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i("写入文件失败！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("创建文件失败！");
			return false;
		}
		return true;
	}


	/**
	 *  获取相册下的图片文件
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static File getImageFile(Context context, String fileName) {
		File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+fileName);
		if (!tempFile.exists()){
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tempFile;
	}

	/**
	 * 随机获取图片文件名
	 * @return
	 */
	public static String getImagFileName(){
		return getFileName(".jpg");
	}

	/**
	 * 随机获取文件名(没有后缀)
	 * @return
	 */
	public static String getFileNameNoEndwith(){
		return  getFileName("");
	}
	/**
	 *  随机获取文件名称
	 * @return
	 */
	public static String getFileName(String endwith){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String ranStr = StringUtil.getRandomString(6);
		String filename =sdf.format(new Date())+"_"+ranStr+endwith;
		return filename;
	}
	/**
	 * 获取log日志保存路径
	 * @return
	 */
	public static String getLogSavePath(){
		File xutilFile=org.xutils.common.util.FileUtil.getCacheDir("log");
		if(xutilFile!=null){
			return xutilFile.getPath();
		}
		return getFileSavePath();
	}

	/**
	 * 数据库保存路径
	 * @return
	 */
	public static String getDBSavePath(){
		File xutilFile=org.xutils.common.util.FileUtil.getCacheDir("db");
		if(xutilFile!=null){
			return xutilFile.getPath();
		}
		return getFileSavePath();
	}

	/**
	 * 文件下载路径
	 * @return
	 */
	public static String getDownLoadPath(){
		File xutilFile=org.xutils.common.util.FileUtil.getCacheDir("download");
		if(xutilFile!=null){
			String path=xutilFile.getPath();
			return path;
		}
		return getFileSavePath();
	}
	/**
	 * 获取文件下载保存地址
	 * @return
	 */
	private static String getFileSavePath(){
		String sdPath=getSavePath();
		if(!TextUtils.isEmpty(sdPath)){
			String pkg= x.app().getPackageName();
			pkg=pkg.substring(pkg.lastIndexOf(".")+1);
			String path=sdPath+ File.separator+"wenshifile"+ File.separator+pkg+ File.separator;
			File file = new File(path);
			if (!file.exists())
				file.mkdir();
			return path;
		}else {
			return x.app().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
		}
	}
	/**
	 * 获取文件下载保存地址
	 * @return
	 */
	private static String getSavePath(){
		String path="";
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		File sd2File = new File("/mnt/sdcard2");
		File emmcFile = new File("/mnt/emmc");
		if(sdCardExist){
			//外部存储卡
			return Environment.getExternalStorageDirectory().getPath();
		}else if(sd2File.exists()){
			//SD卡2
			return sd2File.getPath();
		}else if(emmcFile.exists()){
			//内部存储卡
			return emmcFile.getPath();
		}
		return  path;
	}

	/**
	 * 根据文件获得对应的MIME类型。
	 * @param file
	 */
	public static String getMIMEType(File file) {
		String type="*/*";
		String fName = file.getName();
		//获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if(dotIndex < 0){
			return type;
		}
        /* 获取文件的后缀名*/
		String end=fName.substring(dotIndex,fName.length()).toLowerCase();
		if(end=="")return type;
		//在MIME和文件类型的匹配表中找到对应的MIME类型。
		for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
			if(end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	/**
	 * 匹配
	 * @param fileName
	 * @return
	 */
	public static boolean isMIMEType(String fileName){
		boolean isMIMEType=false;
		int dotIndex = fileName.lastIndexOf(".");
		if(dotIndex < 0){
			return false;
		}
		String end=fileName.substring(dotIndex, fileName.length()).toLowerCase();
		for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
			if(end.equals(MIME_MapTable[i][0]))
				isMIMEType=true;
		}
		return isMIMEType;
	}
	public static final String[][] MIME_MapTable={
			//{后缀名，MIME类型}
//            {".3gp",    "video/3gpp"},
//            {".apk",    "application/vnd.android.package-archive"},
//            {".asf",    "video/x-ms-asf"},
//            {".avi",    "video/x-msvideo"},
//            {".bin",    "application/octet-stream"},
//            {".bmp",    "image/bmp"},
//            {".c",  "text/plain"},
//            {".class",  "application/octet-stream"},
//            {".conf",   "text/plain"},
//            {".cpp",    "text/plain"},
			{".doc",    "application/msword"},
			{".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
			{".xls",    "application/vnd.ms-excel"},
			{".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
//            {".exe",    "application/octet-stream"},
//            {".gif",    "image/gif"},
//            {".gtar",   "application/x-gtar"},
//            {".gz", "application/x-gzip"},
//            {".h",  "text/plain"},
//            {".htm",    "text/html"},
//            {".html",   "text/html"},
//            {".jar",    "application/java-archive"},
//            {".java",   "text/plain"},
//            {".jpeg",   "image/jpeg"},
//            {".jpg",    "image/jpeg"},
//            {".js", "application/x-javascript"},
//            {".log",    "text/plain"},
//            {".m3u",    "audio/x-mpegurl"},
//            {".m4a",    "audio/mp4a-latm"},
//            {".m4b",    "audio/mp4a-latm"},
//            {".m4p",    "audio/mp4a-latm"},
//            {".m4u",    "video/vnd.mpegurl"},
//            {".m4v",    "video/x-m4v"},
//            {".mov",    "video/quicktime"},
//            {".mp2",    "audio/x-mpeg"},
//            {".mp3",    "audio/x-mpeg"},
//            {".mp4",    "video/mp4"},
//            {".mpc",    "application/vnd.mpohun.certificate"},
//            {".mpe",    "video/mpeg"},
//            {".mpeg",   "video/mpeg"},
//            {".mpg",    "video/mpeg"},
//            {".mpg4",   "video/mp4"},
//            {".mpga",   "audio/mpeg"},
//            {".msg",    "application/vnd.ms-outlook"},
//            {".ogg",    "audio/ogg"},
//            {".pdf",    "application/pdf"},
//            {".png",    "image/png"},
//            {".pps",    "application/vnd.ms-powerpoint"},
//            {".ppt",    "application/vnd.ms-powerpoint"},
//            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
//            {".prop",   "text/plain"},
//            {".rc", "text/plain"},
//            {".rmvb",   "audio/x-pn-realaudio"},
//            {".rtf",    "application/rtf"},
//            {".sh", "text/plain"},
//            {".tar",    "application/x-tar"},
//            {".tgz",    "application/x-compressed"},
//            {".txt",    "text/plain"},
//            {".wav",    "audio/x-wav"},
//            {".wma",    "audio/x-ms-wma"},
//            {".wmv",    "audio/x-ms-wmv"},
//            {".wps",    "application/vnd.ms-works"},
//            {".xml",    "text/plain"},
//            {".z",  "application/x-compress"},
			{".zip",    "application/x-zip-compressed"},
			{".rar",    "application/x-zip-compressed"},
//            {"",        "*/*"}
	};

}
