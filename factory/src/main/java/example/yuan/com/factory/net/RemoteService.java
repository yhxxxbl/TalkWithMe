package example.yuan.com.factory.net;

import example.yuan.com.factory.model.api.RspModel;
import example.yuan.com.factory.model.api.account.AccountRspModel;
import example.yuan.com.factory.model.api.account.RegisterModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网络请求的所有接口
 */

public interface RemoteService {
    /**
     * 网络请求一个注册接口
     * @param model 传入的是RegisterModel
     * @return <AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);
}
