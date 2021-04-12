package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.image.Sprite;

import java.awt.*;

public abstract class Entity extends MapObject {
    protected Sprite sprite;

    @Override
    public void draw(Graphics2D g) {
        if (!isOutsideScreen()) {
            // draw sprite
        }
    }
}
