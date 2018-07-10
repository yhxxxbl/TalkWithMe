package example.yuan.com.factory.presenter.account;

import example.yuan.com.factory.presenter.BaseContract;

/**
 * Created by Administrator on 2018/7/8/008.
 */

public interface RegisterContract {
    interface View extends  BaseContract.View<Presenter>{
        //注册成功
        void registerSuccess();
    }
    interface Presenter extends BaseContract.Presenter{
        //发起注册
        void register(String phone,String password,String name);

        boolean checkMobie(String phone);

    }
}
