package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import java.awt.*;

public abstract class MapBasic {
    private final int id = idEnumerator++;
    private static int idEnumerator = 0;

    /**
     * Useful state for many game objects - "dead" (`!alive`) vs `alive`.
     */
    public boolean alive = true;

    // TODO: exists, etc.

    public void update() {
        // nothing
    }

    public void draw(Graphics2D g) {
        // nothing
    }
}
