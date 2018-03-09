package cn.tthud.taitian.wxapi;

import cn.tthud.taitian.net.rxbus.RxBus;
import cn.tthud.taitian.net.rxbus.RxBusBaseMessage;
import cn.tthud.taitian.net.rxbus.RxCodeConstants;
import cn.tthud.taitian.utils.Constants;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
		api.handleIntent(getIntent(), this);
    }
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}
	@Override
	public void onReq(BaseReq req) {
	}
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		//有时候支付结果还需要发送给服务器确认支付状态
		if (resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
			if (resp.errCode==0){
				Toast.makeText(this,"支付成功",Toast.LENGTH_LONG).show();
				RxBus.getDefault().post(RxCodeConstants.MessageFragment_PAY, new RxBusBaseMessage(1,"支付成功"));
			}else if (resp.errCode==-2){
				Toast.makeText(this,"取消支付",Toast.LENGTH_LONG).show();
				RxBus.getDefault().post(RxCodeConstants.MessageFragment_PAY, new RxBusBaseMessage(1,"取消支付"));
			}else {
				Toast.makeText(this,"支付失败",Toast.LENGTH_LONG).show();
				RxBus.getDefault().post(RxCodeConstants.MessageFragment_PAY, new RxBusBaseMessage(1,"支付失败"));
			}
			finish();
		}
	}
}