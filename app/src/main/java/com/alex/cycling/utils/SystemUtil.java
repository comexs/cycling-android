package com.alex.cycling.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.alex.cycling.base.CSApplication;
import com.alex.cycling.service.LocationService;
import com.alex.cycling.ui.camera.bean.PhotoItem;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * 判断服务是否启动, 注意只要名称相同, 会检测任何服务.
     *
     * @param context      上下文
     * @param serviceClass 服务类
     * @return 是否启动服务
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        if (context == null) {
            return false;
        }
        Context appContext = context.getApplicationContext();
        ActivityManager manager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            List<ActivityManager.RunningServiceInfo> infos = manager.getRunningServices(Integer.MAX_VALUE);
            if (infos != null && !infos.isEmpty()) {
                for (ActivityManager.RunningServiceInfo service : infos) {
                    // 添加Uid验证, 防止服务重名, 当前服务无法启动
                    if (getUid(context) == service.uid && serviceClass.getName().equals(service.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取应用的Uid, 用于验证服务是否启动
     *
     * @param context 上下文
     * @return uid
     */
    public static int getUid(Context context) {
        if (context == null) {
            return -1;
        }
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
            if (infos != null && !infos.isEmpty()) {
                for (ActivityManager.RunningAppProcessInfo processInfo : infos) {
                    if (processInfo.pid == pid) {
                        return processInfo.uid;
                    }
                }
            }
        }
        return -1;
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
                            .endsWith(".jpeg"));
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

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = CSApplication.getInstance().getPackageManager().getPackageInfo(CSApplication.getInstance().getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {

        }
        return versionCode;
    }

    public static String getVersionName() {
        String versionName = "";
        try {
            versionName = CSApplication.getInstance().getPackageManager().getPackageInfo(CSApplication.getInstance().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return versionName;
    }
}
