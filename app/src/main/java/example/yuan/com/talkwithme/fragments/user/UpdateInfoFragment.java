package example.yuan.com.talkwithme.fragments.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import example.yuan.com.common.app.Application;
import example.yuan.com.common.app.Fragment;
import example.yuan.com.common.widget.PortraitView;
import example.yuan.com.factory.Factory;
import example.yuan.com.factory.net.UploadHelper;
import example.yuan.com.talkwithme.R;
import example.yuan.com.talkwithme.fragments.media.GalleryFragment;

import static android.app.Activity.RESULT_OK;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options=new UCrop.Options();
                //设置图片处理的格式JPEG
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                //设置压缩后的图片精度
                options.setCompressionQuality(96);

                File file= Application.getPortraitTmpFile();
                UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(file))
                        .withAspectRatio(1,1)//一比一比例
                        .withMaxResultSize(520,520)//返回最大的尺寸
                        .withOptions(options)
                        .start(getActivity());


            }
        }).show(getChildFragmentManager(),GalleryFragment.class.getName());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri!=null){
                loadPortrait(resultUri);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 将Uri加载到当前头像
     * @param uri
     */

    private void loadPortrait(Uri uri){
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

        final String localPath=uri.getPath();
        Log.e("TAG","localPath"+localPath);

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url=UploadHelper.uploadPortrait(localPath);
                Log.e("TAG","URL"+url);
            }
        });
    }
}
