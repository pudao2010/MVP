package com.pudao.android.selectimage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pudao.android.R;
import com.pudao.android.base.BaseFragment;
import com.pudao.android.utils.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pucheng on 2017/3/1.
 *
 */

public class SelectImageFragment extends BaseFragment implements SelectImageContract.View, ImageLoaderListener, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.btn_finish)
    Button mBtnFinish;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_all)
    Button mBtnAll;
    @BindView(R.id.btn_preview)
    Button mBtnPreview;

    private ImageAdapter mAdapter;
    private List<Image> mSelectedImage;
    private LoaderListener mCursorLoader = new LoaderListener();
    private String mCamImageName;

    private ImageFolderAdapter mImageFolderAdapter;

    private SelectImageContract.Operator mOperator;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_selectimage;
    }

    private static SelectOptions mOption;

    public static SelectImageFragment newInstance(SelectOptions options) {
        SelectImageFragment fragment = new SelectImageFragment();
        mOption = options;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        this.mOperator = (SelectImageContract.Operator) context;
        this.mOperator.setDataView(this);
        super.onAttach(context);
    }

    @Override
    protected void initview() {
        super.initview();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        mAdapter = new ImageAdapter(this.getContext(), this);
        mImageFolderAdapter = new ImageFolderAdapter(getActivity());
        mImageFolderAdapter.setLoader(this);
        mRecyclerView.addItemDecoration(new SpaceGridItemDecoration(DensityUtil.dip2px(this.getContext(), 1)));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initdata() {
        super.initdata();
        mSelectedImage = new ArrayList<>();

        if (mOption.getSelectCount() > 1 && mOption.getSelectedImages() != null) {
            List<String> images = mOption.getSelectedImages();
            for (String s : images) {
                // checkShare file exists
                if (s != null && new File(s).exists()) {
                    Image image = new Image();
                    image.setSelect(true);
                    image.setPath(s);
                    mSelectedImage.add(image);
                }
            }
        }
        getLoaderManager().initLoader(0, null, mCursorLoader);
    }

    @OnClick({R.id.btn_finish, R.id.btn_all, R.id.btn_preview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:

                break;
            case R.id.btn_all:

                break;
            case R.id.btn_preview:

                break;
        }
    }

    @Override
    public void onOpenCameraSuccess() {

    }

    @Override
    public void onCameraPermissionDenied() {

    }

    @Override
    public void displayImage(ImageView iv, String path) {
        Glide.with(this).load(path)
                .asBitmap()
                .centerCrop()
                .error(R.drawable.ic_split_graph)
                .into(iv);
    }

    @Override
    public void onItemClick(int position, long itemId) {
        if (mOption.isHasCam()) {
            System.out.println("点击事件发生---------------");
            if (position != 0) {
                System.out.println("position !=0 ---------------");
                handleSelectChange(position);
            } else {
                if (mSelectedImage.size() < mOption.getSelectCount()) {
                    mOperator.requestCamera();
                } else {
                    Toast.makeText(getActivity(), "最多只能选择 " + mOption.getSelectCount() + " 张图片", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            handleSelectChange(position);
        }
    }

    /**
     *  处理图片列表被点击的事件
     */
    private void handleSelectChange(int position) {
        Image image = mAdapter.getItem(position);
        //如果是多选模式
        final int selectCount = mOption.getSelectCount();
        if (selectCount > 1) {
            if (image.isSelect()) {
                image.setSelect(false);
                mSelectedImage.remove(image);
                mAdapter.updateItem(position);
            } else {
                if (mSelectedImage.size() == selectCount) {
                    Toast.makeText(getActivity(), "最多只能选择 " + selectCount + " 张照片", Toast.LENGTH_SHORT).show();
                } else {
                    image.setSelect(true);
                    mSelectedImage.add(image);
                    mAdapter.updateItem(position);
                }
            }
            handleSelectSizeChange(mSelectedImage.size());
        } else {
            mSelectedImage.add(image);
            handleResult();
        }
    }

    /**
     * LoaderManager.LoaderCallbacks是3.0之后出现的新特性，
     * 通过LoaderManager.LoaderCallbacks接口可以很轻松的实现
     * 异步加载数据到Fragment或Activity 中，Loaders提供了
     * 回调机制onLoadFinished()通知最终的运行结果，有点类似AsyncTask类，
     * 但由于Loader对于并发可以用过Loader管理器统一管理，
     * 所以更适合批量处理多个异步任务的处理(当然内部仍然是多线程)。
     * 要使用Loader就要实现LoaderManager.LoaderCallbacks接口，
     * 并实现它的抽象方法
     * 本质是安卓使用SQlite数据库存储图片等媒体文件, 通过查询数据库
     */
    private class LoaderListener implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,                 //path, 图片绝对路径
                MediaStore.Images.Media.DISPLAY_NAME,         //name, 图片文件名
                MediaStore.Images.Media.DATE_ADDED,           //date_time, 添加到数据库的时间,单位秒
                MediaStore.Images.Media._ID,                  //id, 数据库主键
                MediaStore.Images.Media.MINI_THUMB_MAGIC,     //缩略图
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME}; //直接包含图片的文件夹就是该图片的 bucket，就是文件夹名

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 0) {
                //数据库光标加载器
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
            }
            return null;
        }

        /**
         *  异步加载结束的回调
         */
        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
            if (data != null) {

                final ArrayList<Image> images = new ArrayList<>();
                final List<ImageFolder> imageFolders = new ArrayList<>();

                final ImageFolder defaultFolder = new ImageFolder();
                defaultFolder.setName("全部照片");
                defaultFolder.setPath("");
                imageFolders.add(defaultFolder);

                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String thumbPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        String bucket = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                        // 创建Image对象, 加入集合中
                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
                        image.setThumbPath(thumbPath);
                        image.setFolderName(bucket);

                        images.add(image);

                        //如果是新拍的照片
                        if (mCamImageName != null && mCamImageName.equals(image.getName())) {
                            image.setSelect(true);
                            mSelectedImage.add(image);
                        }

                        //如果是被选中的图片
                        if (mSelectedImage.size() > 0) {
                            for (Image i : mSelectedImage) {
                                if (i.getPath().equals(image.getPath())) {
                                    image.setSelect(true);
                                }
                            }
                        }

                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        ImageFolder folder = new ImageFolder();
                        folder.setName(folderFile.getName());
                        folder.setPath(folderFile.getAbsolutePath());
                        if (!imageFolders.contains(folder)) {
                            folder.getImages().add(image);
                            folder.setAlbumPath(image.getPath());//默认相册封面
                            imageFolders.add(folder);
                        } else {
                            // 更新
                            ImageFolder f = imageFolders.get(imageFolders.indexOf(folder));
                            f.getImages().add(image);
                        }


                    } while (data.moveToNext());
                }
                addImagesToAdapter(images);
                defaultFolder.getImages().addAll(images);
                if (mOption.isHasCam()) {
                    defaultFolder.setAlbumPath(images.size() > 1 ? images.get(1).getPath() : null);
                } else {
                    defaultFolder.setAlbumPath(images.size() > 0 ? images.get(0).getPath() : null);
                }
                mImageFolderAdapter.resetItem(imageFolders);

                //删除掉不存在的，在于用户选择了相片，又去相册删除
                if (mSelectedImage.size() > 0) {
                    List<Image> rs = new ArrayList<>();
                    for (Image i : mSelectedImage) {
                        File f = new File(i.getPath());
                        if (!f.exists()) {
                            rs.add(i);
                        }
                    }
                    mSelectedImage.removeAll(rs);
                }

                // If add new mCamera picture, and we only need one picture, we result it.
                if (mOption.getSelectCount() == 1 && mCamImageName != null) {
                    handleResult();
                }

                handleSelectSizeChange(mSelectedImage.size());
