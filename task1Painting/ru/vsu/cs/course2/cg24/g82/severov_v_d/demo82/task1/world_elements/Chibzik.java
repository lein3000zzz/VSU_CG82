package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class Chibzik extends WorldObject {
    private static final Random rnd = new Random();
    private static final int priority = 10;
    private boolean canMove = true;
    private boolean reachedToys = false;
    private float thickness;
    private int currentX;
    private int upperY;
    private int state;
    private int stopPointSpread;
    private static final Color chibzikColor = new Color(24, 22, 35);

    public Chibzik(int upperY, int currentX, int thickness) {
        super(priority);
        this.thickness = thickness;
        this.currentX = currentX;
        this.upperY = upperY;
        this.stopPointSpread = rnd.nextInt(-15, 15);
    }

    public int getStopPointSpread() {
        return stopPointSpread;
    }

    public void setStopPointSpread(int stopPointSpread) {
        this.stopPointSpread = stopPointSpread;
    }

    private interface LegDrawer{
        void draw(Graphics2D g);
    }

    private static class LegDrawerA implements LegDrawer {
        private Chibzik ch;

        public LegDrawerA(Chibzik ch) {
            this.ch = ch;
        }

        @Override
        public void draw(Graphics2D g) {
            ch.drawLegsStateTogether(g);
        }
    }

    private static class LegDrawerB implements LegDrawer {
        private Chibzik ch;

        public LegDrawerB(Chibzik ch) {
            this.ch = ch;
        }

        @Override
        public void draw(Graphics2D g) {
            ch.drawLegsStateApart(g);
        }
    }

    private LegDrawer[] ld = new LegDrawer[] {new LegDrawerA(this), new LegDrawerB(this)};

    public void draw(Graphics2D g) {
        drawBody(g);
        drawHands(g);
        if (canMove) {
            ld[state].draw(g);
        } else {
            drawLegsStateTogether(g);
        }
    }

    private void drawHands(Graphics2D g) {
        g.setColor(chibzikColor);
        g.setStroke(new BasicStroke(thickness / 2));
        g.drawLine(currentX + 2, upperY - 30, currentX + 5, upperY - 16);
        g.drawLine(currentX - 2, upperY - 30, currentX - 5, upperY - 16);
    }

    public void drawBody(Graphics2D g) {
        g.setColor(chibzikColor);
        g.setStroke(new BasicStroke(thickness));
        g.drawLine(currentX, upperY - 15, currentX, upperY - 35);
        g.setColor(Color.WHITE);
        g.fillOval(currentX + 1, upperY - 33, 2, 2);
        g.fillOval(currentX - 2, upperY - 33, 2, 2);
    }

    public void drawLegsStateTogether(Graphics2D g) {
        g.setColor(chibzikColor);
        g.setStroke(new BasicStroke(thickness / 2));
        g.drawLine(currentX + 2, upperY, currentX + 2, upperY - 16);
        g.drawLine(currentX - 2, upperY, currentX - 2, upperY - 16);
    }

    public void drawLegsStateApart(Graphics2D g) {
        g.setColor(chibzikColor);
        g.setStroke(new BasicStroke(thickness / 2));
        g.drawLine(currentX + 7, upperY, currentX + 2, upperY - 16);
        g.drawLine(currentX - 7, upperY, currentX - 2, upperY - 16);
    }

    public void changeState() {
        state++;
        state %= 2;
    }

    public void moveChibzik(int xChanging) {
        if (canMove) {
            if (reachedToys) {
                setCurrentX(currentX - xChanging);
            } else {
                setCurrentX(currentX + xChanging);
            }
        }
    }

    public void disableMovement(int millis) {
        canMove = false;
        Timer pauseTimer = new Timer(millis, new AbstractAction() {;
            @Override
            public void actionPerformed(ActionEvent e) {
                canMove = true;
            }
        });
        pauseTimer.setRepeats(false);
        pauseTimer.start();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public int getCurrentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getUpperY() {
        return upperY;
    }

    public void setUpperY(int upperY) {
        this.upperY = upperY;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public boolean isReachedToys() {
        return reachedToys;
    }

    public void setReachedToys(boolean reachedToys) {
        this.reachedToys = reachedToys;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
