package example.yuan.com.talkwithme.activities;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import example.yuan.com.common.app.Activity;
import example.yuan.com.common.app.Fragment;
import example.yuan.com.talkwithme.R;
import example.yuan.com.talkwithme.fragments.account.AccountTrigger;
import example.yuan.com.talkwithme.fragments.account.LoginFragment;
import example.yuan.com.talkwithme.fragments.account.RegisterFragment;

public class AccountActivity extends Activity implements AccountTrigger {
    private Fragment mfragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;
    @BindView(R.id.im_bg)
    ImageView mBackground;

    //账户Activity显示的入口
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }



    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化fragment
        mfragment=mLoginFragment=new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frag_container,mfragment)
                .commit();
        //初始化背景
        Glide.with(this).load(R.drawable.bg_sc_beauty).centerCrop()
                                .into(mBackground);
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mfragment==mLoginFragment){
            if (mRegisterFragment==null){
                //第一次的情况为空 之后就不为空了
                mRegisterFragment=new RegisterFragment();
            }
            fragment=mRegisterFragment;
        }else {
            //默认情况下已经赋值，无需判定是否为空
                fragment=mLoginFragment;
        }
        //重新复制当前正在显示的Fragment;
        mfragment=fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragment)
                .commit();
    }
}
