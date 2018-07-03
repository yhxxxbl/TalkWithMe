package example.yuan.com.talkwithme.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import example.yuan.com.common.app.Activity;
import example.yuan.com.talkwithme.R;
import example.yuan.com.talkwithme.fragments.accountFragment.UpdateInfoFragment;

public class AccountActivity extends Activity {
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

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frag_container,new UpdateInfoFragment())
                .commit();

    }
}
