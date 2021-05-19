package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.image.Sprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Used for showing infinitely scrolling backgrounds.
 */
public class Backdrop extends MapObject {
    private final BufferedImage image;
    private final int imageWidth;
    private final int imageHeight;

    public Backdrop(String path) {
        this.image = Sprite.loadImage(path);
        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();

        immovable = true;
        moves = true;
    }

    @Override
    public void update() {
        super.update();
        position.x %= imageWidth;
        position.y %= imageHeight;
    }

    @Override
    public void draw(Graphics2D g) {
        Vec2D mapScroll = Engine.camera.getScroll();
        final int x = Math.floorMod((int) (position.x - mapScroll.x), imageWidth);
        final int y = Math.floorMod((int) (position.y - mapScroll.y), imageHeight);

        // 1 2
        // 3 4
        // TODO: Use GamePanel.WINDOW_WIDTH and WINDOW_HEIGHT
        g.drawImage(image, 0, 0, x, y, imageWidth - x, imageHeight - y, imageWidth, imageHeight, null);
        g.drawImage(image, x, 0, imageWidth, y, 0, imageHeight - y, imageWidth - x, imageHeight, null);
        g.drawImage(image, 0, y, x, imageHeight, imageWidth - x, 0, imageWidth, imageHeight - y, null);
        g.drawImage(image, x, y, imageWidth, imageHeight, 0, 0, imageWidth - x, imageHeight - y, null);
    }
}
