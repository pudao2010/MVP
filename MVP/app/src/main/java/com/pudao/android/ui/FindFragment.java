package com.pudao.android.ui;

import android.os.Bundle;

import com.pudao.android.R;
import com.pudao.android.base.BaseFragment;

/**
 * Created by pucheng on 2017/2/27.
 * 发现
 */

public class FindFragment extends BaseFragment{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    public static final String BUNDLE_TITLE = "title";

    public static FindFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        FindFragment fragment = new FindFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
