package com.alex.cycling.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.alex.greendao.WorkPoint;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * Created by Administrator on 2015/6/30.
 */
public final class BaiduTool {

    private static CoordinateConverter converter;
    private static double originShift = 2 * 3.14159265358979324 * 6378137 / 2.0;//墨卡托投影半周长
    private static final double EARTH_RADIUS = 6378137;//地球半径

    public static LatLng wgs2bd(LatLng oldLatLng) {
        if (converter == null) {
            converter = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
        }
        converter.coord(oldLatLng);
        return converter.convert();
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);

        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));
        double dist = theta * EARTH_RADIUS;
        return dist;
    }

    public static double getDis(LatLng latLng, LatLng latLng2) {
        LatLng p1 = wgs2bd(latLng);
        LatLng p2 = wgs2bd(latLng2);
        return DistanceUtil.getDistance(p1, p2);
    }

    public static double getDis(WorkPoint workPoint1, WorkPoint workPoint2) {
        LatLng latLng1 = new LatLng(workPoint1.getLat(), workPoint1.getLon());
        LatLng latLng2 = new LatLng(workPoint2.getLat(), workPoint2.getLon());
        return getDis(latLng1, latLng2);
    }

    public static int LatLngBoundsToZoom(LatLng latLng, LatLng latLng2, int screen) {
        double dis = getDis(latLng, latLng2);
        dis *= 1.2;
        double resolution = dis / screen;
        if (resolution < 1) {
            return 19;
        } else if (resolution < 2) {
            return 18;
        } else if (resolution < 4) {
            return 17;
        } else if (resolution < 8) {
            return 16;
        } else if (resolution < 16) {
            return 15;
        } else if (resolution < 32) {
            return 14;
        } else if (resolution < 64) {
            return 13;
        } else if (resolution < 128) {
            return 12;
        } else if (resolution < 256) {
            return 11;
        } else if (resolution < 512) {
            return 10;
        } else if (resolution < 1024) {
            return 9;
        } else if (resolution < 2048) {
            return 8;
        } else if (resolution < 4096) {
            return 7;
        } else if (resolution < 8192) {
            return 6;
        } else if (resolution < 16384) {
            return 5;
        } else if (resolution < 32768) {
            return 4;
        } else {
            return 3;
        }
    }

    public static double pixelsToMeters(float zoom, int pixel) {
        if (zoom == 19) {
            return pixel * 0.5 + 0.5;
        }
        return Math.pow(2, 18 - zoom) * pixel;
    }

    public static double pixelsToAngle(float zoom, int pixel) {
        double dis = pixelsToMeters(zoom, pixel);
        double angle = dis * 180 / originShift;
        return angle;
    }

    public static void isGpsOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);
        boolean isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isOpen) {
//            RtViewUtils.showNCAlert(context, "检测到您没有打开GPS，是否去开启?", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    openGps(context);
//                }
//            });
        }
    }

    public static void openGps(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public static void geoAddress(LatLng latLng, OnGetGeoCoderResultListener listener) {
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(listener);
        ReverseGeoCodeOption reversOption;
        reversOption = new ReverseGeoCodeOption();
        reversOption.location(wgs2bd(latLng));
        geoCoder.reverseGeoCode(reversOption);
    }
}
