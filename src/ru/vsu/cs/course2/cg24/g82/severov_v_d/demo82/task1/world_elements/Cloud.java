package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;

public class Cloud extends WorldObject {
    private static final int SIZE_ONE = 100;
    private static final int SIZE_TWO = 120;
    private static final int SIZE_THREE = 150;
    private static final int priority = 7;
    private int speed;
    private int cloudType;
    private int currentX;
    private int currentY;
    private static final Color cloudMainColor = new Color(180, 171, 161);
    private static final Color cloudSecondaryColor = new Color(107, 97, 89);

    public Cloud(int speed, int cloudType, int currentX, int currentY) {
        super(priority);
        this.speed = speed;
        this.cloudType = cloudType % 3 + 1;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    public void draw(Graphics2D g) {
        g.setColor(cloudMainColor);
        g.setStroke(new BasicStroke(1));
        switch(cloudType) {
            case (1) -> drawTypeOne(g);
            case (2) -> drawTypeTwo(g);
            case (3) -> drawTypeThree(g);
        }
    }

    public void drawTypeOne(Graphics2D g) {
        for (int i = 0; i < 5; i++) {
            g.fillOval(currentX + SIZE_ONE / 4 * i, currentY, (int) (SIZE_ONE / 2.5f), SIZE_ONE / 3);
        }
    }

    public void drawTypeTwo(Graphics2D g) {
        for (int i = 0; i < 7; i++) {
            if (i % 3 == 0)
                g.setColor(cloudSecondaryColor);
            else
                g.setColor(cloudMainColor);
            g.fillOval(currentX + SIZE_TWO / 6 * i, currentY, SIZE_TWO / 3, SIZE_TWO / 4);
        }
    }

    public void drawTypeThree(Graphics2D g) {
        g.setColor(cloudSecondaryColor);
        for (int i = 0; i < 3; i++) {
            g.fillOval(currentX + SIZE_THREE / 6 * i, currentY, SIZE_THREE / 3, SIZE_THREE / 4);
        }
    }

    public void moveCloud() {
        setCurrentX(currentX - speed);
        switch(cloudType) {
            case (1) -> checkRedrawCloudOne();
            case (2) -> checkRedrawCloudTwo();
            case (3) -> checkRedrawCloudThree();
        }
    }

    public void checkRedrawCloudOne() {
        if (currentX < -(int) (SIZE_ONE / 2.5f) * 5) {
            setCurrentX(800);
        }
    }

    public void checkRedrawCloudTwo() {
        if (currentX < -SIZE_TWO / 3 * 7) {
            setCurrentX(800);
        }
    }

    public void checkRedrawCloudThree() {
        if (currentX < -SIZE_TWO / 3 * 3) {
            setCurrentX(800);
        }
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int getCloudType() {
        return cloudType;
    }

    public void setCloudType(int cloudType) {
        this.cloudType = cloudType;
    }

    public int getCurrentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
