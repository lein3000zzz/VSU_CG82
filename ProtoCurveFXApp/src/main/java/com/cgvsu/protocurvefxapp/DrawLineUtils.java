package com.cgvsu.protocurvefxapp;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class DrawLineUtils {
    public static void drawBresenhamLine(GraphicsContext graphicsContext, ArrayList<Point2D> interpolatedPoints) {
        PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
            Point2D start = interpolatedPoints.get(i);
            Point2D end = interpolatedPoints.get(i + 1);

            int x0 = (int) Math.round(start.getX());
            int y0 = (int) Math.round(start.getY());
            int x1 = (int) Math.round(end.getX());
            int y1 = (int) Math.round(end.getY());

            int dx = Math.abs(x1 - x0);
            int dy = Math.abs(y1 - y0);

            int sx = x0 < x1 ? 1 : -1;
            int sy = y0 < y1 ? 1 : -1;
            int err = dx - dy;

            while (true) {
                pixelWriter.setColor(x0, y0, Color.RED);
                if (x0 == x1 && y0 == y1) {
                    break;
                }
                int e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    x0 += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    y0 += sy;
                }
            }
        }
    }

    public static void drawDDALine(GraphicsContext graphicsContext, ArrayList<Point2D> interpolatedPoints) {
        PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
            Point2D start = interpolatedPoints.get(i);
            Point2D end = interpolatedPoints.get(i + 1);
            double dx = end.getX() - start.getX();
            double dy = end.getY() - start.getY();
            double steps = Math.max(Math.abs(dx), Math.abs(dy));

            double xIncrement = dx / steps;
            double yIncrement = dy / steps;

            double x = start.getX();
            double y = start.getY();

            for (int j = 0; j <= steps; j++) {
                pixelWriter.setColor((int) Math.round(x), (int) Math.round(y), Color.BLUE);
                x += xIncrement;
                y += yIncrement;
            }
        }
    }

    public static void drawStokeLine(GraphicsContext graphicsContext, ArrayList<Point2D> interpolatedPoints) {
        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
            Point2D start = interpolatedPoints.get(i);
            Point2D end = interpolatedPoints.get(i + 1);
            graphicsContext.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
    }
}
