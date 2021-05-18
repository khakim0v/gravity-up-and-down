package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.Time;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.image.Sprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Used for showing infinitely scrolling backgrounds.
 */
public class Backdrop extends MapObject {
    private final BufferedImage image;
    private final int width;
    private final int height;

    public Backdrop(String path) {
        this.image = Sprite.loadImage(path);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void update() {
        double x = (position.x + velocity.x * Time.deltaTime) % width;
        double y = (position.y + velocity.y * Time.deltaTime) % height;
        position.set(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        Vec2D mapScroll = Engine.camera.getScroll();
        final int x = Math.floorMod((int) (position.x - mapScroll.x), width);
        final int y = Math.floorMod((int) (position.y - mapScroll.y), height);

        // 1 2
        // 3 4
        // TODO: Use GamePanel.WINDOW_WIDTH and WINDOW_HEIGHT
        g.drawImage(image, 0, 0, x, y, width - x, height - y, width, height, null);
        g.drawImage(image, x, 0, width, y, 0, height - y, width - x, height, null);
        g.drawImage(image, 0, y, x, height, width - x, 0, width, height - y, null);
        g.drawImage(image, x, y, width, height, 0, 0, width - x, height - y, null);
    }
}
