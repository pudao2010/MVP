package com.pudao.android.selectimage;

import android.support.v4.view.ViewPager;

import com.pudao.android.R;
import com.pudao.android.base.BaseActivity;

public class ImageGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_gallery;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
