package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Ground extends WorldObject {
    private static final int priority = 3;
    private static final int lowerY = 350;
    private static final int upperY = 395;
    private static final Color grassColor = new Color(13, 184, 53);
    private static final Color grassBladeColor = new Color(54, 84, 52);
    private static final Color dirtColor = new Color(124, 79, 51);
    private static final Color roadColor = new Color(231, 129, 74);
    private static final Color tilesColor = new Color(158, 79, 61);
    private static final Color pebbleColor = new Color(82, 82, 82, 255);

    public Ground() {
        super(priority);
    }

    public int getLowerY() {
        return lowerY;
    }


    public int getUpperY() {
        return upperY;
    }


    public void draw(Graphics2D g) {
        drawRoadAndTiles(g, 0);
        drawRoadAndTiles(g, 267 * 2);
        drawGrass(g, 267);
    }

    public void drawGrass(Graphics2D g, int startingPoint) {
        g.setColor(grassColor);
        g.fillRect(startingPoint, lowerY, 267, upperY - lowerY);
        g.setColor(dirtColor);
        g.fillRect(startingPoint, lowerY + (upperY - lowerY) / 5 * 4, 267, (upperY - lowerY) / 5);
        drawGrassBlock(g, startingPoint, 40);
        drawPebble(g, startingPoint);
    }

    public void drawGrassBlock(Graphics2D g, int startingPoint, int delta) {
        g.setColor(grassBladeColor);
        int blockWidth = 20;
        int blockHeight = 30;
        int xChanging = 25;
        int yChanging = 8;
        for (int i = 0; i < 11; i++) {
            int currBlockHeight = blockHeight;
            int xGrowthPoint = startingPoint + xChanging * i;
            int yGrowthPoint = lowerY + 20 - blockHeight;
            if (i % 3 == 2) {
                yGrowthPoint -= yChanging;
            } else if (i % 3 == 1) {
                yGrowthPoint += yChanging;
            }
            int startAngle = 180;
            int arcAngle = 180;
            int currentY = yGrowthPoint;
            for (int j = 0; j < 4; j++) {
                g.drawArc(xGrowthPoint, currentY, blockWidth, currBlockHeight, startAngle, arcAngle);
                currBlockHeight *= 2;
                currentY -= currBlockHeight / 2;
                startAngle += delta;
                arcAngle -= delta * 2;
            }
            g.drawLine(xGrowthPoint + blockWidth / 2, yGrowthPoint + 20, xGrowthPoint + blockWidth / 2, yGrowthPoint + 30);
        }
    }

    public void drawRoadAndTiles(Graphics2D g, int startingPoint) {
        g.setColor(roadColor);
        g.fillRect(startingPoint, lowerY, 267, upperY - lowerY);
        drawPebble(g, startingPoint);
        drawTiles(g, startingPoint);
    }

    public void drawTiles(Graphics2D g, int startingPoint) {
        int numberForNaturalLook = (upperY - lowerY) / 7;
        startingPoint += numberForNaturalLook;
        double rotationAngle = Math.toRadians(-45);
        int tileY = (upperY + lowerY) / 2 + numberForNaturalLook;
        g.rotate(rotationAngle);
        for (int i = 0; i < 7; i++) {
            int xAfterRotation = (int) (startingPoint * Math.cos(rotationAngle) + tileY * Math.sin(rotationAngle));
            int yAfterRotation = (int) (tileY * Math.cos(rotationAngle) - startingPoint * Math.sin(rotationAngle));
            Rectangle currTile = new Rectangle(xAfterRotation, yAfterRotation, (upperY - lowerY) - numberForNaturalLook, (upperY - lowerY) / 2);
            g.setColor(tilesColor);
            g.fill(currTile);
            g.setColor(Color.black);
            g.draw(currTile);
            startingPoint += (int) (267 / 7.5);
        }
        g.rotate(-rotationAngle);
    }

    public void drawPebble(Graphics2D g, int startingPoint) {
        int pebbleRadius = 3;
        g.setColor(pebbleColor);
        int xChanging = 30;
        int yChanging = 10;
        for (int i = 1; i < 9; i++) {
            int randomX = startingPoint + xChanging * i;
            int randomY = lowerY + 20;
            if (i % 3 == 0) {
                randomY += yChanging;
            } else if (i % 3 == 2) {
                randomY -= yChanging;
            }
            g.fillOval(randomX - pebbleRadius, randomY - pebbleRadius, pebbleRadius * 2, pebbleRadius * 2);
        }
    }
}
