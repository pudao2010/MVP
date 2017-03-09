package com.pudao.android.rxjava;


import com.pudao.android.base.ResultModle;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Thisdk on 2016/4/11.
 * RxJava 对网络请求的统一处理
 */
public class HttpMethod {

    /**
     * 使用通用的处理方式去处理网络请求
     *
     * @param observable RxJava Adapter 生成的请求
     * @param subscriber 用于返回数据的观察者
     * @param <T>        接口返回的数据类型
     * @return
     */
    public static <T> Subscription execute(Observable<Response<ResultModle<T>>> observable, Subscriber<T> subscriber) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()) //取消订阅
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultErrorFunc<T>()) //操作符进行过滤
                .map(new HttpResultStatusCodeFunc<T>())
                .subscribe(subscriber);
    }

    /**
     * 接口返回的数据与返回到P层数据不一样需要转换的处理方式
     *
     * @param observable RxJava Adapter 生成的请求
     * @param subscriber 用于返回数据的观察者
     * @param conversion 转换数据的Func1
     * @param <T>        接口返回的数据类型
     * @param <R>        需要的最终类型
     * @return
     */
    public static <T, R> Subscription execute(Observable<Response<ResultModle<T>>> observable, Subscriber<R> subscriber, HttpResultConversionFunc<T, R> conversion) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultErrorFunc<T>())
                .map(new HttpResultStatusCodeFunc<T>())
                .map(conversion)
                .subscribe(subscriber);
    }

}
