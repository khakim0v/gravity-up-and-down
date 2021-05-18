package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import java.awt.*;

public abstract class MapBasic {
    private final int id = idEnumerator++;
    private static int idEnumerator = 0;

    public void update() {
        // nothing
    }

    public void draw(Graphics2D g) {
        // nothing
    }
}
