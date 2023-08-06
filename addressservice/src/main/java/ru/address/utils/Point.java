package ru.address.utils;

import lombok.Data;


@Data
public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getDistance(Point point) {
        return Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2));
    }
}
