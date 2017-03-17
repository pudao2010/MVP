package com.pudao.android.rxjava;

import com.pudao.android.network.ApiException;

import rx.functions.Func1;

/**
 * 转换类的包装,RxJava将 T类型 转换为 R类型
 */
public abstract class HttpResultConversionFunc<T, R> implements Func1<T, R> {

    @Override
    public R call(T t) {
        R r = conversion(t);
        if (r == null) {
            throw new ApiException(ApiException.CONVERSION_RESULT_NULL);
        }
        return r;
    }
    // 将T类型转换为R类型的抽象方法,子类必须实现
    public abstract R conversion(T t);

}


