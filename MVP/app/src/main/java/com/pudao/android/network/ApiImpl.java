package com.pudao.android.network;

import com.pudao.android.rxjava.HttpMethod;
import com.pudao.android.rxjava.ResultSubscriber;

import rx.Subscription;

/**
 * Created by pucheng on 2017/2/20.
 *
 */

public class ApiImpl implements Api {

    private ApiService mApiService;

    public ApiImpl() {
        // 实例化ApiService, 单例设计
    }

    @Override
    public Subscription login(String usercode, String password, ResultSubscriber<Void> subscriber) {
        // 执行具体的Rxjava的线程操作符转换
        return HttpMethod.execute(mApiService.login(usercode, password), subscriber);
    }
}
