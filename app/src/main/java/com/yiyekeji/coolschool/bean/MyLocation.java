package com.yiyekeji.coolschool.bean;

/**
 * Created by lxl on 2017/1/11.
 */
public class MyLocation {
    private double X;
    private double Y;

    public double getX() {
        return X;
    }

    @Override
    public String toString() {
        return "MyLocation{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }
}
