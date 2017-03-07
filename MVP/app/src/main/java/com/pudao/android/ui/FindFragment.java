package com.pudao.android.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.pudao.android.R;
import com.pudao.android.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pucheng on 2017/2/27.
 * 发现
 */

public class FindFragment extends BaseFragment {

    @BindView(R.id.re_friends)
    RelativeLayout mReFriends;

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

    @OnClick(R.id.re_friends)
    public void onClick() {
        Snackbar.make(mReFriends, "snackbar", Snackbar.LENGTH_SHORT)
                .setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();

    }

}
