package example.yuan.com.talkwithme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.Ui;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import example.yuan.com.common.app.Activity;
import example.yuan.com.common.widget.PortraitView;
import example.yuan.com.talkwithme.activities.AccountActivity;
import example.yuan.com.talkwithme.fragments.mainfragment.ActiveFragment;
import example.yuan.com.talkwithme.fragments.mainfragment.ContactFragment;
import example.yuan.com.talkwithme.fragments.mainfragment.FindFragment;
import example.yuan.com.talkwithme.helper.NavHelper;

public class MainActivity extends Activity implements
        BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {

    @BindView(R.id.appBar)
    View mLayAppBar;

    @BindView(R.id.img_portrait)
    PortraitView mportrait;

    @BindView(R.id.txt_title)
    TextView mTextView;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.BottomNavigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    View mAction;

    private NavHelper<Integer> mNavHelper;


    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            return;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);

        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact))
                .add(R.id.action_find, new NavHelper.Tab<>(FindFragment.class, R.string.title_find));

        //添加事件监听
        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this)
                .load(R.drawable.bg_src_paint)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppBar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    return;
                }else {
                    Toast.makeText(this,"Please pass the permission",Toast.LENGTH_SHORT).show();
                }break;
                default:
        }
    }

    @Override
    protected void initData() {
        super.initData();
        //从底部导航中接管Menu
        //然后手动设置第一次点击
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_contact, 0);
    }

    @OnClick(R.id.img_serch)
    void onSearchMenuClick() {

    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
        AccountActivity.show(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //转接事件流到帮助类中
        return mNavHelper.performClickMenu(item.getItemId());

    }

    /**
     * NacHelper处理后回调的方法
     *
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中取出Title资源ID
        mTextView.setText(newTab.extra);
        //对浮动按钮进行隐藏于显示的动画
        float transY = 0;
        float rotation = 0;
        //主界面时隐藏
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            //tansY默认为0时显示
            if (Objects.equals(newTab.extra, R.string.title_contact)) {
                rotation = 720;
            }
            if (Objects.equals(newTab.extra, R.string.title_find)){
                transY = Ui.dipToPx(getResources(), 76);

            }

        }
        //开始动画
        //旋转，位移，时间，差值器
        mAction.animate().rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(400)
                .start();
    }
}



