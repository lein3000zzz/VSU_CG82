package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.DrawPanel;
import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;

public class Background extends WorldObject {
    private static final int priority = 1;
    private int lowerY;
    private boolean mountainsFlag;
    private boolean sunFlag;
    private static final Color skyColor = new Color(202, 222, 138);
    private static final Color mountainColor = new Color(101, 83, 63);

    public int getLowerY() {
        return lowerY;
    }

    public void setLowerY(int lowerY) {
        this.lowerY = lowerY;
    }

    public boolean isMountainsFlag() {
        return mountainsFlag;
    }

    public void setMountainsFlag(boolean mountainsFlag) {
        this.mountainsFlag = mountainsFlag;
    }

    public Background(int lowerY, boolean mountainsFlag, boolean sunFlag) {
        super(priority);
        this.lowerY = lowerY;
        this.mountainsFlag = mountainsFlag;
        this.sunFlag = sunFlag;
    }

    public void draw(Graphics2D g) {
        g.setColor(skyColor);
        g.fillRect(0, 0, 1920, lowerY);
        if (sunFlag) {
            Sun sun = new Sun(630, 200, 35, 20, 25);
            sun.draw(g);
        }
        if (mountainsFlag) {
            drawMountains(g);
        }
    }

    public void drawMountains(Graphics2D g) {
        g.setColor(mountainColor);
        int[] x1 = {200, 500, 700};
        int[] y1 = {lowerY, 100, lowerY};
        int[] x2 = {400, 700, 1000};
        int[] y2 = {lowerY, 200, lowerY};
        g.fillPolygon(x1,y1,3);
        g.setColor(Color.black);
        g.drawPolygon(x1,y1,3);
        g.setColor(mountainColor);
        g.fillPolygon(x2,y2,3);
        g.setColor(Color.black);
        g.drawPolygon(x2,y2,3);
    }

    public boolean isSunFlag() {
        return sunFlag;
    }

    public void setSunFlag(boolean sunFlag) {
        this.sunFlag = sunFlag;
    }
}
