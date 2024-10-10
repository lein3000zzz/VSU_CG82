package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.practice1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawPanelPractice extends JPanel {
    private static final Random rnd = new Random();
    private List<Sun> suns = new ArrayList<>();
    private int x = rnd.nextInt(600);
    private static final Color[] COLORS = {Color.ORANGE, Color.BLUE, Color.YELLOW, Color.RED, Color.CYAN, Color.MAGENTA};
    private Timer timer = new Timer(40, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Sun sun : suns) {
                sun.setX(sun.getX() + 1);
                if (sun.getX() > 800) {
                    sun.setX(0);
                }
            }
            repaint();
        }
    });

    public DrawPanelPractice() {
        for (int i = 0; i < 5; i++) {
            suns.add(new Sun(
                    100 + rnd.nextInt(600),
                    50 + rnd.nextInt(150),
                    10 + rnd.nextInt(90),
                    3 + rnd.nextInt(100),
                    20 + rnd.nextInt(80),
                    COLORS[rnd.nextInt(COLORS.length)]
            ));
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Sun sun : suns) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        sun.setX(sun.getX() - 10);
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        sun.setX(sun.getX() + 10);
                    }
                }
                repaint();
            }
        }
        );
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(20, 30, 40, 40);
        g.drawLine(20, 30, 40, 10);
        g.drawLine(60, 30, 40, 10);

        for (Sun s : suns) {
            s.draw((Graphics2D) g);
        }

        timer.start();
    }

    // с методом при перерисовке генерится новое, если делать рандом, что кринж
//    public static void drawSun(Graphics2D g, int x, int y, int r, int beamCount, int beamLength, Color color) {
//        g.setColor(color);
//        g.fillOval(x - r, y - r, r * 2, r * 2);
//        double deltaAlpha = 2 * Math.PI / beamCount;
//        for (int i = 0; i < beamCount; i++) {
//            double alpha = deltaAlpha * i;
//            double x1 = x + r * Math.cos(alpha);
//            double y1 = y + r * Math.sin(alpha);
//            double x2 = x + (r + beamLength) * Math.cos(alpha);
//            double y2 = y + (r + beamLength) * Math.sin(alpha);
//            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
//        }
//    }
}
