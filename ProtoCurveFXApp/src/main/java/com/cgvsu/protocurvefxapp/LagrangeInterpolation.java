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
}
