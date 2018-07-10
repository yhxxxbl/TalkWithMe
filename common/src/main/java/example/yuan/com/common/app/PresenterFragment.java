package example.yuan.com.common.app;

import android.content.Context;

import example.yuan.com.factory.presenter.BaseContract;

/**
 * Created by Administrator on 2018/7/8/008.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment
implements BaseContract.View<Presenter>{

    protected Presenter mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //界面onAttach之后就开始触发
        initPersenter();
    }

    @Override
    public void showError(int str) {
            Application.showToast(str);
    }

    /**
     * 初始化presenter
     * @return
     */
    protected abstract Presenter initPersenter();
    @Override
    public void showLoading() {

    }

    @Override
    public void setPresenter(Presenter presenter) {
        //View 中赋值Presenter
            this.mPresenter=presenter;
    }
}
