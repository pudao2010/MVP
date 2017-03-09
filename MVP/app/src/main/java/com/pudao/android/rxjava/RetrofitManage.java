package com.pudao.android.rxjava;


import com.pudao.android.utils.Constant;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 2017年3月9日14:00:38
 */
public class RetrofitManage {

    private static RetrofitManage mRetrofitManage;

    public static void init(OkHttpClient okHttpClient) {
        mRetrofitManage = new RetrofitManage(okHttpClient);
    }

    private Retrofit mRetrofit;

    private Map<Class<?>, Object> mApis;
    // 私有构造方法，标准的单例设计模式
    private RetrofitManage(OkHttpClient okHttpClient) {

        mApis = new HashMap<>();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 单例模式的Api实例获取
     *
     * @param clazz 需要获取实例的类
     * @param <T>   泛型
     * @return 实例
     */
    public static <T> T getApiInstance(Class<T> clazz) {
        T object = (T) mRetrofitManage.mApis.get(clazz);
        if (object == null) {
            object = mRetrofitManage.mRetrofit.create(clazz);
            mRetrofitManage.mApis.put(clazz, object);
        }
        return object;
    }

}
