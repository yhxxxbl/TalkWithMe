package example.yuan.com.factory.data.helper;
;
import example.yuan.com.factory.Factory;
import example.yuan.com.factory.R;
import example.yuan.com.factory.data.DataSource;
import example.yuan.com.factory.model.api.RspModel;
import example.yuan.com.factory.model.api.account.AccountRspModel;
import example.yuan.com.factory.model.api.account.RegisterModel;
import example.yuan.com.factory.model.db.User;
import example.yuan.com.factory.net.NetWork;
import example.yuan.com.factory.net.RemoteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/7/9/009.
 */

public class AccountHelper {
    /**
     * 注册的接口
     * @param model 传递一个注册的model
     * @param callBack  成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.CallBack<User> callBack){
        //调用retrofit对网络请求接口做代理
        RemoteService service= NetWork.getRetrofit().create(RemoteService.class);
        //得到一个Call
        Call<RspModel<AccountRspModel>> call= service.accountRegister(model);
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call,
                                   Response<RspModel<AccountRspModel>> response) {
                //网络请求成功
                //从返回中得到我们的全局Model，内部使用Gson进行解析
              RspModel<AccountRspModel> rspModel=response.body();
              if (rspModel.success()) {
                  //拿到实体
                  AccountRspModel accountRspModel = rspModel.getResult();
                  if (accountRspModel.isBind()) {
                      User user = accountRspModel.getUser();
                      //TODO 进行的是数据库写入和缓存绑定
                      //然后返回
                      callBack.onDataLoaded(user);
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
                callBack.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    public static void bindpush(final DataSource.CallBack<User> callBack){
    callBack.onDataNotAvailable(R.string.app_name);
    }
}
