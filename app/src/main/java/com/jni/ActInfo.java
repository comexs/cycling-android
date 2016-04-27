package com.jni;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by comexs on 16/4/6.
 */
public class ActInfo implements Parcelable {

    public double distance;   //距离
    public double averSpeed;  //平均速度
    public double climbed;    //攀升
    public double calorie;    //卡路里

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAverSpeed() {
        return averSpeed;
    }

    public void setAverSpeed(double averSpeed) {
        this.averSpeed = averSpeed;
    }

    public double getClimbed() {
        return climbed;
    }

    public void setClimbed(double climbed) {
        this.climbed = climbed;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.distance);
        dest.writeDouble(this.averSpeed);
        dest.writeDouble(this.climbed);
        dest.writeDouble(this.calorie);
    }

    public ActInfo() {
    }

    protected ActInfo(Parcel in) {
        this.distance = in.readDouble();
        this.averSpeed = in.readDouble();
        this.climbed = in.readDouble();
        this.calorie = in.readDouble();
    }

    public static final Creator<ActInfo> CREATOR = new Creator<ActInfo>() {
        @Override
        public ActInfo createFromParcel(Parcel source) {
            return new ActInfo(source);
        }

        @Override
        public ActInfo[] newArray(int size) {
            return new ActInfo[size];
        }
    };
}
