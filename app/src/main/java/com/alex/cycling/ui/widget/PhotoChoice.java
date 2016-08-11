package com.alex.cycling.ui.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.utils.FileUtil;
import com.alex.cycling.utils.ToastUtil;
import com.alex.cycling.utils.permission.PermissionsChecker;
import com.alex.cycling.utils.permission.PermissionsType;

import java.io.File;

/**
 * Created by Administrator on 2016/3/9.
 */
public class PhotoChoice {

    //上传图片路径
    public String mImagePath = null;

    public static final int PHONE_IMAGE_REQUEST_CODE = 88;
    public static final int CAMERA_IMAGE_REQUEST_CODE = 99;
    public static final int PHOTO_IMAGE_RESULT_CODE = 100;

    Activity mContext;

    ChoiceListener listener;

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public PhotoChoice(Activity activity, ChoiceListener listener) {
        mContext = activity;
        this.listener = listener;
        if (null == mPermissionsChecker) {
            mPermissionsChecker = new PermissionsChecker(mContext);
        }
    }

    //创建图片的地址
    public boolean createPath() {
        if (FileUtil.isSDExist()) {
            File file = new File(FileUtil.getImgDir());
            if (!file.exists()) {
                file.mkdir();
            }
            mImagePath = file.getPath() + File.separator + System.currentTimeMillis() + ".png";
            if (new File(mImagePath).exists()) {
                try {
                    new File(mImagePath).delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    public void setAvatar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("更换头像")
                .setItems(R.array.camera_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!FileUtil.isSDExist()) {
                            ToastUtil.showToast("SD卡不存在");
                            return;
                        }
                        //6.0权限检测
                        if (PermissionsChecker.isMarshmallowOrHigher()) {
                            String[] permissions = PERMISSIONS;
                            if (mPermissionsChecker.lacksPermissions(permissions)) {
                                mContext.requestPermissions(permissions, PermissionsType.WRITE_EXTERNAL_STORAGE_CODE);
                                return;
                            }
                        }
                        if (which == 0) {
                            createPath();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 下面这句指定调用相机拍照后的照片存储的路径
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mImagePath)));
                            mContext.startActivityForResult(intent, CAMERA_IMAGE_REQUEST_CODE);
                        } else {
                            createPath();
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            mContext.startActivityForResult(intent, PHONE_IMAGE_REQUEST_CODE);
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        String dividers[] = {
                "android:id/titleDividerTop", "android:id/titleDivider", "android:id/alertTitle"
        };

        for (int i = 0; i < dividers.length; ++i) {
            int divierId = mContext.getResources().getIdentifier(dividers[i], null, null);
            View divider = dialog.findViewById(divierId);
            if (divider != null) {
                if (!dividers[i].equals("android:id/alertTitle")) {
                    divider.setBackgroundColor(mContext.getResources().getColor(R.color.black));
                } else if (divider instanceof TextView) {
                    ((TextView) divider).setTextColor(mContext.getResources().getColor(R.color.black));
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // 相册回来
        if (requestCode == PHONE_IMAGE_REQUEST_CODE) {
            if (data != null) {
                startPhotoZoom(data.getData());
            }
        } else if (requestCode == CAMERA_IMAGE_REQUEST_CODE) {
            if (new File(mImagePath).exists()) {
                File temp = new File(mImagePath);
                startPhotoZoom(Uri.fromFile(temp));
            }
        } else if (requestCode == PHOTO_IMAGE_RESULT_CODE) {
            if (data != null && null != listener) {
                listener.choiceSuccess(mImagePath);
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("output", Uri.fromFile(new File(mImagePath)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mContext.startActivityForResult(intent, PHOTO_IMAGE_RESULT_CODE);
    }

    public interface ChoiceListener {
        void choiceSuccess(String uri);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PermissionsType.WRITE_EXTERNAL_STORAGE_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //选择允许后采取的动作

                    } else {
                        ToastUtil.showToast("需要存储权限");
                    }
                }
            }
        }
    }

}
