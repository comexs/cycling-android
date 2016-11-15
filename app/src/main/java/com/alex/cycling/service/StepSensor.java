package com.alex.cycling.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by zhong on 2016/11/14.
 */

public class StepSensor implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mStepCount;
    private Sensor mStepDetector;
    private static final int sensorTypeD = Sensor.TYPE_STEP_DETECTOR;
    private static final int sensorTypeC = Sensor.TYPE_STEP_COUNTER;
    private float mCount;//步行总数
    private float mDetector;//步行探测器

    public StepSensor(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mStepCount = mSensorManager.getDefaultSensor(sensorTypeC);
        mStepDetector = mSensorManager.getDefaultSensor(sensorTypeD);
    }

    public void end() {
        mSensorManager.unregisterListener(this);
    }

    private void start() {
        mSensorManager.registerListener(this, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == sensorTypeC) {
//            setStepCount(event.values[0]);
            mCount = event.values[0];
        }
        if (event.sensor.getType() == sensorTypeD) {
            if (event.values[0] == 1.0) {
                mDetector++;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
