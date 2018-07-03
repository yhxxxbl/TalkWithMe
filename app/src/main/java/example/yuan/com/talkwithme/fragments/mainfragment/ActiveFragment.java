package example.yuan.com.talkwithme.fragments.mainfragment;


import butterknife.BindView;
import example.yuan.com.common.app.Fragment;
import example.yuan.com.common.widget.GalleryView;
import example.yuan.com.talkwithme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends Fragment {
    @BindView(R.id.galleryView)
    GalleryView mGallery;


    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();

        mGallery.setup(getLoaderManager(), new GalleryView.SelectChangeListener() {
            @Override
            public void onSelectedCountChanged(int Count) {

            }
        });
    }
}
