package com.alex.cycling.utils;

import android.widget.Toast;

import com.alex.cycling.base.CSApplication;

/**
 * Created by comexs on 16/5/5.
 */
public class ToastUtil {

    public static void showToast(String title) {
        Toast.makeText(CSApplication.getInstance(), title, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int id) {
        Toast.makeText(CSApplication.getInstance(), CSApplication.getInstance().getString(id), Toast.LENGTH_SHORT).show();
    }
}
