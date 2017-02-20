package com.pudao.android.mvp;

/**
 * Created by pucheng on 2017/2/20.
 */

public interface Presenter<T extends MvpView> {

    // 在Activity或者Fragment的生命周期onCreate时进行绑定
    void attachView(T mvpView);

    // 在Activity或者Fragment的生命周期onDestroy时进行解绑
    void detachView();

}
