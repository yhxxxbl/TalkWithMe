package example.yuan.com.factory.net;

import example.yuan.com.Common;
import example.yuan.com.factory.Factory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 */

public class NetWork {
    //构建一个Retrofit
    public static Retrofit getRetrofit(){
        //得到一个Ok Client
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        Retrofit.Builder builder=new Retrofit.Builder();

        //设置为本地链接
        return builder.baseUrl(Common.Constance.API_URI)
                //设置Client
                .client(okHttpClient)
                //设置Json解析
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

    }
}
