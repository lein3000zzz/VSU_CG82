package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1;

import java.awt.*;

public abstract class WorldObject implements Comparable<WorldObject> {
    private int priority;

    public abstract void draw(Graphics2D g);

    @Override
    public int compareTo(WorldObject o) {
        if (this.priority > o.priority) {
            return 1;
        } else if (this.priority < o.priority) {
            return -1;
        } else {
            return 0;
        }
    }

    public WorldObject(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
