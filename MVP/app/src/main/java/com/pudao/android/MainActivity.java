package com.pudao.android;

import android.view.View;
import android.widget.ImageView;

import com.pudao.android.adapter.MainAdapter;
import com.pudao.android.base.BaseActivity;
import com.pudao.android.lazy.LazyViewPager;
import com.pudao.android.popup.AddPupupWindow;
import com.pudao.android.widget.AlphaTabsIndicator;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_add_friend)
    ImageView mIvAddFriend;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.viewpager)
    LazyViewPager mViewpager;
    @BindView(R.id.alpha_indicator)
    AlphaTabsIndicator mAlphaIndicator;
    private AddPupupWindow addPupupWindow;

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

        addPupupWindow = new AddPupupWindow(this, R.layout.pupupwindow_add, new AddPupupWindow.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    //发起群聊
                    case 0:
                        // TODO 群聊选择好友
                        break;
                    //添加新的好友
                    case 1:
                        showToast("添加好友");
                        break;
                    //扫一扫
                    case 2:
                        showToast("二维码扫描");
                        break;
                    //帮助及反馈
                    case 3:
                        showToast("帮助反馈");
                        break;
                }
            }
        });
    }

    @OnClick({R.id.iv_add_friend, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_friend:
                //TODO 弹出popup
                addPupupWindow.showAsDropDown(mIvAddFriend);
                break;
            case R.id.iv_search:
                //TODO 跳转搜索
                break;
        }
    }
}
