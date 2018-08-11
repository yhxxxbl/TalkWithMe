package example.yuan.com.talkwithme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;


import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

import example.yuan.com.common.app.Activity;
import example.yuan.com.factory.persistence.Account;
import example.yuan.com.talkwithme.activities.AccountActivity;
import example.yuan.com.talkwithme.activities.MainActivity;
import example.yuan.com.talkwithme.assist.PermissionFragment;

public class LaunchActivity extends Activity {
    //Drawable

    private ColorDrawable mDrawable;



    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //获得根布局
        View root=findViewById(R.id.activity_launch);
        //获取颜色
        int color= UiCompat.getColor(getResources(),R.color.colorPrimary);
        //创建一个drawable
        ColorDrawable drawable=new ColorDrawable(color);
        //将drawable设置给背景
        root.setBackground(drawable);
        mDrawable=drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        //动画完成50%的时候开始等待获取到pushID
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });
    }

    /**
     * 等待个推框架设置好值
     */
    private void waitPushReceiverId(){
        if (Account.isLogin()){
            //已经登陆的情况下判断是否绑定
            //如果没有绑定则等待广播接收器进行绑定
            if (Account.isBind()){
                skip();
                return;
            }
        }else {
            //没有登陆
            //没有登陆的情况下不能绑定PushID
            if (!TextUtils.isEmpty(Account.getPushId())) {
                skip();
                return;
            }
        }
            // 循环等待
            getWindow().getDecorView()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            waitPushReceiverId();
                        }
                    }, 500);

    }

    /**
     * 再跳转之前需要给剩下的50%完成
     */
    private void skip(){
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });
    }

    private void reallySkip(){
        //真实的跳转操作
        //权限检查
        if ( PermissionFragment.isHaveAll(this,getSupportFragmentManager())){
            MainActivity.show(this);
            //检测跳转到主页还是登陆
            if (Account.isLogin()){
                MainActivity.show(this);
            }else {
                AccountActivity.show(this);
            }
            finish();
        }
    }
    /**
     * 给背景设置一个动画
     * @param endProgress 动画的结束进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress,final Runnable endCallback){
        int finalColor= Resource.Color.WHITE;//获得一个最终的颜色

        ArgbEvaluator evaluator=new ArgbEvaluator();
        int endColor=(int)evaluator.evaluate(endProgress,mDrawable.getColor(),finalColor);
        //构建一个属性动画
        ValueAnimator animator= ObjectAnimator.ofObject(this,property,evaluator,endColor);
        animator.setDuration(1500);//时间
        animator.setIntValues(mDrawable.getColor(),endColor);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        animator.start();
    }
    
    private final Property<LaunchActivity,Object> property=new Property<LaunchActivity, Object>(Object.class,"color") {
        @Override
        public void set(LaunchActivity object, Object value) {
            object.mDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LaunchActivity launchActivity) {
            return launchActivity.mDrawable.getColor();
        }
    };

}
