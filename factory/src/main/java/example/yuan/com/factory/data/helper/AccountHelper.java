package example.yuan.com.factory.data.helper;
;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.config.FlowManager;

import example.yuan.com.factory.Factory;
import example.yuan.com.factory.R;
import example.yuan.com.factory.data.DataSource;
import example.yuan.com.factory.model.api.RspModel;
import example.yuan.com.factory.model.api.account.AccountRspModel;
import example.yuan.com.factory.model.api.account.LoginModel;
import example.yuan.com.factory.model.api.account.RegisterModel;
import example.yuan.com.factory.model.db.User;
import example.yuan.com.factory.net.NetWork;
import example.yuan.com.factory.net.RemoteService;
import example.yuan.com.factory.persistence.Account;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountHelper {
    /**
     * 注册的接口
     * @param model 传递一个注册的model
     * @param callBack  成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.CallBack<User> callBack){
        //调用retrofit对网络请求接口做代理
        RemoteService service= NetWork.remoteService();
        //得到一个Call
        Call<RspModel<AccountRspModel>> call= service.accountRegister(model);
        call.enqueue(new AccountRspCallback(callBack));
    }

    /**
     * 登陆的调用
     * @param model  登陆的Model
     * @param callBack 成功与失败的接口返回推送
     */
    public static void login(final LoginModel model, final DataSource.CallBack<User> callBack){
        //调用retrofit对网络请求接口做代理
        RemoteService service= NetWork.remoteService();
        //得到一个Call
        Call<RspModel<AccountRspModel>> call= service.accountlogin(model);
        call.enqueue(new AccountRspCallback(callBack));
    }

    public static void bindpush(final DataSource.CallBack<User> callBack){
                String pushId=Account.getPushId();
                if (TextUtils.isEmpty(pushId))//如果为空直接return
                    return;
                //调用Retrofit为网络请求做代理
                RemoteService remoteService=NetWork.remoteService();
                Call<RspModel<AccountRspModel>> call= remoteService.accountBind(pushId);
                call.enqueue(new AccountRspCallback(callBack));
    }




    /**
     * 请求的回调的封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>>{
        final DataSource.CallBack<User> callBack;
        AccountRspCallback(DataSource.CallBack<User> callBack){
            this.callBack=callBack;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call,
                               Response<RspModel<AccountRspModel>> response) {
            //网络请求成功
            //从返回中得到我们的全局Model，内部使用Gson进行解析
            RspModel<AccountRspModel> rspModel=response.body();
            if (rspModel.success()) {
                //拿到实体
                AccountRspModel accountRspModel = rspModel.getResult();
                //获取我的信息
                User user = accountRspModel.getUser();
                user.save();//直接保存
                      /*
                      通过ModelAdapter
                      FlowManager.getModelAdapter(User.class)
                              .save(user);*/
                Account.login(accountRspModel);//将自己的信息同步到持久化中

                if (accountRspModel.isBind()) {
                    if (callBack!=null)
                    callBack.onDataLoaded(user);  //返回
                } else {
                    //进行绑定的唤起
                    bindpush(callBack);
                }
            }else {
                Factory.decodeRspCode(rspModel,callBack);
                //callBack.onDataNotAvailable(R.string.data_network_error);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            //网络请求失败
            if (callBack!=null)
            callBack.onDataNotAvailable(R.string.data_network_error);
        }
    }
}
