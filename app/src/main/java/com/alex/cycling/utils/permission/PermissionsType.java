package com.alex.cycling.utils.permission;

/**
 * Created by zhong on 2016/6/1.
 */
public class PermissionsType {

    /**
     * 读取手机权限
     */
    public static final int READ_PHONE_STATE_CODE = 1;
    /**
     * 获取相机权限
     */
    public static final int CAMERA_CODE = 2;

    /**
     * 获取存储权限
     */
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 3;

    public static class PermissionsTypeExtend {

        public static String toDescription(int type) {
            switch (type) {
                case PermissionsType.READ_PHONE_STATE_CODE:
                    return "需要在系统“权限”中打开“电话”开关，才能更好的为你服务";
                case PermissionsType.CAMERA_CODE:
                    return "需要在系统“权限”中打开“相机”开关，才能相机拍照";
                case PermissionsType.WRITE_EXTERNAL_STORAGE_CODE:
                    return "需要在系统“权限”中打开“存储”开关，才能离线缓存";
                default:
                    return "需要在系统“权限”中打开相关权限，才能更好的为你服务";
            }
        }
    }

}
