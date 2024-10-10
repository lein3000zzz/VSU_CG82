package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;
import java.util.Random;

public class Balloons extends WorldObject {
    private static final Random rnd = new Random();
    private static final int priority = 8;
    private static int state;
    private int upperY;
    private int bindingX;
    private int shift;
    private final int balloonsRadius = 5;
    private int bindingWidth = 8;
    private int bindingHeight = 8;

    public Balloons(int upperY, int bindingX) {
        super(priority);
        this.upperY = upperY;
        this.bindingX = bindingX;
    }

    private static final Color bindingColor = new Color(163, 157, 164);
    private static final Color stringsColor = new Color(223, 210, 196);
    private static final Color[] BALLOONS_COLORS = {Color.ORANGE, Color.BLUE, Color.YELLOW, Color.RED, Color.CYAN, Color.MAGENTA, new Color(255, 77, 100), new Color(255, 128, 43), new Color(99, 160, 53), new Color(240, 170, 55), new Color(79, 154, 155), new Color(255, 77, 100)};

    public void draw(Graphics2D g) {
        g.setColor(stringsColor);
        g.setStroke(new BasicStroke(2));
        g.drawLine(bindingX + bindingWidth / 2, upperY - bindingHeight, bindingX + bindingWidth / 2, upperY - 20);
        g.setColor(bindingColor);
        g.fillRoundRect(bindingX, upperY - bindingHeight, bindingWidth, bindingHeight, 3, 3);
        drawState(g, shift);
    }

    public void drawState(Graphics2D g, int shift) {
        g.setStroke(new BasicStroke(0.5f));
        int spread = 0;
        for (int i = 0; i < 7; i++) {
            if (i % 2 == 0) {
                g.setColor(bindingColor);
                g.drawLine(bindingX + bindingWidth / 2, upperY - 20, bindingX + spread + shift, upperY - 20 - 30 + spread + shift);
                g.setColor(BALLOONS_COLORS[i % 7]);
                g.fillOval(bindingX + spread + shift - balloonsRadius, upperY - 20 - 30 + spread + shift - balloonsRadius, balloonsRadius * 2, balloonsRadius * 2);
                g.setColor(Color.BLACK);
                g.drawOval(bindingX + spread + shift - balloonsRadius, upperY - 20 - 30 + spread + shift - balloonsRadius, balloonsRadius * 2, balloonsRadius * 2);
            }
            else {
                g.setColor(bindingColor);
                g.drawLine(bindingX + bindingWidth / 2, upperY - 20, bindingX - spread + shift, upperY - 20 - 30 + spread + shift);
                g.setColor(BALLOONS_COLORS[i % 7]);
                g.fillOval(bindingX - spread + shift - balloonsRadius, upperY - 20 - 30 + spread + shift - balloonsRadius, balloonsRadius * 2, balloonsRadius * 2);
                g.setColor(Color.BLACK);
                g.drawOval(bindingX - spread + shift - balloonsRadius, upperY - 20 - 30 + spread + shift - balloonsRadius, balloonsRadius * 2, balloonsRadius * 2);
            }
            spread += 2;
        }
    }
    public void changeState() {
        Balloons.state++;
        Balloons.state %= 5;
        switch (state) {
            case (0) -> shift = rnd.nextInt(-2, 2);
            case (1) -> shift = rnd.nextInt(-5, -1);
            case (2) -> shift = rnd.nextInt(-1, 3);
            case (3) -> shift = rnd.nextInt(2, 6);
            case (4) -> shift = rnd.nextInt(-4, 0);
        }
    }
    public int getUpperY() {
        return upperY;
    }

    public void setUpperY(int upperY) {
        this.upperY = upperY;
    }

    public int getBindingX() {
        return bindingX;
    }

    public void setBindingX(int bindingX) {
        this.bindingX = bindingX;
    }

    public int getBindingWidth() {
        return bindingWidth;
    }

    public void setBindingWidth(int bindingWidth) {
        this.bindingWidth = bindingWidth;
    }

    public int getBindingHeight() {
        return bindingHeight;
    }

    public void setBindingHeight(int bindingHeight) {
        this.bindingHeight = bindingHeight;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
