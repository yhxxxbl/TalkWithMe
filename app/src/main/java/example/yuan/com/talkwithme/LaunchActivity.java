package example.yuan.com.talkwithme;

import android.os.Bundle;


import example.yuan.com.common.app.Activity;
import example.yuan.com.talkwithme.activities.MainActivity;
import example.yuan.com.talkwithme.assist.PermissionFragment;

public class LaunchActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
      if ( PermissionFragment.isHaveAll(this,getSupportFragmentManager())){
          MainActivity.show(this);
          finish();
      }
    }
}
