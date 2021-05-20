package cz.cvut.fel.khakikir.gravityupdown.game.pojo;

import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

public class SavePoint {
    public Vec2D position;
    public Vec2D velocity;
    public Vec2D acceleration;
    public boolean flipY;
    public int facing;

    public double timeElapsed;

    public SavePoint() {
        position = new Vec2D();
        velocity = new Vec2D();
        acceleration = new Vec2D();
        facing = 0;
        flipY = false;
        timeElapsed = 0.0;
    }
}
