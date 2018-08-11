package example.yuan.com.talkwithme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

import example.yuan.com.factory.Factory;
import example.yuan.com.factory.data.helper.AccountHelper;
import example.yuan.com.factory.persistence.Account;

/**
 * 消息接收器-个推
 */

public class MessageReceiver extends BroadcastReceiver{
    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;

        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                Log.i(TAG, "GET_CLIENTID:" + bundle.toString());
                // 当Id初始化的时获取设备ID
                onClientInit(bundle.getString("clientid"));
                break;
            case PushConsts.GET_MSG_DATA: {
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String message = new String(payload);
                    Log.i(TAG, "GET_MSG_DATA:" + message);
                    onMessageArrived(message);
                }
                break;
            }
            default:
                Log.i(TAG, "OTHER:" + bundle.toString());
                break;
        }
    }
    /**
     * ID初始化时
     * @param cid 设备ID
     */
    private void onClientInit(String cid){
        //设置设备ID
        Account.setPushId(cid);
        if (Account.isLogin()){
            //判断账户的登录状态，进行一次pushID的绑定
            AccountHelper.bindpush(null);
        }
    }

    /**
     * 消息送达时
     * @param message 新消息
     */
    private void onMessageArrived(String message){
        //交给Factory处理
        Factory.dispatchPush(message);
    }
}
