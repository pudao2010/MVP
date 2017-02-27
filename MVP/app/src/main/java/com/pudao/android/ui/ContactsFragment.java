package com.pudao.android.ui;

import android.os.Bundle;

import com.pudao.android.R;
import com.pudao.android.base.BaseFragment;

/**
 * Created by pucheng on 2017/2/27.
 * 通讯录
 */

public class ContactsFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    public static final String BUNDLE_TITLE = "title";

    public static ContactsFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
