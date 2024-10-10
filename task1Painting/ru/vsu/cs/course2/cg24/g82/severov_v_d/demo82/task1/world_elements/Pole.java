package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;

public class Pole extends WorldObject {
    private final int HEIGHT = 150;
    private static int priority = 5;
    private int upperY;
    private int bindingX;
    private static final Color mainPoleColor = new Color(24, 13, 22);

    public Pole(int upperY, int bindingX) {
        super(priority);
        this.upperY = upperY;
        this.bindingX = bindingX;
    }

    public void draw(Graphics2D g) {
        g.setColor(mainPoleColor);
        int strokeWidth = 8;
        for (int i = 3; i >= 1; i--) {
            g.setStroke(new BasicStroke(strokeWidth));
            g.drawLine(bindingX, upperY - 4, bindingX, upperY - HEIGHT / i);
            strokeWidth -= 2;
        }
        drawBulbs(g);
    }
    public void drawBulbs(Graphics2D g) {
        Paint brush = new RadialGradientPaint(bindingX, upperY - HEIGHT - (float) HEIGHT / 15, 25, new float[] {0, 0.7f, 1}, new Color[] {Color.YELLOW, Color.WHITE, Color.YELLOW});
        g.setPaint(brush);
        g.fillArc(bindingX, upperY - HEIGHT - HEIGHT / 15, 25, HEIGHT / 15, 250, 120);
        g.fillArc(bindingX - 25, upperY - HEIGHT - HEIGHT / 15, 25, HEIGHT / 15, 160, 130);
        g.setColor(mainPoleColor);
        g.drawLine(bindingX, upperY - HEIGHT, bindingX + 25, upperY - HEIGHT - HEIGHT / 15);
        g.drawLine(bindingX, upperY - HEIGHT, bindingX - 25, upperY - HEIGHT - HEIGHT / 15);
    }
    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
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
}
