package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.world_elements;

import ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1.WorldObject;

import java.awt.*;

public abstract class Building extends WorldObject {
    private static final int priority = 4;
    private int upperY;
    private int startingX;

    public Building(int upperY, int startingX) {
        super(priority);
        this.upperY = upperY;
        this.startingX = startingX;
    }


    public int getStartingX() {
        return startingX;
    }

    public void setStartingX(int startingX) {
        this.startingX = startingX;
    }

    public int getUpperY() {
        return upperY;
    }

    public void setUpperY(int upperY) {
        this.upperY = upperY;
    }
}
