package example.yuan.com.factory.data;

import android.support.annotation.StringRes;

/**
 * 数据源接口定义
 */

public interface DataSource {

    interface SuccessCallback<T>{
        //数据加载成功，网络请求成功
        //只关注成功的接口
        void onDataLoaded(T t);
    }

    interface FailedCallback{
        //数据加载失败，网络请求失败
        //只关注失败的接口
        void onDataNotAvailable(@StringRes int strRes);
    }

    /**
     * 同时包括了成功或者失败的接口
     * @param <T>  任意类型
     */
    interface CallBack<T> extends SuccessCallback<T>,FailedCallback{

    }
}
