package example.yuan.com.talkwithme.activities;

import android.content.Context;
import android.content.Intent;

import example.yuan.com.common.app.Activity;
import example.yuan.com.common.app.Fragment;
import example.yuan.com.talkwithme.R;
import example.yuan.com.talkwithme.fragments.user.UpdateInfoFragment;

public class UserActivity extends Activity{

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }
    private Fragment mfragment;
    //账户Activity显示的入口
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mfragment=new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frag_container,mfragment)
                .commit();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mfragment.onActivityResult(requestCode,resultCode,data);
    }
}
