package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import java.awt.*;

public abstract class MapBasic {
    private static int idEnumerator = 0;
    private final int id = idEnumerator++;
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

    /**
     * Controls whether `update()` and `draw()` are automatically called by `GameState`/`MapGroup`.
     */
    public boolean exists = true;

    /**
     * Handy function for "killing" game objects. Use `reset()` to revive them.
     */
    public void kill() {
        alive = false;
        exists = false;
    }

    /**
     * Handy function for bringing game objects "back to life". Just sets `alive` and `exists` back to `true`.
     * In practice, this function is most often called by `FlxObject#reset()`.
     */
    public void revive() {
        alive = true;
        exists = true;
    }

    public void update() {
        // nothing
    }

    public void draw(Graphics2D g) {
        // nothing
    }
}
