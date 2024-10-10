package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;

public class Sun extends WorldObject {
    private static final int priority = 6;
    private int x;
    private int y;
    private int r;
    private int beamCount;
    private int beamLength;
    private static final Color sunColor = new Color(255, 128, 43);

    public Sun(int x, int y, int r, int beamCount, int beamLength) {
        super(priority);
        this.x = x;
        this.y = y;
        this.r = r;
        this.beamCount = beamCount;
        this.beamLength = beamLength;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getBeamCount() {
        return beamCount;
    }

    public void setBeamCount(int beamCount) {
        this.beamCount = beamCount;
    }

    public int getBeamLength() {
        return beamLength;
    }

    public void setBeamLength(int beamLength) {
        this.beamLength = beamLength;
    }

    public void draw(Graphics2D g) {
        Paint brush = new RadialGradientPaint(x, y, r + beamLength, new float[] {0, 0.1f, 1}, new Color[] {sunColor, Color.WHITE, sunColor});
        g.setPaint(brush);
        g.fillOval(x - r, y - r, r * 2, r * 2);
        double deltaAlpha = 2 * Math.PI / beamCount;
        for (int i = 0; i < beamCount; i++) {
            double alpha = deltaAlpha * i;
            double x1 = x + r * Math.cos(alpha);
            double y1 = y + r * Math.sin(alpha);
            double x2 = x + (r + beamLength) * Math.cos(alpha);
            double y2 = y + (r + beamLength) * Math.sin(alpha);
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }
    }
}
