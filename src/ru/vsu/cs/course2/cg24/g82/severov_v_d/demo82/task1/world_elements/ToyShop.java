package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import java.awt.*;

public class ToyShop extends Building {
    private static final int WALL_HEIGHT = 80;
    private static final int BASEMENT_HEIGHT = 15;
    private static final int ROOF_HEIGHT = 40;
    private static final int WINDOW_HEIGHT = WALL_HEIGHT / 5 * 2;
    private static final int WIDTH = 200;
    private static final Color[] TOY_COLORS = {Color.ORANGE, Color.BLUE, Color.YELLOW, Color.RED, Color.CYAN, Color.MAGENTA};
    private static final Color[] SIGN_COLORS = {new Color(146, 218, 162), new Color(255, 77, 100), new Color(255, 128, 43)};
    private static final Color[] SHOP_COLORS = {new Color(99, 160, 53), new Color(240, 170, 55), new Color(79, 154, 155), new Color(255, 77, 100)};
    private static final Color windowBarsColor = new Color(120, 27, 67);
    private static final Color toyPodiumColor = new Color(104, 104, 129);
    private static final Color roofColor = new Color(125, 52, 43);
    private static final Color windowColor = new Color(64, 68, 97);
    private static final Color basementColor = new Color(119, 60, 53);
    public ToyShop(int upperY, int startingX) {
        super(upperY, startingX);
    }

    public void draw(Graphics2D g) {
        drawBasement(g);
        drawMainWall(g, getStartingX() + 15);
        drawWindow(g, getStartingX() + 35);
        drawRoof(g);
    }

    public void drawBasement(Graphics2D g) {
        g.setColor(basementColor);
        g.fillRect(getStartingX(), getUpperY() - BASEMENT_HEIGHT, WIDTH, BASEMENT_HEIGHT);
    }

    public void drawMainWall(Graphics2D g, int startingX) {
        int segmentWidth = 10;
        for (int i = 0; i < (WIDTH - 30) / segmentWidth; i++) {
            g.setColor(SHOP_COLORS[i % 4]);
            g.fillRect(startingX, getUpperY() - BASEMENT_HEIGHT - WALL_HEIGHT, segmentWidth, WALL_HEIGHT);
            startingX += segmentWidth;
        }
    }

    public void drawWindow(Graphics2D g, int startingX) {
        g.setColor(windowColor);
        g.fillRect(startingX, getUpperY() - BASEMENT_HEIGHT - WINDOW_HEIGHT, 130, WINDOW_HEIGHT);
        g.setColor(windowBarsColor);
        g.setStroke(new BasicStroke(2));
        g.drawRect(startingX, getUpperY() - BASEMENT_HEIGHT - WINDOW_HEIGHT, 130, WINDOW_HEIGHT);
        drawToys(g, startingX + 15);
    }

    public void drawToys(Graphics2D g, int startingX) {
        int podiumY = getUpperY() - BASEMENT_HEIGHT - WINDOW_HEIGHT / 3;
        int numberOfToys = 10;
        g.setColor(toyPodiumColor);
        g.drawLine(startingX, podiumY, startingX + 100, podiumY);
        int toyRadius = 2;
        for (int i = 0; i < 100 / numberOfToys; i++) {
            g.setColor(TOY_COLORS[i % 6]);
            int toyX = startingX + 5 + numberOfToys * i;
            int toyY = podiumY - toyRadius / 2;
            g.fillOval(toyX - toyRadius, toyY - toyRadius, toyRadius * 2, toyRadius * 2);
        }
    }

    public void drawRoof(Graphics2D g) {
        int startingY = getUpperY() - BASEMENT_HEIGHT - WALL_HEIGHT;
        int[] x1 = {getStartingX(), getStartingX() + 20, getStartingX() + WIDTH - 20, getStartingX() + WIDTH};
        int[] y1 = {startingY, startingY - ROOF_HEIGHT, startingY - ROOF_HEIGHT, startingY};
        g.setColor(roofColor);
        g.fillPolygon(x1,y1,4);
        drawToySign(g, getStartingX() + 50, startingY);
    }

    public void drawToySign(Graphics2D g, int startingX, int startingY) {
        g.setColor(SIGN_COLORS[0]);
        g.fillRoundRect(startingX, startingY - ROOF_HEIGHT + 5, 30, 7, 20, 20);
        g.fillRoundRect(startingX + 30 / 2 - 5, startingY - ROOF_HEIGHT + 5, 10, 30, 20, 20);
        g.setColor(SIGN_COLORS[1]);
        g.setStroke(new BasicStroke(7));
        g.drawOval(startingX + 40, startingY - ROOF_HEIGHT + 7, 25, 27);
        g.setColor(SIGN_COLORS[2]);
        g.drawLine(startingX + 80, startingY - ROOF_HEIGHT + 5 + 3, startingX + 90, startingY - ROOF_HEIGHT + 15 + 3);
        g.drawLine(startingX + 80, startingY - ROOF_HEIGHT + 30 + 3, startingX + 100, startingY - ROOF_HEIGHT + 5 + 3);
    }
}
