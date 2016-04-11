package com.alex.cycling.utils;

import com.alex.greendao.WorkPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comexs on 16/4/9.
 */
public class VacuateUtil {


    /**
     * 存储采样点数据的链表
     */
    public List<WorkPoint> points = new ArrayList<WorkPoint>();

    /**
     * 控制数据压缩精度的极差
     */
    private static double D = 0.0001;

    public void vacuate(List<WorkPoint> points, int level) {
        if (level == 1) {
            D = 0.00001;
        } else if (level == 2) {
            D = 0.00005;
        } else if (level == 3) {
            D = 0.0001;
        }
        if (this.points.size() > 0) {
            this.points.clear();
        }
        this.points = points;
        compress(points.get(0), points.get(points.size() - 1));
    }

    public List<WorkPoint> getResult() {
        return points;
    }

    /**
     * 对矢量曲线进行压缩
     *
     * @param from 曲线的起始点
     * @param to   曲线的终止点
     */
    public void compress(WorkPoint from, WorkPoint to) {

        /**
         * 压缩算法的开关量
         */
        boolean switchvalue = false;

        /**
         * 由起始点和终止点构成的直线方程一般式的系数
         */
        double A = (from.getLon() - to.getLon())
                / Math.sqrt(Math.pow((from.getLon() - to.getLon()), 2)
                + Math.pow((from.getLat() - to.getLat()), 2));

        /**
         * 由起始点和终止点构成的直线方程一般式的系数
         */
        double B = (to.getLat() - from.getLat())
                / Math.sqrt(Math.pow((from.getLon() - to.getLon()), 2)
                + Math.pow((from.getLat() - to.getLat()), 2));

        /**
         * 由起始点和终止点构成的直线方程一般式的系数
         */
        double C = (from.getLat() * to.getLon() - to.getLat() * from.getLon())
                / Math.sqrt(Math.pow((from.getLon() - to.getLon()), 2)
                + Math.pow((from.getLat() - to.getLat()), 2));

        double d = 0;
        double dmax = 0;
        int m = points.indexOf(from);
        int n = points.indexOf(to);
        if (n == m + 1)
            return;
        WorkPoint middle = null;
        List<Double> distance = new ArrayList<Double>();
        for (int i = m + 1; i < n; i++) {
            d = Math.abs(A * (points.get(i).getLat()) + B
                    * (points.get(i).getLon()) + C)
                    / Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
            distance.add(d);
        }
        dmax = distance.get(0);
        for (int j = 1; j < distance.size(); j++) {
            if (distance.get(j) > dmax)
                dmax = distance.get(j);
        }
        if (dmax > D)
            switchvalue = true;
        else
            switchvalue = false;
        if (!switchvalue) {
            // 删除Points(m,n)内的坐标
            for (int i = m + 1; i < n; i++) {
                points.get(i).setStatus(-1);
            }

        } else {
            for (int i = m + 1; i < n; i++) {
                if ((Math.abs(A * (points.get(i).getLat()) + B
                        * (points.get(i).getLon()) + C)
                        / Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2)) == dmax))
                    middle = points.get(i);
            }
            compress(from, middle);
            compress(middle, to);
        }
    }

}
