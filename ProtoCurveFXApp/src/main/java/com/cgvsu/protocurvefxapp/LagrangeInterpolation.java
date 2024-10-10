package com.cgvsu.protocurvefxapp;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class LagrangeInterpolation {

    public static ArrayList<Point2D> points = new ArrayList<>();

    public static void redraw(GraphicsContext graphicsContext, Canvas canvas) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        final int POINT_RADIUS = 3;
        for (Point2D point : points) {
            graphicsContext.fillOval(
                    point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        if (points.size() > 1) {
            graphicsContext.setLineWidth(2);
            for (int x = 0; x < canvas.getWidth(); x++) {
                double y = interpolate(x);
                if (y >= 0 && y < canvas.getHeight()) {
                    graphicsContext.strokeLine(x, y, x, y);
                }
            }
        }
    }

    // методичка, страница 98
    private static double calculateLagrangePolynomial(double x, int i) {
        double result = 1;
        int n = points.size();
        for (int j = 0; j < n; j++) {
            if (j != i) {
                result *= (x - points.get(j).getX()) / (points.get(i).getX() - points.get(j).getX());
            }
        }
        return result;
    }

    private static double interpolate(double x) {
        double y = 0;
        int n = points.size();
        for (int i = 0; i < n; i++) {
            y += points.get(i).getY() * calculateLagrangePolynomial(x, i);
        }
        return y;
    }
}
