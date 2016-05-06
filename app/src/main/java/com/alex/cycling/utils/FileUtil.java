package com.alex.cycling.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2015/6/1.
 */
public final class FileUtil {

    public final static String BASEPATH = "alex";

    public final static String LOG_PATH = "Log";

    public final static String DATE_PATH = "Data";

    public final static String IMAGE_PATH = "Image";

    public final static String TRACK_PATH = "workTrack";

    public final static String CACHE_IMAGE_PATH = "cache";

    public final static String DB_PATH = "db";


    public static String getMainDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BASEPATH + File.separator;
    }

    public static String getTrackDir() {
        String path = getMainDir() + DATE_PATH + File.separator + TRACK_PATH + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getLogDir() {
        String path = getMainDir() + LOG_PATH + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getDbDir() {
        String path = getMainDir() + DB_PATH + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getImgDir() {
        String path = getMainDir() + IMAGE_PATH + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getCacheImgDir() {
        String path = getMainDir() + IMAGE_PATH + File.separator + CACHE_IMAGE_PATH + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    // 判断SD卡是否存在
    public static boolean isSDExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //获取系统的SD卡图片
    public static String getSystemPhotoPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    }

}
