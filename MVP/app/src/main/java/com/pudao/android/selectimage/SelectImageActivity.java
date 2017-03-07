package com.pudao.android.selectimage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import com.pudao.android.R;
import com.pudao.android.base.BaseActivity;

public class SelectImageActivity extends BaseActivity implements SelectImageContract.Operator{

    private static final int READ_SD = 0x03; // 读取SD
    private static final int OPEN_CAMERA = 0x04; // 打开摄像头

    private SelectImageContract.View mView;

    private static SelectOptions mOption;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_image;
    }

    public static void show(Context context, SelectOptions options) {
        mOption = options;
        context.startActivity(new Intent(context, SelectImageActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 这里去请求读取SD卡权限
        applyPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, READ_SD);
    }

    @Override
    protected void onApplyPermissionsResult(int requestCode, boolean isSuccess) {
        if (requestCode==READ_SD){
            if (isSuccess){
                // 获取到权限后, 用Fragment显示图片缩略图
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content, SelectImageFragment.newInstance(mOption))
                        .commitNowAllowingStateLoss();
            }
        }
        if (requestCode == OPEN_CAMERA && isSuccess){

        }
    }

    @Override
    public void requestCamera() {

    }

    @Override
    public void requestExternalStorage() {

    }

    @Override
    public void onBack() {

    }

    @Override
    public void setDataView(SelectImageContract.View view) {
        mView = view;
    }
}
