package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.Vec2D;

import java.awt.*;

public abstract class MapObject {
    protected Vec2D position;
    protected Vec2D velocity;

    protected MapObject() {
        this.position = new Vec2D(0, 0);
        this.velocity = new Vec2D(0, 0);
    }

    protected void setPosition(double x, double y) {
        this.position.set(x, y);
    }

    protected void translatePosition(double dx, double dy) {
        position.set(position.getX() + dx, position.getY() + dy);
    }

    public void setVelocity(double x, double y) {
        this.velocity.set(x, y);
    }

    protected void translateVelocity(double dx, double dy) {
        velocity.set(velocity.getX() + dx, velocity.getY() + dy);
    }

    protected boolean isOutsideScreen() {
        return false;
    }

    public abstract void update();

    public abstract void draw(Graphics2D g);
}
