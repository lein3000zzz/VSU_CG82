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
                createNewPoint(canvas.getGraphicsContext2D(), event);
            }
            if (findPointNearby(event.getX(), event.getY()) == null) {
                createNewPoint(canvas.getGraphicsContext2D(), event);
            }
            LagrangeInterpolation.redraw(canvas.getGraphicsContext2D(), canvas);
            draggedPoint = null;
        });
    }

    private void createNewPoint(GraphicsContext graphicsContext, MouseEvent event) {
        LagrangeInterpolation.points.add(new Point2D(event.getX(), event.getY()));
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
}