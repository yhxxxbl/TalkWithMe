package example.yuan.com.talkwithme.fragments.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import net.qiujuer.genius.ui.Ui;

import example.yuan.com.common.tools.UiTool;
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
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //返回一个我们已经复写的透明dialog
        return new TransBottomSheetDialog (getContext());

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
                mListener.onSelectedImage(paths[0]);
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
        void onSelectedImage(String path);
    }

    public static class TransBottomSheetDialog extends BottomSheetDialog{

        public TransBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        public TransBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window=getWindow();
            if (window==null)
                return;
            //屏幕高度
            int ScreenHight= UiTool.getScreenHeight(getOwnerActivity());
            //动态获取fragment高度
            int StatusHight= UiTool.getStatusBarHeight(getOwnerActivity());
            int TheFragmentHeight=ScreenHight-StatusHight;



            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ScreenHight<=0?ViewGroup.LayoutParams.MATCH_PARENT:ScreenHight);

        }
    }

}