//                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private void handleSelectSizeChange(int size) {
        if (size > 0) {
            mBtnPreview.setEnabled(true);
            mBtnFinish.setEnabled(true);
            mBtnFinish.setText(String.format("%s(%s)", "完成", size));
        } else {
            mBtnPreview.setEnabled(false);
            mBtnFinish.setEnabled(false);
            mBtnFinish.setText("完成");
        }
    }

    private void addImagesToAdapter(ArrayList<Image> images) {
        mAdapter.clear();
        if (mOption.isHasCam()) {
            Image cam = new Image();
            mAdapter.addItem(cam);
        }
        mAdapter.addAll(images);
    }

    /**
     * 处理选择后的结果
     */
    private void handleResult() {
        if (mSelectedImage.size() != 0) {
            if (mOption.isCrop()) {
                List<String> selectedImage = mOption.getSelectedImages();
                selectedImage.clear();
                selectedImage.add(mSelectedImage.get(0).getPath());
                mSelectedImage.clear();
//                CropActivity.show(this, mOption);
            } else {
//                mOption.getCallback().doSelected(Util.toArray(mSelectedImage));
                getActivity().finish();
            }
        }
    }

    /**
     * 打开相机
     */
    private void toOpenCamera() {
        // 判断是否挂载了SD卡
        mCamImageName = null;
        String savePath = "";
        if (Util.hasSDCard()) {
            savePath = Util.getCameraPath();
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
            Toast.makeText(getActivity(), "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_LONG).show();
            return;
        }

        mCamImageName = Util.getSaveImageFullName();
        File out = new File(savePath, mCamImageName);
        Uri uri = Uri.fromFile(out);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                0x03);
    }

    /**
     * 拍照完成通知系统添加照片
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case 0x03:
                    if (mCamImageName == null) return;
                    Uri localUri = Uri.fromFile(new File(Util.getCameraPath() + mCamImageName));
                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                    getActivity().sendBroadcast(localIntent);
                    break;
                case 0x04:
                    if (data == null) return;
                    mOption.getCallback().doSelected(new String[]{data.getStringExtra("crop_path")});
                    getActivity().finish();
                    break;
            }
        }
    }

}
