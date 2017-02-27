package com.pudao.android.ui;

import android.os.Bundle;

import com.pudao.android.R;
import com.pudao.android.base.BaseFragment;

/**
 * Created by pucheng on 2017/2/27.
 * 微信
 */

public class WechatFragment extends BaseFragment{

    public static final String BUNDLE_TITLE = "title";

    public static WechatFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        WechatFragment fragment = new WechatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wechat;
    }
}
