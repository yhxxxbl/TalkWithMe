package example.yuan.com.factory.presenter.account;

import example.yuan.com.factory.presenter.BaseContract;

/**
 * Created by Administrator on 2018/7/8/008.
 */

public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{
        //登陆成功
        void registerSuccess();

    }
    interface Presenter extends BaseContract.Presenter{
        //发起登陆
        void login(String phone,String password,String name);
    }
}
