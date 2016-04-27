package com.jni;


import android.os.Parcel;
import android.os.Parcelable;

import com.alex.greendao.WorkPoint;

public class BtPoint implements Parcelable {

    public static final long TIME_REF = 1420041600000L;

    public double lat;
    public double lon;
    public double alt;
    public float speed;
    public float power;
    public int temp;
    public int status;
    public long time;

    public void clear() {
        this.lat = 0;
        this.lon = 0;
        this.alt = 0;
        this.speed = 0;
        this.power = 0;
        this.temp = 0;
        this.status = 0;
        this.time = 0;
    }

    public void copy(WorkPoint workPoint) {
        this.lat = workPoint.getLat();
        this.lon = workPoint.getLon();
        this.alt = workPoint.getAlt();
        this.speed = workPoint.getSpeed();
        this.power = workPoint.getPower();
        this.temp = workPoint.getTemp();
        this.status = workPoint.getStatus();
        this.time = workPoint.getTime();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeDouble(this.alt);
        dest.writeFloat(this.speed);
        dest.writeFloat(this.power);
        dest.writeInt(this.temp);
        dest.writeInt(this.status);
        dest.writeLong(this.time);
    }

    public BtPoint() {
    }

    protected BtPoint(Parcel in) {
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.alt = in.readDouble();
        this.speed = in.readFloat();
        this.power = in.readFloat();
        this.temp = in.readInt();
        this.status = in.readInt();
        this.time = in.readLong();
    }

    public static final Creator<BtPoint> CREATOR = new Creator<BtPoint>() {
        @Override
        public BtPoint createFromParcel(Parcel source) {
            return new BtPoint(source);
        }

        @Override
        public BtPoint[] newArray(int size) {
            return new BtPoint[size];
        }
    };
}
