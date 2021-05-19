package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.image.Sprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapSprite extends MapObject {
    /**
     * Can be set to `FlxObject.LEFT`, `RIGHT`, `UP`, and `DOWN` to take advantage
     * of flipped sprites and/or just track player orientation more easily.
     */
    public int facing = MapObject.RIGHT;

    /**
     * Whether this sprite is flipped on the X axis.
     */
    public boolean flipX = false;

    /**
     * Whether this sprite is flipped on the Y axis.
     */
    public boolean flipY = false;

    private BufferedImage image;

    /**
     * Creates a `MapSprite` at a specified position.
     *
     * @param x The initial X position of the sprite.
     * @param y The initial Y position of the sprite.
     */
    public MapSprite(float x, float y) {
        super(x, y);
    }

    public void loadGraphic(String path) {
        this.image = Sprite.loadImage(path);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        Vec2D mapScroll = Engine.camera.getScroll();
        if (image != null) {
            int x = (int) (position.x - mapScroll.x);
            int y = (int) (position.y - mapScroll.y);
            x = flipX ? (int) (x + width) : x;
            y = flipY ? (int) (y + height) : y;

            double width = flipX ? -this.width : this.width;
            double height = flipY ? -this.height : this.height;
            g.drawImage(image, x, y, (int) width, (int) height, null);
        }
    }
}
