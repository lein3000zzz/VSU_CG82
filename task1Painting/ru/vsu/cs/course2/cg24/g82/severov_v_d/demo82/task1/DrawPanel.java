package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.practice1.Sun;
import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DrawPanel extends JPanel {
    private final World world = new World();
    private java.util.List<Cloud> cloudList = new ArrayList<>();
    private java.util.List<Balloons> balloonsList = new ArrayList<>();
    private static final Random rnd = new Random();
    private final Timer balloonsTimer = new Timer(300, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Balloons balloons : balloonsList) {
                balloons.changeState();
            }
            repaint();
        }
    });

    ChibzikLayer chibzikLayer = new ChibzikLayer();

    Timer chibzikTimer = new Timer(50, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            chibzikLayer.moveChibziks(5);
            chibzikLayer.checkChibzikReached();
            repaint();
        }
    });

    private final Timer cloudsTimer = new Timer(50, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Cloud cloud : cloudList) {
                cloud.moveCloud();
            }
            repaint();
        }
    });
    public DrawPanel() {
        Ground ground = new Ground();
        Background background = new Background(ground.getLowerY(), true, true);
        Water water = new Water(150, ground.getLowerY());
        ThreeStoreyShop shop = new ThreeStoreyShop(ground.getLowerY(), 20, "Shoes");
        ToyShop toyShop = new ToyShop(ground.getLowerY(), 600);
        Balloons balloons1 = new Balloons(ground.getLowerY(), 267 * 2 + 20);
        Balloons balloons2 = new Balloons(ground.getLowerY(), 267 - 30);
        Collections.addAll(balloonsList, balloons1, balloons2);
        Cloud cloudOne = new Cloud(1, 0, 400, 10);
        Cloud cloudTwo = new Cloud(3, 1, 200, 40);
        Cloud cloudThree = new Cloud(2, 2, 600, 25);
        Collections.addAll(cloudList, cloudOne, cloudTwo, cloudThree);
        Pole pole1 = new Pole(ground.getLowerY(), 250);
        Pole pole2 = new Pole(ground.getLowerY(), 267 * 2 + 15);
        world.addObject(ground, background, water, shop, toyShop, balloons1, balloons2, cloudOne, cloudTwo, cloudThree, pole1, pole2, chibzikLayer);
        addMouseListener(new MouseAdapter() {
                             @Override
                             public void mouseClicked(MouseEvent e) {
                                 chibzikLayer.addChibzik(new Chibzik(ground.getLowerY(), -10, 5));
                                 chibzikTimer.start();
                             }
                         }
        );
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        balloonsTimer.start();
        cloudsTimer.start();
        world.draw((Graphics2D) g);
    }

}
