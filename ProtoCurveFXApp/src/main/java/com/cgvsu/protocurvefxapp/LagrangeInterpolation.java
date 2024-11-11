package com.cgvsu.protocurvefxapp;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LagrangeInterpolation {
    // методичка, страница 98
    public static double calculateLagrangePolynomial(double x, int i, ArrayList<Point2D> points) {
        double result = 1;
        int n = points.size();
        for (int j = 0; j < n; j++) {
            if (j != i) {
                result *= (x - points.get(j).getX()) / (points.get(i).getX() - points.get(j).getX());
            }
        }
        return result;
    }
    // методичка, страница 98
    public static double interpolate(double x, ArrayList<Point2D> points) {
        double y = 0;
        int n = points.size();
        for (int i = 0; i < n; i++) {
            y += points.get(i).getY() * calculateLagrangePolynomial(x, i, points);
        }
        return y;
    }

    public static ArrayList<Point2D> calculateLagrangePoints(ArrayList<Point2D> points, double leftBorder, double rightBorder, double lowerBorder, double upperBorder) {
        ArrayList<Point2D> interpolatedPoints = new ArrayList<>();
        for (double x = leftBorder; x < rightBorder; x += 1) {
            double y = LagrangeInterpolation.interpolate(x, points);
            if (foundPointOnTheSameX(x, points)) {
                y = getPointYFromTheSameX(x, points);
            }
            y = checkYReq(y, lowerBorder, upperBorder);
            interpolatedPoints.add(new Point2D(x, y));
        }
        return interpolatedPoints;
    }

    public static boolean foundPointOnTheSameX(double x, ArrayList<Point2D> points) {
        for (Point2D point : points) {
            if (Math.abs(x - point.getX()) < 0.01) {
                return true;
            }
        }
        return false;
    }

    private static double getPointYFromTheSameX(double x, ArrayList<Point2D> points) {
        for (Point2D point : points) {
            if (Math.abs(x - point.getX()) < 0.1) {
                return point.getY();
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    private static double checkYReq(double y, double lowerBorder, double upperBorder) {
        if (y < lowerBorder)
            return lowerBorder - 1;
        else if (y > upperBorder)
            return upperBorder + 1;
        else
            return y;
    }
}
