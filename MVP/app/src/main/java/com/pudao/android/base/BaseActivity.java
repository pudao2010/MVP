package com.pudao.android.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.pudao.android.mvp.BasePresenter;
import com.pudao.android.mvp.MvpView;
import com.pudao.android.utils.ReflectUtil;

/**
 * Created by pucheng on 2017/2/20.
 * 基础的Activity,封装了公用的方法
 * 添加了presenter的自动管理
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements MvpView{


    protected String TAG = getClass().getSimpleName();

    public abstract int getLayoutId();

    protected P mActivityPresenter;

    protected Toolbar mToolbar;

    protected LayoutInflater mLayoutInflater;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(getLayoutId());
        //
//        ButterKnife.bind(this);
        initActivityPresenter();
        if (savedInstanceState == null)
            savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null)
            init(savedInstanceState);
        mLayoutInflater = getLayoutInflater();
        initview();
        initdata();
    }

    // 子类可复写此方法做一些初始化的操作
    protected void init(Bundle savedInstanceState) {

    }

    protected void initview() {

    }

    protected void initdata() {
    }


    // 根据反射获取到泛型的Presenter
    private void initActivityPresenter(){
        try {
            Class<P> classGeneric = ReflectUtil.getClassGeneric(this, 0);
            if (classGeneric != null) {
                mActivityPresenter = classGeneric.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mActivityPresenter != null) {
            Class mvpViewClass = mActivityPresenter.getMvpViewClass();
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
            mActivityPresenter.attachView(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    // Activity销毁时进行解绑
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mActivityPresenter != null) {
            mActivityPresenter.detachView();
            mActivityPresenter = null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    //--------------------针对Android6.0的运行时权限封装---------------------------------

    /**
     *  申请权限
     */
    protected void applyPermissions(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            onApplyPermissionsResult(requestCode, true);
        }
    }

    /**
     *  申请权限后的结果回调
     */
    protected void onApplyPermissionsResult(int requestCode, boolean isSuccess) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            onApplyPermissionsResult(requestCode, false);
        } else {
            onApplyPermissionsResult(requestCode, grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }

    //------------------ 页面跳转的封装 --------------------------------

    protected void readyGo(Class<? extends BaseActivity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    protected void readyGo(Class<? extends BaseActivity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void readyGoForResult(Class<? extends BaseActivity> clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
    }

    protected void readyGoForResult(Class<? extends BaseActivity> clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    //-------------------Toast提示的封装------------------------------------

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    //------------------MvpView接口中的方法实现, 这里给出默认的实现,子类可重写-----
    @Override
    public void showErrorInfo(String msg) {
        showToast(msg);
    }

    //------------------Fragment相关------------------------------------------

    // 添加Fragment
    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }
    // 替换Fragment
    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment);
            transaction.commit();
        }
    }
}
