/**
 * 
 */
package cn.tthud.taitian.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;


import java.util.ArrayList;
import java.util.List;

import cn.tthud.taitian.R;
import cn.tthud.taitian.net.rxbus.RxBus;
import cn.tthud.taitian.net.rxbus.RxBusBaseMessage;
import cn.tthud.taitian.net.rxbus.RxCodeConstants;
import cn.tthud.taitian.utils.Log;
import cn.tthud.taitian.utils.NetUtils;

/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 * 
 * @创建时间：2016年5月27日 下午3:app_loading01:31
 * @项目名称： AMapLocationDemo
 * @author hongming.wang
 * @文件名称：PermissionsChecker.java
 * @类型名称：PermissionsChecker
 * @since 2.5.0
 */
public class CheckPermissionsActivity extends BaseActivity
		implements ActivityCompat.OnRequestPermissionsResultCallback {
	/**
	 * 需要进行检测的权限数组
	 */
	public String[] needPermissions = {
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.CALL_PHONE,
			Manifest.permission.CAMERA
			};
	
	public static final int PERMISSON_REQUESTCODE = 0;
	
	/**
	 * 判断是否需要检测，防止不停的弹框
	 */
	private boolean isNeedCheck = true;
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!NetUtils.isConnected(this)) {
			return;
		}
		Log.i("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP111");
		if(isNeedCheck){
			Log.i("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP222");
			checkPermissions(needPermissions);
		}
	}
	
	/**
	 * 
	 * @since 2.5.0
	 *
	 */
	public void checkPermissions(String... permissions) {
		List<String> needRequestPermissonList = findDeniedPermissions(permissions);
		if (null != needRequestPermissonList
				&& needRequestPermissonList.size() > 0) {
			ActivityCompat.requestPermissions(this,
					needRequestPermissonList.toArray(
							new String[needRequestPermissonList.size()]),
					PERMISSON_REQUESTCODE);
			Log.i("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP333");
		}else {
			isNeedCheck = false;
			Log.i("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP444");
			RxBus.getDefault().post(RxCodeConstants.APP_GET_PERMISSION, new RxBusBaseMessage(1));
		}
	}

	/**
	 * 获取权限集中需要申请权限的列表
	 * 
	 * @param permissions
	 * @return
	 * @since 2.5.0
	 *
	 */
	private List<String> findDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissonList = new ArrayList<String>();
		for (String perm : permissions) {
			if (ContextCompat.checkSelfPermission(this,
					perm) != PackageManager.PERMISSION_GRANTED
					|| ActivityCompat.shouldShowRequestPermissionRationale(
							this, perm)) {
				needRequestPermissonList.add(perm);
			} 
		}
		return needRequestPermissonList;
	}

	/**
	 * 检测是否说有的权限都已经授权
	 * @param grantResults
	 * @return
	 * @since 2.5.0
	 *
	 */
	private boolean verifyPermissions(int[] grantResults) {
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
		if (requestCode == PERMISSON_REQUESTCODE) {
			Log.i("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP555");
			if (!verifyPermissions(paramArrayOfInt)) {
				showMissingPermissionDialog();
				isNeedCheck = false;
				Log.i("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP666");
			}
			Log.i("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP777");
		}
	}

	/**
	 * 显示提示信息
	 * 
	 * @since 2.5.0
	 *
	 */
	private void showMissingPermissionDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setMessage("需要赋予通话的权限，不开启将无法正常工作！")
		.setPositiveButton("设置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
				startActivity(intent);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).create();
		dialog.show();
	}

	/**
	 *  启动应用的设置
	 * 
	 * @since 2.5.0
	 *
	 */
	private void startAppSettings() {
		Intent intent = new Intent(
				Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean isNeedCheck() {
		return isNeedCheck;
	}

	public void setNeedCheck(boolean needCheck) {
		isNeedCheck = needCheck;
	}
}
