package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

public abstract class MapObject extends MapBasic {
    public Vec2D position;
    public Vec2D velocity;

    protected double width;
    protected double height;

    protected MapObject() {
        this.position = new Vec2D(0, 0);
        this.velocity = new Vec2D(0, 0);
    }

    public MapObject(double x, double y, double width, double height) {
        setPosition(x, y);
        this.width = width;
        this.height = height;
    }

    public void setPosition(double x, double y) {
        this.position.set(x, y);
    }

    public void setVelocity(double x, double y) {
        this.velocity.set(x, y);
    }

    /**
     * Retrieve the midpoint of this object in world coordinates.
     * @return A `Vec2D` object containing the midpoint of this object in world coordinates.
     */
    public Vec2D getMidpoint() {
        return new Vec2D(position.x + width * 0.5, position.y + height * 0.5);
    }
}
