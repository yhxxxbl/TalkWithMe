package example.yuan.com.factory.presenter.account;

import android.drm.DrmStore;
import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

import javax.security.auth.callback.Callback;

import example.yuan.com.Common;
import example.yuan.com.factory.R;
import example.yuan.com.factory.data.DataSource;
import example.yuan.com.factory.data.helper.AccountHelper;
import example.yuan.com.factory.model.api.account.RegisterModel;
import example.yuan.com.factory.model.db.User;
import example.yuan.com.factory.presenter.BasePresenter;

/**
 * Created by Administrator on 2018/7/8/008.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter,DataSource.CallBack<User> {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String password, String name) {
        //调用开始方法，
        start();
        //得到View接口
        RegisterContract.View view=getmView();

        //校验
        if (!checkMobie(phone)){
            //如果手机号不合法，提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if (password.length()<5){
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else if (name.length()<2){
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else {
            //成功，进行网络请求
            //构造Model，进行请求调用
            RegisterModel model=new RegisterModel(phone,password,name);
            //进行网络请求，并设置会送接口为自己：上面实现了DataSource.CallBack<User>接口
            AccountHelper.register(model,this);
        }
    }

    /**
     * 检查手机号是否合法
     * @param phone
     * @return 合法为true
     */
    @Override
    public boolean checkMobie(String phone) {
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBLIE,phone);
    }

    @Override
    public void onDataLoaded(User user) {
        //但网络请求成功，注册好了回送一个用户信息
        //仅告知界面注册成功即可
        final RegisterContract.View view=getmView();
        if (view==null)
            return;
        //此时是从网络回送回来的，并不保证处于主线程状态
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        //告知网络请求失败
        final RegisterContract.View view=getmView();
        if (view==null)
            return;
        //此时是从网络回送回来的，并不保证处于主线程状态
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //显示错误
                view.showError(strRes);
            }
        });
    }
}
