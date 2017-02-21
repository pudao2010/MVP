package com.pudao.android.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pudao.android.mvp.BasePresenter;
import com.pudao.android.mvp.MvpView;
import com.pudao.android.utils.ReflectUtil;

/**
 * Created by pucheng on 2017/2/20.
 *
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements MvpView {

    protected String TAG = getClass().getSimpleName();

    protected View mRootView;

    protected abstract int getLayoutId();

    protected P mFragmentPresenter;

    protected LayoutInflater mLayoutInflater;

    public static Fragment newInstance() {
        throw new RuntimeException("The static method must be rewritten");
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
//            ButterKnife.bind(this, mRootView);
            initFragmentPresenter();
            mLayoutInflater = getLayoutInflater(savedInstanceState);
            initview();
            initdata();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initFragmentPresenter() {
        try {
            Class<P> classGeneric = ReflectUtil.getClassGeneric(this, 0);
            if (classGeneric != null) {
                mFragmentPresenter = classGeneric.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mFragmentPresenter != null) {
            Class mvpViewClass = mFragmentPresenter.getMvpViewClass();
            if (mvpViewClass == null) {
                throw new RuntimeException("the presenter is not generic type");
            }
            boolean isHaveMvpViewInterface = false;
            Class<?>[] interfaces = getClass().getInterfaces();
            for (Class clazz : interfaces) {
                if (mvpViewClass == clazz) {
                    isHaveMvpViewInterface = true;
                    break;
                }
            }
            if (!isHaveMvpViewInterface) {
                throw new RuntimeException("please implements MvpView : " + mvpViewClass.getName());
            }
            mFragmentPresenter.attachView(this);
        }
    }

    protected void initview() {
    }

    protected void initdata() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
        if (mFragmentPresenter != null) {
            mFragmentPresenter.detachView();
            mFragmentPresenter = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ButterKnife.unbind(this);
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    //--------------------运行时权限封装---------------------------

    protected void applyPermissions(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            onApplyPermissionsResult(requestCode, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            onApplyPermissionsResult(requestCode, false);
            return;
        }
        onApplyPermissionsResult(requestCode, grantResults[0] == PackageManager.PERMISSION_GRANTED);
    }

    protected void onApplyPermissionsResult(int requestCode, boolean isSuccess) {
    }

    //------------------------页面跳转封装------------------------------------

    protected void readyGo(Class<? extends BaseActivity> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    protected void readyGo(Class<? extends BaseActivity> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void readyGoForResult(Class<? extends BaseActivity> clazz, int requestCode) {
        startActivityForResult(new Intent(getActivity(), clazz), requestCode);
    }

    protected void readyGoForResult(Class<? extends BaseActivity> clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * MvpView 中的接口方法.这里的是默认实现,如果需要其他方式的展示错误,请在子类重写
     */
    @Override
    public void showErrorInfo(String msg) {
        showToast(msg);
    }

    //----------------------Toast相关封装-------------------------------
    protected void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

}
