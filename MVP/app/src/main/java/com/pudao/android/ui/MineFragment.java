package com.pudao.android.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pudao.android.R;
import com.pudao.android.base.BaseFragment;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import butterknife.OnClick;

/**
 * Created by pucheng on 2017/2/27.
 * 我
 */

public class MineFragment extends BaseFragment {

    private static final int IMAGE_PICKER = 1;

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

    @OnClick(R.id.re_myinfo)
    public void onClick() {
//        SelectImageActivity.show(getContext(), new SelectOptions.Builder()
//                .setSelectCount(1)
//                .setHasCam(true)
//                .setCrop(700, 700)
//                .setCallback(new SelectOptions.Callback() {
//                    @Override
//                    public void doSelected(String[] images) {
//                        String path = images[0];
//                    }
//                }).build());

//        Intent intent = new Intent(this.getContext(), ImageGridActivity.class);
//        startActivityForResult(intent, IMAGE_PICKER);

        // 自由配置选项
        ImgSelConfig config = new ImgSelConfig.Builder(this.getContext(), loader)
                // 是否多选, 默认true
                .multiSelect(true)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.drawable.icon_arrow_back)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(true)
                // 最大选择图片数量，默认9
                .maxNum(9)
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, 1);
    }

    // 自定义图片加载器
    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            // TODO 在这边可以自定义图片加载库来加载ImageView，例如Glide、Picasso、ImageLoader等
            Glide.with(context).load(path).into(imageView);
        }
    };


}
