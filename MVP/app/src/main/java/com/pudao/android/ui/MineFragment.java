package com.pudao.android.ui;

import android.os.Bundle;

import com.pudao.android.R;
import com.pudao.android.base.BaseFragment;

/**
 * Created by pucheng on 2017/2/27.
 * 我
 */

public class MineFragment extends BaseFragment{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    public static final String BUNDLE_TITLE = "title";

    public static MineFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        MineFragment fragment = new MineFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
