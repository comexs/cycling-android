/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.alex.cycling.utils.thread;

public interface Callback<T> {
    void onValue(T value);
}
