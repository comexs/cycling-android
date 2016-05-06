package com.alex.cycling.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.alex.cycling.base.CSApplication;
import com.alex.cycling.service.LocationService;
import com.alex.cycling.ui.camera.bean.PhotoItem;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by comexs on 16/4/12.
 */
public class SystemUtil {

    public static boolean hasLocationServiceRun(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前机器的屏幕信息对象<br/>
     * 另外：通过android.os.Build类可以获取当前系统的相关信息
     *
     * @return
     */
    public static DisplayMetrics getScreenInfo() {
        WindowManager windowManager = (WindowManager) CSApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        // dm.widthPixels;//寬度
        // dm.heightPixels; //高度
        // dm.density; //密度
        return dm;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = CSApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = CSApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //获取path路径下的图片
    public static ArrayList<PhotoItem> findPicsInDir(String path) {
        ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    String filePath = pathname.getAbsolutePath();
                    return (filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath
                            .endsWith(".jepg"));
                }
            })) {
                photos.add(new PhotoItem(file.getAbsolutePath(), file.lastModified()));
            }
        }
        Collections.sort(photos);
        return photos;
    }

    public static int getCameraAlbumWidth() {
        return (DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(10)) / 4 - DisplayUtil.dip2px(4);
    }

    // 相机照片列表高度计算
    public static int getCameraPhotoAreaHeight() {
        return getCameraPhotoWidth() + DisplayUtil.dip2px(4);
    }

    public static int getCameraPhotoWidth() {
        return DisplayUtil.getScreenWidth() / 4 - DisplayUtil.dip2px(2);
    }

}
