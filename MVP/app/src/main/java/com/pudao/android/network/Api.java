package com.pudao.android.network;

import com.pudao.android.rxjava.ResultSubscriber;

import rx.Subscription;


/**
 * Created by pucheng on 2017/2/20.
 * Api接口，返回订阅者
 */

public interface Api {
    Subscription login(String usercode, String password, ResultSubscriber<Void> subscriber);
}
