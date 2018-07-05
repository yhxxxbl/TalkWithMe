package example.yuan.com.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import example.yuan.com.common.R;
import example.yuan.com.common.widget.recycler.RecyclerAdapter;


public class GalleryView extends RecyclerView {
    private static final int MAX_IMAGE_COUNT=3;//最大选中图片数量
    private static final int MIN_IMAGE_FILESIZE=2*1024;//最小的图片大小
    private static final int LOARDER_ID=12345;

    private Adapter mAdapter = new Adapter();
    private LoaderCallback mloaderCallback=new LoaderCallback();
    private List<Image> mSelectedImages=new LinkedList<>();
    private SelectChangeListener mListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                if(onItemSelectClick(image))
                {
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * 初始化方法
     * @param loaderManager
     * @return 可用于销毁Loader
     */
    public int setup(LoaderManager loaderManager, SelectChangeListener listener){
        mListener=listener;
        loaderManager.initLoader(LOARDER_ID,null,mloaderCallback);
        return LOARDER_ID;
    }

    /**
     * cell点击的具体逻辑
     * @param image
     * @return true表示进行了数据更改需要刷新
     */
    private boolean onItemSelectClick(Image image){
        //是否需要进行刷新
        boolean notifyRefresh;
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
            image.isSelect=false;
            notifyRefresh=true;
        }else {
            if(mSelectedImages.size()>=MAX_IMAGE_COUNT){
                String str=getResources().getString(R.string.label_gallery_select_max_size);
                str=String.format(str,MAX_IMAGE_COUNT);

                Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
                notifyRefresh=false;
            }else {
                mSelectedImages.add(image);
                image.isSelect=true;
                notifyRefresh=true;
            }
        }
        //如果数据有更改，那我们需要通知外部数据改变了
        if(notifyRefresh)
            notifySelectChanged();
        return true;
    }

    /**
     * 得到选中图片的全部地址
     * @return  返回一个数组
     */
    public String[] getSelectedPath(){
        String[] paths=new String [mSelectedImages.size()];
        int i=0;
        for(Image image:mSelectedImages){
            paths[i++] =image.path;
        }
        return paths;
    }

    /**
     * 清空图片
     */
    public void clear(){
        for (Image image:mSelectedImages){
            //先重置状态
            image.isSelect=false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged(){
        //得到监听者，并提供数量变化回调
        SelectChangeListener listener=mListener;
        if (listener!=null){
            listener.onSelectedCountChanged(mSelectedImages.size());
        }
    }

    /**
     * 通知Adapter数据更改
     * @param images
     */
    private void updataSource(List<Image> images){
        mAdapter.replace(images);
    }
    /**
     * 数据加载的Loader Callback
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
            private final String [] Image_Projection=new String[]{
                    MediaStore.Images.Media._ID,//id
                    MediaStore.Images.Media.DATA,//图片路径
                    MediaStore.Images.Media.DATE_ADDED //图片的创建时间
            };
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            if (id==LOARDER_ID){
                //如果是我们的ID则可以进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI    ,
                        Image_Projection,
                        null,
                        null,
                        Image_Projection[2]+" DESC");//倒序查询？？？？？？？
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<Image> images=new ArrayList<>();
            if (data!=null){
                int count=data.getCount();
                if (count>0){
                    data.moveToFirst();//移动游标到开始位置
                    //得到对应的列的坐标
                    int indexId=data.getColumnIndexOrThrow(Image_Projection[0]);
                    int indexPath=data.getColumnIndexOrThrow(Image_Projection[1]);
                    int indexData=data.getColumnIndexOrThrow(Image_Projection[2]);

                    do {
                        //循环读取
                        int id=data.getInt(indexId);
                        String path=data.getString(indexPath);
                        long datatime=data.getLong(indexData);

                        File file=new File(path);
                        if (!file.exists()||file.length()<MIN_IMAGE_FILESIZE){
                            continue;
                            //如果图片不存在或者图片太小，跳过本次循环
                        }
                        //添加一条新的数据
                        Image image=new Image();
                        image.id=id;
                        image.path=path;
                        image.date=datatime;
                        images.add(image);

                    }while (data.moveToNext());
                }
            }
                updataSource(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            updataSource(null);
            //进行界面清空操作
        }
    }

    /**
     * 内部的数据结构
     */
    private static class Image {
        int id;//数据的ID
        String path;//图片的路径

        String name;//图片的名字

        long date;//图片的创建日期
        boolean isSelect;//是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    /**
     * 适配器
     */
    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;

        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }
    }

    /**
     * Cell对应的Holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {
        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;


        public ViewHolder(View itemView) {
            super(itemView);

            mPic=(ImageView)itemView.findViewById(R.id.img_image);
            mShade=itemView.findViewById(R.id.view_shade);
            mSelected=(CheckBox)itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)//加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存加载
                    .centerCrop()
                    .placeholder(R.color.grey_200)//默认颜色
                    .into(mPic);

            mShade.setVisibility(image.isSelect?VISIBLE:INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }

    /**
     * 监听器
     */
    public interface SelectChangeListener{
        void onSelectedCountChanged(int Count);
    }
}
