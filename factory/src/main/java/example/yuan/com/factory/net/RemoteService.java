package example.yuan.com.factory.net;

import example.yuan.com.factory.model.api.RspModel;
import example.yuan.com.factory.model.api.account.AccountRspModel;
import example.yuan.com.factory.model.api.account.LoginModel;
import example.yuan.com.factory.model.api.account.RegisterModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 网络请求的所有接口
 */

public interface RemoteService {
    /**
     * 网络请求：一个注册接口
     * @param model 传入的是RegisterModel
     * @return <AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 网络请求： 登陆接口
     * @param model  loginModel
     * @return   RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountlogin(@Body LoginModel model);

    /**
     * 绑定设备ID
     * @param pushId 设备ID
     * @return  RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true,value = "pushId") String pushId);
}
