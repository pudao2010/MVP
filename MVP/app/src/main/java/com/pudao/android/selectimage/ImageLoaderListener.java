package com.pudao.android.selectimage;

import android.widget.ImageView;

/**
 * 暴露一个图片加载器
 */
public interface ImageLoaderListener {

    void displayImage(ImageView iv, String path);

}
