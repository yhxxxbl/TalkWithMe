package example.yuan.com.talkwithme.activities;

import java.util.Objects;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import example.yuan.com.common.app.Activity;
import example.yuan.com.common.widget.DrawerLayout;
import example.yuan.com.common.widget.PortraitView;
import example.yuan.com.talkwithme.R;
import example.yuan.com.talkwithme.fragments.main.ActiveFragment;
import example.yuan.com.talkwithme.fragments.main.ContactFragment;
import example.yuan.com.talkwithme.fragments.main.FindFragment;
import example.yuan.com.talkwithme.helper.NavHelper;
import net.qiujuer.genius.ui.Ui;

public class MainActivity extends Activity implements
        BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {

    @BindView(R.id.main_layout_right)
    View mLayoutRight;

    @BindView(R.id.main_layout_left)
    NavigationView mLayoutLeft;

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

    @BindView(R.id.drawLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_drawer_img)
    ImageView mNavImageView;

    private NavHelper<Integer> mNavHelper;

    /**
     * Intent调起MainActivity的方法
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        View mHeaderLayout = mLayoutLeft.getHeaderView(0)
                .findViewById(R.id.header_layout);
        //初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);

        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact))
                .add(R.id.action_find, new NavHelper.Tab<>(FindFragment.class, R.string.title_find));

        //添加事件监听
        mNavigation.setItemIconTintList(null);
        mNavigation.setOnNavigationItemSelectedListener(this);
        //为DrawerLayout设置监听事件

        mDrawerLayout.setCustomLeftEdgeSize(mDrawerLayout,1f);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                mLayoutRight.layout(mLayoutLeft.getRight(), 0, mLayoutLeft.getRight() + display.getWidth(),
                        display.getHeight());

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //初始化NavigationView
        mLayoutLeft.setItemIconTintList(null);
        mLayoutLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        Glide.with(this)//加载ToolBar的图片
                .load(R.drawable.bg_src_paint)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppBar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

        Glide.with(this)//加载NavigationView中HeaderLayout的背景
                .load(R.drawable.header_photo)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mHeaderLayout) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

        Glide.with(getApplicationContext())
                .load(R.drawable.nav_drawer_img)//加载厘米秀
                .listener(new RequestListener<Integer, GlideDrawable>() {
                              @Override
                              public boolean onException(Exception e, Integer model
                                      , Target<GlideDrawable> target, boolean isFirstResource) {
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(GlideDrawable resource, Integer model,
                                                             Target<GlideDrawable> target,
                                                             boolean isFromMemoryCache, boolean
                                                                     isFirstResource) {
                                  // 获取gif动画时长
                                  GifDrawable drawable = (GifDrawable) resource;
                                  GifDecoder decoder = drawable.getDecoder();
                                  long duration = 0;
                                  for (int i = 0; i < drawable.getFrameCount(); i++) {
                                      duration += decoder.getDelay(i);
                                  }
                                  Log.e("peter", "动画时长" + duration);
                                  //后面这段代码根据你的具体业务需求，添加相应的代码块
              /*  handler.sendEmptyMessageDelayed(MESSAGE_SUCCESS,
                        duration + 700);*/
                                  return false;
                              }
                          }
                ).into(new GlideDrawableImageViewTarget
                (mNavImageView, 1));//1代表播放一次
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

    @OnClick(R.id.img_portrait)
    void onPortraitClick() {
        mDrawerLayout.openDrawer(GravityCompat.START);
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
            if (Objects.equals(newTab.extra, R.string.title_find)) {
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



