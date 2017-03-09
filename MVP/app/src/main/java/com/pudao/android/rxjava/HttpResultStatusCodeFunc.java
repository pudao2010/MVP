package com.pudao.android.rxjava;


import com.pudao.android.base.ResultModle;
import com.pudao.android.network.ApiException;

import rx.functions.Func1;

/**
 * 状态码
 */
public class HttpResultStatusCodeFunc<T> implements Func1<ResultModle<T>, T> {

    @Override
    public T call(ResultModle<T> resultModle) {
        if (resultModle.getCode() != 0) {
            throw new ApiException(resultModle.getCode(), resultModle.getMsg());
        } else {
            if (resultModle.getRows() != null) {
                return resultModle.getRows();
            } else {
                return resultModle.getData();
            }
        }
    }

}


