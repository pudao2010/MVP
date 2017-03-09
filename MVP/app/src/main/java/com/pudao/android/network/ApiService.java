package com.pudao.android.network;

import com.pudao.android.base.ResultModle;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pucheng on 2017/2/20.
 * 使用注解的网络接口
 */

public interface ApiService {

    // 登录
    @FormUrlEncoded
    @POST("login/login!login.action")
    Observable<Response<ResultModle<Void>>> login(@Field("usercode") String usercode, @Field("password") String password);
}
