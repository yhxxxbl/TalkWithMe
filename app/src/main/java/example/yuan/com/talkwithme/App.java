package example.yuan.com.talkwithme;

import com.igexin.sdk.PushManager;

import example.yuan.com.common.app.Application;
import example.yuan.com.factory.Factory;

/**
 * Created by Administrator on 2018/7/4/004.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Factory.setup();//调用Factory进行初始化
        PushManager.getInstance().initialize(this);//推送进行初始化
    }
}
