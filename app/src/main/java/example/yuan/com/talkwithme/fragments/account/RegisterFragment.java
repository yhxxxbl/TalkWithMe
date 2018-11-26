package example.yuan.com.talkwithme.fragments.account;


import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;
import example.yuan.com.common.app.Fragment;
import example.yuan.com.common.app.PresenterFragment;
import example.yuan.com.factory.presenter.account.RegisterContract;
import example.yuan.com.factory.presenter.account.RegisterPresenter;
import example.yuan.com.talkwithme.R;
import example.yuan.com.talkwithme.activities.MainActivity;

/**
 * 注册界面
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter>
implements RegisterContract.View{

    private AccountTrigger mAccountTrigger;
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.edit_name)
    EditText mName;

    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    @BindView(R.id.txt_go_login)
    TextView mLogin;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    protected RegisterContract.Presenter initPersenter() {
        return new RegisterPresenter(this );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到Activity的应用
        mAccountTrigger=(AccountTrigger)context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone=mPhone.getText().toString();
        String password=mPassword.getText().toString();
        String name=mName.getText().toString();

        //调用presenter层注册
        mPresenter.register(phone,password,name);
    }

    @OnClick(R.id.txt_go_login)
    void onShowLoginClick(){
        //让AccountActivity进行界面切换
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        //如果触发了，一定是结束了
        mLoading.stop();//停止loading
        //让控件可以输入
        mPhone.setEnabled(true);
        mName.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
        mLogin.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        //正在进行时，界面不可以操作
        mLoading.start();//让空间不可以点击
        mPhone.setEnabled(false);
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
        mLogin.setEnabled(false);
    }

    @Override
    public void registerSuccess() {
        //注册成功后已经登陆 要进行界面跳转 MainActivity
        MainActivity.show(getContext());
        getActivity().finish();

    }
}
