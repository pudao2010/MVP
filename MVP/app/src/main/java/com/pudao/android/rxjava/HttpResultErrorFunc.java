package com.pudao.android.rxjava;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.pudao.android.base.ResultModle;
import com.pudao.android.network.ApiException;

import java.io.IOException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Thisdk on 2016/4/11.
 * 全部请求的错误从这里开始产生
 */
public class HttpResultErrorFunc<T> implements Func1<Response<ResultModle<T>>, ResultModle<T>> {

    @Override
    public ResultModle<T> call(Response<ResultModle<T>> response) {
        //正常返回的处理方式
        if (response.isSuccessful()) {
            return response.body();
        }
        //其他错误码的处理方式
        int code = response.code();
        String errorJson = "";
        ResultModle<T> errorBody = null;
        try {
            errorJson = response.errorBody().string();
        } catch (IOException e) {
            //抛出状态码 , 告知I/O错误
            throw new ApiException("HTTP " + code + " I/O steam error");
        }
        try {
            errorBody = new Gson().fromJson(errorJson, new TypeToken<ResultModle<T>>() {
            }.getType());
        } catch (JsonSyntaxException | JsonIOException e) {
            //抛出状态码 , 和无法序列化的内容
            throw new ApiException("HTTP " + code + " " + errorJson);
        }
        if (errorBody == null) {
            //抛出状态码 , 告知没有内容
            throw new ApiException("HTTP " + code + " error body is null");
        }
        //存在返回内容对象,按统一方式处理
        return errorBody;
    }


}


