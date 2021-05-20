package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import java.awt.*;

public abstract class MapBasic {
    private final int id = idEnumerator++;
    private static int idEnumerator = 0;

    /**
     * Useful state for many game objects - "dead" (`!alive`) vs `alive`.
     */
    public boolean alive = true;

    /**
     * Controls whether `update()` is automatically called by `GameState`/`MapGroup`.
     */
    public boolean active = true;

    /**
     * Controls whether `draw()` is automatically called by `GameState`/`MapGroup`.
     */
    public boolean visible = true;

    // TODO: exists, etc.

    public void update() {
        // nothing
    }

    public void draw(Graphics2D g) {
        // nothing
    }
}
