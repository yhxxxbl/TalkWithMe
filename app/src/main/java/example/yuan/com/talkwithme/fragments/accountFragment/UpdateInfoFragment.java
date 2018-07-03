package example.yuan.com.talkwithme.fragments.accountFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import example.yuan.com.common.app.Fragment;
import example.yuan.com.common.widget.PortraitView;
import example.yuan.com.talkwithme.R;
import example.yuan.com.talkwithme.fragments.media.GalleryFragment;

/**
 * 用户更新信息的界面
 */
public class UpdateInfoFragment extends Fragment {

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.img_portrait)
    void onPortraitClick(){
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelecterImage(String path) {

            }
        })       //show的时候建议使用getchildfragmentmanager
                .show(getChildFragmentManager(),GalleryFragment.class.getName());
    }
}
