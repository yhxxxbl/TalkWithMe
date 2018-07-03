package example.yuan.com.talkwithme.fragments.media;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.yuan.com.common.widget.GalleryView;
import example.yuan.com.talkwithme.R;

/**
 *图片选择Fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment
implements GalleryView.SelectChangeListener{

    private GalleryView mGalleryView;
    private OnSelectedListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取GalleryView
        View root =inflater.inflate(R.layout.fragment_gallery, container, false);
        mGalleryView=(GalleryView) root.findViewById(R.id.galleryView);
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        mGalleryView.setup(getLoaderManager(),this);
    }

    @Override
    public void onSelectedCountChanged(int Count) {
        if (Count>0){
            dismiss();
            if (mListener!=null){
                //得到所有的选中的图片的路径
                String [] paths=mGalleryView.getSelectedPath();
                mListener.onSelecterImage(paths[0]);
                mListener=null;
            }
        }
    }

    /**
     * 设置事件监听，并返回自己
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener){
        mListener=listener;
        return this;
    }

    /**
     * 选中图片监听回调
     */
    public interface OnSelectedListener{
        void onSelecterImage(String path);
    }


}
