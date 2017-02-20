package com.pudao.android.mvp;

import com.pudao.android.network.Api;
import com.pudao.android.network.ApiImpl;
import com.pudao.android.utils.ReflectUtil;

/**
 * Created by pucheng on 2017/2/20.
 */

public class BasePresenter<T extends MvpView> implements Presenter<T> {

    protected Api mApi = new ApiImpl();

    protected String TAG = this.getClass().getSimpleName();

    // Presenter层持有View层的引用
    private T mvpView;

    // 在attachView时获得实例
    @Override
    public void attachView(T mvpView) {
        this.mvpView = mvpView;
    }

    // 在detachView时置为null
    @Override
    public void detachView() {
        mvpView = null;
    }

    public T getMvpView(){
        return mvpView;
    }


    public Class<T> getMvpViewClass(){
        return ReflectUtil.getClassGeneric(this, 0);
    }
}
