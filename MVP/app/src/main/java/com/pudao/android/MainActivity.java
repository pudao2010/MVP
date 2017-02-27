package com.pudao.android;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.pudao.android.adapter.MainAdapter;
import com.pudao.android.base.BaseActivity;
import com.pudao.android.widget.AlphaTabsIndicator;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_add_friend)
    ImageView mIvAddFriend;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.alpha_indicator)
    AlphaTabsIndicator mAlphaIndicator;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initview() {
        super.initview();
        //
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), mAlphaIndicator);
        mViewpager.setAdapter(mainAdapter);
        mViewpager.addOnPageChangeListener(mainAdapter);
        //
        mAlphaIndicator.setViewPager(mViewpager);
        //
        mAlphaIndicator.getTabView(0).showNumber(6);
        mAlphaIndicator.getTabView(1).showNumber(888);
        mAlphaIndicator.getTabView(2).showNumber(88);
        mAlphaIndicator.getTabView(3).showPoint();
    }

    @OnClick({R.id.iv_add_friend, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_friend:
                //TODO 弹出popup
                break;
            case R.id.iv_search:
                //TODO 跳转搜索
                break;
        }
    }
}
