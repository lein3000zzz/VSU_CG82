package ru.vsu.cs.course2.cg24.g82.severov_v_d.demo82.task1;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class World {

    List<WorldObject> worldObjects;

    public World(List<WorldObject> worldObjects) {
        this.worldObjects = worldObjects;
        Collections.sort(worldObjects);
    }

    public World(WorldObject... worldObjectsToAdd) {
        this.worldObjects = new ArrayList<>();
        worldObjects.addAll(Arrays.asList(worldObjectsToAdd));
        Collections.sort(worldObjects);
    }

    public World() {
        this.worldObjects = new ArrayList<>();
        Collections.sort(worldObjects);
    }

    public void draw(Graphics2D g) {
        for (WorldObject worldObject: worldObjects) {
            worldObject.draw(g);
        }
    }

    public void addObject(WorldObject... worldObjectsToAdd) {
        worldObjects.addAll(Arrays.asList(worldObjectsToAdd));
        Collections.sort(worldObjects);
    }

    public List<WorldObject> getWorldObjects() {
        return worldObjects;
    }

    public void setWorldObjects(List<WorldObject> worldObjects) {
        this.worldObjects = worldObjects;
    }
}
