package com.alex.cycling.db.bean;

/**
 * Created by comexs on 16/3/30.
 */
public class TrackInfo {

    private long trackId;

    private String trackUUID;

    private String trackName;

    private long startTime;

    private long endTime;

    private long totalTime;

    private double totalDis;

    private double averageSpeed;

    private double maxSpeed;

    private double climbUp;

    private double climbDown;

    private double maxSlope;

    private double minSlope;

    private double calorie;

    private double startLat;

    private double startLon;

    private String startGeoCode;

    private double endLat;

    private double endLon;

    private String endGeoCode;

    private String imageUrl;

    private String device;

    private int status;

    public TrackInfo(long trackId, String trackUUID, String trackName, long startTime, long endTime, long totalTime, double totalDis, double averageSpeed, double maxSpeed, double climbUp, double climbDown, double maxSlope, double minSlope, double calorie, double startLat, double startLon, String startGeoCode, double endLat, double endLon, String endGeoCode, String imageUrl, String device, int status) {
        this.trackId = trackId;
        this.trackUUID = trackUUID;
        this.trackName = trackName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.totalDis = totalDis;
        this.averageSpeed = averageSpeed;
        this.maxSpeed = maxSpeed;
        this.climbUp = climbUp;
        this.climbDown = climbDown;
        this.maxSlope = maxSlope;
        this.minSlope = minSlope;
        this.calorie = calorie;
        this.startLat = startLat;
        this.startLon = startLon;
        this.startGeoCode = startGeoCode;
        this.endLat = endLat;
        this.endLon = endLon;
        this.endGeoCode = endGeoCode;
        this.imageUrl = imageUrl;
        this.device = device;
        this.status = status;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public String getTrackUUID() {
        return trackUUID;
    }

    public void setTrackUUID(String trackUUID) {
        this.trackUUID = trackUUID;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public double getTotalDis() {
        return totalDis;
    }

    public void setTotalDis(double totalDis) {
        this.totalDis = totalDis;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getClimbUp() {
        return climbUp;
    }

    public void setClimbUp(double climbUp) {
        this.climbUp = climbUp;
    }

    public double getClimbDown() {
        return climbDown;
    }

    public void setClimbDown(double climbDown) {
        this.climbDown = climbDown;
    }

    public double getMaxSlope() {
        return maxSlope;
    }

    public void setMaxSlope(double maxSlope) {
        this.maxSlope = maxSlope;
    }

    public double getMinSlope() {
        return minSlope;
    }

    public void setMinSlope(double minSlope) {
        this.minSlope = minSlope;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public void setStartLon(double startLon) {
        this.startLon = startLon;
    }

    public String getStartGeoCode() {
        return startGeoCode;
    }

    public void setStartGeoCode(String startGeoCode) {
        this.startGeoCode = startGeoCode;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    public String getEndGeoCode() {
        return endGeoCode;
    }

    public void setEndGeoCode(String endGeoCode) {
        this.endGeoCode = endGeoCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
