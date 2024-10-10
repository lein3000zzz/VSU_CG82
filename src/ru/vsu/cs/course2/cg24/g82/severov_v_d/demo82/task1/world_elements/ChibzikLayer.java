package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChibzikLayer extends WorldObject {
    private static final int priority = 9;

    private static final Random rnd = new Random();

    List<Chibzik> chibziks;

    public ChibzikLayer() {
        super(priority);
        this.chibziks = new ArrayList<>();
    }

    public ChibzikLayer(Chibzik... chibziksToAdd) {
        super(priority);
        this.chibziks = new ArrayList<>();
        chibziks.addAll(Arrays.asList(chibziksToAdd));
    }

    public ChibzikLayer(List<Chibzik> chibziks) {
        super(priority);
        this.chibziks = chibziks;
    }

    public void addChibzik(Chibzik... chibziksToAdd) {
        chibziks.addAll(Arrays.asList(chibziksToAdd));
    }

    public void moveChibziks(int xChanging) {
        for (Chibzik chibzik : chibziks) {
            chibzik.moveChibzik(xChanging);
            chibzik.changeState();
        }
    }

    public void checkChibzikReached() {
        for (Chibzik chibzik : chibziks) {
            if (chibzik.getCurrentX() >= 700 + chibzik.getStopPointSpread()) {
                chibzik.moveChibzik(0);
                chibzik.setReachedToys(true);
                chibzik.disableMovement(1500);
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        for (Chibzik chibzik: chibziks) {
            chibzik.draw(g);
        }
    }

    public List<Chibzik> getChibziks() {
        return chibziks;
    }

    public void setChibziks(List<Chibzik> chibziks) {
        this.chibziks = chibziks;
    }

}
