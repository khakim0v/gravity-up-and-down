package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class MapObject {
    protected Point2D position;

    protected Point2D getPosition() {
        return position;
    }

    protected void setPosition(Point2D position) {
        this.position = position;
    }

    protected void setPosition(double x, double y) {
        this.position.setLocation(x, y);
    }

    protected abstract boolean isOutsideScreen();

    public abstract void draw(Graphics2D g);
}
