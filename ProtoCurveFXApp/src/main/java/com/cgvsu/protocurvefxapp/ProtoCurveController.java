package com.cgvsu.protocurvefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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

        canvas.setOnMousePressed(event -> {
            draggedPoint = findPointNearby(event.getX(), event.getY());
        });

        canvas.setOnMouseDragged(event -> {
            if (draggedPoint != null) {
                LagrangeInterpolation.points.remove(draggedPoint);
                draggedPoint = new Point2D(event.getX(), event.getY());
                LagrangeInterpolation.points.add(draggedPoint);
            }
            LagrangeInterpolation.redraw(canvas.getGraphicsContext2D(), canvas);
        });

        canvas.setOnMouseReleased(event -> {
            if (draggedPoint != null) {
                LagrangeInterpolation.points.remove(draggedPoint);
                createPointWithXCheck(event);
            }
            if (findPointNearby(event.getX(), event.getY()) == null) {
//                createNewPoint(canvas.getGraphicsContext2D(), event);
                createPointWithXCheck(event);
            }
            LagrangeInterpolation.redraw(canvas.getGraphicsContext2D(), canvas);
            draggedPoint = null;
        });
    }

    private void createNewPoint(GraphicsContext graphicsContext, MouseEvent event) {
        LagrangeInterpolation.points.add(new Point2D(event.getX(), event.getY()));
        LagrangeInterpolation.redraw(graphicsContext, canvas);
    }

    private void createNewPointByXY(GraphicsContext graphicsContext, double x, double y) {
        LagrangeInterpolation.points.add(new Point2D(x, y));
        LagrangeInterpolation.redraw(graphicsContext, canvas);
    }

    private Point2D findPointNearby(double x, double y) {
        for (Point2D point : LagrangeInterpolation.points) {
            if (Math.hypot(point.getX() - x, point.getY() - y) <= POINT_RADIUS) {
                return point;
            }
        }
        return null;
    }
    private boolean findPointOnTheSameX(double x) {
        for (Point2D point : LagrangeInterpolation.points) {
            if (x == point.getX()) {
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