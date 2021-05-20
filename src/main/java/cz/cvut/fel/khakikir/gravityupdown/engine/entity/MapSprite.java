package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

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
    private float alpha = 1.0f;

    private AlphaComposite acAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    private final AlphaComposite acNormal = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);

    /**
     * Creates a `MapSprite` at a specified position.
     *
     * @param x The initial X position of the sprite.
     * @param y The initial Y position of the sprite.
     */
    public MapSprite(float x, float y) {
        super(x, y);
    }

    /**
     * Creates a `MapSprite` at a specified position and loads the graphic
     *
     * @param x The initial X position of the sprite.
     * @param y The initial Y position of the sprite.
     */
    public MapSprite(float x, float y, String path) {
        super(x, y);
        loadGraphic(path);
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.acAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    public BufferedImage getImage() {
        return image;
    }

    public Graphics2D getGraphics() {
        if (image != null) {
            return image.createGraphics();
        } else {
            return null;
        }
    }

    public void loadGraphic(String path) {
        this.image = Sprite.loadImage(path);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public void makeGraphic(double width, double height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage((int) this.width, (int) this.height,
                BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (image != null) {
            Vec2D screenPosition = getScreenPosition();
            int x = (int) screenPosition.x;
            int y = (int) screenPosition.y;
            x = flipX ? (int) (x + width) : x;
            y = flipY ? (int) (y + height) : y;

            double width = flipX ? -this.width : this.width;
            double height = flipY ? -this.height : this.height;

            g.setComposite(acAlpha);
            g.drawImage(image, x, y, (int) width, (int) height, null);
            g.setComposite(acNormal);
        }
    }
}
