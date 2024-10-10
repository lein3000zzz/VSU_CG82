package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import java.awt.*;

public class ThreeStoreyShop extends Building {
    private String signText;
    private static final Color buildingColor = new Color(221, 166, 129);
    private static final Color buildingWindowSurroundColor = new Color(177, 91, 62);
    private static final Color windowColor = new Color(79, 80, 94);
    private static final Color windowBarsColor = new Color(201, 189, 175);
    private static final Color roofColor = new Color(103, 50, 42);
    private static final Color signColor = new Color(219, 203, 183);
    private static final Color detailColor = new Color(255, 198, 94);


    public void draw(Graphics2D g) {
        drawMainPart(g);
        drawBuildingWidowsRow(g, 33, 80, getStartingX() + 18, getUpperY() - 250 + 10);
        drawBuildingWidowsRow(g, 33, 80, getStartingX() + 18, getUpperY() - 250 + 10 + 100);
        drawSign(g);
        drawWindowAndDoor(g);
    }

    public void drawMainPart(Graphics2D g) {
        g.setColor(buildingColor);
        g.fillRect(getStartingX(), getUpperY() - 250, 150, 250);
        g.setColor(roofColor);
        g.fillRoundRect(getStartingX(), getUpperY() - 250 - 5, 150, 10, 7, 7);
        g.setColor(buildingWindowSurroundColor);
        g.fillRect(getStartingX() + 15, getUpperY() - 250 + 5, 120, 180);
        g.setColor(detailColor);
        g.fillRoundRect(getStartingX() + 20, getUpperY() - 250 + 80, 110, 10, 7, 7);
    }

    public void drawBuildingWidowsRow(Graphics2D g, int width, int height, int startingX, int startingY) {
        for (int i = 0; i < 3; i++) {
            g.setStroke(new BasicStroke(1));
            g.setColor(windowColor);
            g.fillArc(startingX, startingY, width, height, 0, 180);
            g.setColor(windowBarsColor);
            g.setStroke(new BasicStroke(3));
            g.drawArc(startingX, startingY, width, height, 0, 180);
            g.drawLine(startingX, startingY + height / 2, startingX + width, startingY + height / 2);
            g.drawLine(startingX + width / 2, startingY + height / 2, startingX + width / 2, startingY);
            startingX += 40;
        }
    }

    public void drawSign(Graphics2D g) {
        g.setColor(signColor);
        g.fillRoundRect(getStartingX() + 20,  getUpperY() - 85, 110, 35, 30, 30);
        g.setColor(Color.BLACK);
        g.setFont(new Font("ComicSans", Font.ITALIC, 20));
        g.drawString(signText, getStartingX() + 45,getUpperY() - 85 + 22);
    }

    public void drawWindowAndDoor(Graphics2D g) {
        g.setColor(windowColor);
        g.fillRect(getStartingX() + 25, getUpperY() - 45, 70, 30);
        g.setColor(windowBarsColor);
        g.drawRect(getStartingX() + 25, getUpperY() - 45, 70, 30);
        g.setColor(Color.BLACK);
        g.fillRect(getStartingX() + 105, getUpperY() - 40, 30, 40);
        g.setColor(Color.YELLOW);
        g.fillOval(getStartingX() + 130 - 3, getUpperY() - 20 - 3, 3 * 2, 3 * 2);
    }

    public String getSignText() {
        return signText;
    }

    public void setSignText(String signText) {
        this.signText = signText;
    }

    public ThreeStoreyShop(int upperY, int startingX, String signText) {
        super(upperY, startingX);
        this.signText = signText.substring(0, 5);
    }
}
