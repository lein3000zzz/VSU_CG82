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

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    final int POINT_RADIUS = 3;
    private Point2D draggedPoint = null;
    public ArrayList<Point2D> points = new ArrayList<>();
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
            redraw(canvas.getGraphicsContext2D(), points);
        });

        canvas.setOnMouseReleased(event -> {
            if (draggedPoint != null) {
                points.remove(draggedPoint);
                createPointWithXCheck(event, points);
            }
            if (findPointNearby(event.getX(), event.getY()) == null) {
                createPointWithXCheck(event, points);
            }
            redraw(canvas.getGraphicsContext2D(), points);
            draggedPoint = null;
        });
    }

    public void redraw(GraphicsContext graphicsContext, ArrayList<Point2D> points) {
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        drawPoints(graphicsContext, points);
        ArrayList<Point2D> interpolatedPoints = LagrangeInterpolation.calculateLagrangePoints(points, 0, graphicsContext.getCanvas().getWidth(), 0, graphicsContext.getCanvas().getHeight());
        DrawLineUtils.drawBresenhamLine(graphicsContext, interpolatedPoints);
//        DrawLineUtils.drawDDALine(graphicsContext, interpolatedPoints);
//        DrawLineUtils.drawStokeLine(graphicsContext, interpolatedPoints);
    }

    private void drawPoints(GraphicsContext graphicsContext, ArrayList<Point2D> points) {
        final int POINT_RADIUS = 3;
        for (Point2D point : points) {
            graphicsContext.fillOval(
                    point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }
    }

    private void createPointWithXCheck(MouseEvent event, ArrayList<Point2D> points) {
        if (!LagrangeInterpolation.foundPointOnTheSameX(event.getX(), points))
            createNewPoint(canvas.getGraphicsContext2D(), event);
        else {
            for (int x = 1; x < canvas.getWidth(); x++) {
                if (!LagrangeInterpolation.foundPointOnTheSameX(event.getX() + x, points)) {
                    createNewPointByXY(canvas.getGraphicsContext2D(), event.getX() + x, event.getY());
                    break;
                } else if (!LagrangeInterpolation.foundPointOnTheSameX(event.getX() - x, points)) {
                    createNewPointByXY(canvas.getGraphicsContext2D(), event.getX() - x, event.getY());
                    break;
                }
            }
        }
    }
    private void createNewPoint(GraphicsContext graphicsContext, MouseEvent event) {
        points.add(new Point2D(event.getX(), event.getY()));
        redraw(graphicsContext, points);
    }

    private void createNewPointByXY(GraphicsContext graphicsContext, double x, double y) {
        points.add(new Point2D(x, y));
        redraw(graphicsContext, points);
    }

    private Point2D findPointNearby(double x, double y) {
        for (Point2D point : points) {
            if (Math.hypot(point.getX() - x, point.getY() - y) <= POINT_RADIUS) {
                return point;
            }
        }
        return null;
    }
}