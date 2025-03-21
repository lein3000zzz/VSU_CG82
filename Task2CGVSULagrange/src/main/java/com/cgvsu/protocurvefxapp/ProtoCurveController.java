package com.cgvsu.protocurvefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    final int POINT_RADIUS = 3;
    private Point2D draggedPoint = null;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        canvas.getGraphicsContext2D().setImageSmoothing(true);

        canvas.setOnMousePressed(event -> {
            draggedPoint = findPointNearby(event.getX(), event.getY());
        });

        canvas.setOnMouseDragged(event -> {
            if (draggedPoint != null) {
                points.remove(draggedPoint);
                draggedPoint = new Point2D(event.getX(), event.getY());
                points.add(draggedPoint);
            }
            redraw(canvas.getGraphicsContext2D());
        });

        canvas.setOnMouseReleased(event -> {
            if (draggedPoint != null) {
                points.remove(draggedPoint);
                createPointWithXCheck(event);
            }
            if (findPointNearby(event.getX(), event.getY()) == null) {
                createPointWithXCheck(event);
            }
            redraw(canvas.getGraphicsContext2D());
            draggedPoint = null;
        });
    }

    private static final double MIN_DISTANCE_HOLDER = 0.3;
    public static ArrayList<Point2D> points = new ArrayList<>();

    public void redraw(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        final int POINT_RADIUS = 3;
        for (Point2D point : points) {
            graphicsContext.fillOval(
                    point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        if (points.size() > 1) {
            graphicsContext.setLineWidth(0.75);
            Point2D prevXY = null;
            for (double x = 0; x < canvas.getWidth(); x += 0.5) {
                double y = LagrangeInterpolation.interpolate(x, points);
                if (findPointOnTheSameX(x))
                    y = getPointYFromTheSameX(x);
                y = checkYReq(y);
                if (prevXY != null) {
                    graphicsContext.strokeLine(x, y, prevXY.getX(), prevXY.getY());
                }
//                pixelWriter.setColor((int) Math.round(x), (int) Math.round(y), Color.RED);
//                System.out.println(prevXY);
//                System.out.println("X " + x + " Y " + y);
                prevXY = new Point2D(x, y);
            }
        }
//        System.out.println(points.get(0));
//        System.out.println(points);
    }

    private void createNewPoint(GraphicsContext graphicsContext, MouseEvent event) {
        points.add(new Point2D(event.getX(), event.getY()));
        redraw(graphicsContext);
    }

    private double checkYReq(double y) {
        if (y < 0)
            return -1;
        else if (y > canvas.getHeight())
            return canvas.getHeight() + 1;
        else
            return y;
    }

    private void createNewPointByXY(GraphicsContext graphicsContext, double x, double y) {
        points.add(new Point2D(x, y));
        redraw(graphicsContext);
    }
    private Point2D findPointNearby(double x, double y) {
        for (Point2D point : points) {
            if (Math.hypot(point.getX() - x, point.getY() - y) <= POINT_RADIUS) {
                return point;
            }
        }
        return null;
    }
    private boolean findPointOnTheSameX(double x) {
        for (Point2D point : points) {
            if (x == point.getX()) {
                return true;
            }
        }
        return false;
    }

    private double getPointYFromTheSameX(double x) {
        for (Point2D point : points) {
            if (Math.abs(x - point.getX()) < 1E-5) {
                return point.getY();
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    private boolean findPointOnTheSameY(double y) {
        for (Point2D point : points) {
            if (y == point.getY()) {
                return true;
            }
        }
        return false;
    }

    private void createPointWithXCheck(MouseEvent event) {
        if (!findPointOnTheSameX(event.getX()))
            createNewPoint(canvas.getGraphicsContext2D(), event);
        else {
            for (int x = 1; x < canvas.getWidth(); x++) {
                if (!findPointOnTheSameX(event.getX() + x)) {
                    createNewPointByXY(canvas.getGraphicsContext2D(), event.getX() + x, event.getY());
                    break;
                } else if ((!findPointOnTheSameX(event.getX() - x))) {
                    createNewPointByXY(canvas.getGraphicsContext2D(), event.getX() - x, event.getY());
                    break;
                }
            }
        }
    }
}