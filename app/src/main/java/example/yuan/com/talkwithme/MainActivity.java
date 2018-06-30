package example.yuan.com.talkwithme;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import butterknife.BindView;
import butterknife.OnClick;
import example.yuan.com.common.app.Activity;
import example.yuan.com.common.widget.PortraitView;

public class MainActivity extends Activity {

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




    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        Glide.with(this)
                .load(R.drawable.bg_src_paint)
                .centerCrop()
                .into(new ViewTarget<View,GlideDrawable>(mLayAppBar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
    }
    @OnClick(R.id.img_serch)
    void onSearchMenuClick(){
    }

    @OnClick(R.id.btn_action)
    void onActionClick(){
    }
}
