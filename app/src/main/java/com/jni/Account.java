package com.jni;

/**
 * Created by comexs on 16/4/12.
 */
public class Account {

    static {
        System.loadLibrary("jniact");
    }

    public static native void statistic(ActInfo actInfo);


    public static native void isVerfyPoint(BtPoint btPoint);

}
