package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Water extends WorldObject {
    private static final int priority = 2;
    private static final Random rnd = new Random();
    private int waterFlowIntensity; // больше => ближе к берегу, меньше => больше глади
    private int lowerY;
    private static final int FLOW_LENGTH = rnd.nextInt(15, 25);
    private static final Color waterColor = new Color(1, 148, 178);
    List<int[]> randomWaterPoints;

//    public void draw(Graphics2D g) {
//        g.setColor(waterColor);
//        g.fillRect(0, lowerY, 1920, 1080);
//        g.setColor(Color.black);
//        for (int j = 1; j < 12; j++) {
//            int x = -1200;
//            int y = lowerY;
//            for (int i = 1; i <= waterFlowIntensity; i++) {
//                x += 800 / waterFlowIntensity + j * 50;
//                y += (600 - lowerY) / waterFlowIntensity;
//                g.drawLine(x, y, x + FLOW_LENGTH, y);
//            }
//        }
//    }

    public void draw(Graphics2D g) {
        g.setColor(waterColor);
        g.fillRect(0, lowerY, 1920, 1080);
        g.setColor(Color.black);
        for (int j = 0; j < waterFlowIntensity; j++) {
            int x = randomWaterPoints.get(j)[0];
            int y = randomWaterPoints.get(j)[1];
            g.drawLine(x, y, x + FLOW_LENGTH, y);
        }
    }

    public int getWaterFlowIntensity() {
        return waterFlowIntensity;
    }

    public void setWaterFlowIntensity(int waterFlowIntensity) {
        this.waterFlowIntensity = waterFlowIntensity;
    }

    public Water(int waterFlowIntensity, int lowerY) {
        super(priority);
        this.waterFlowIntensity = waterFlowIntensity;
        this.lowerY = lowerY;
        this.randomWaterPoints = new ArrayList<int[]>();
        for (int j = 0; j < waterFlowIntensity; j++) {
            int x = rnd.nextInt(0, 800);
            int y = rnd.nextInt(lowerY, 600);
            randomWaterPoints.add(new int[]{x, y});
        }
    }

    public int getLowerY() {
        return lowerY;
    }

    public void setLowerY(int lowerY) {
        this.lowerY = lowerY;
    }
}
