package example.yuan.com.talkwithme.fragments.account;


import android.content.Context;

import example.yuan.com.common.app.Fragment;
import example.yuan.com.talkwithme.R;

/**
 * 登陆界面
 */
public class LoginFragment extends Fragment {
    private AccountTrigger mAccountTrigger;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到Activity的应用
        mAccountTrigger=(AccountTrigger)context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onResume() {
        super.onResume();
        //切换fragment
        mAccountTrigger.triggerView();
    }
}
