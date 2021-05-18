package cz.cvut.fel.khakikir.gravityupdown.engine.tile;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapBasic;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameStateManager;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    /**
     * A reference to the tilemap this tile object belongs to.
     */
    private TileLayer tilemap;

    /**
     * The ID of this tile type in the core map data.
     * For example, if map only has 16 kinds of tiles in it,
     * this number is usually between 0 and 15.
     */
    private int id;

    /**
     * The tile's sprite
     */
    private BufferedImage image;

    private int width;
    private int height;

    public Tile(TileLayer tilemap, BufferedImage image, int id, int width, int height) {
        this.image = image;
        this.tilemap = tilemap;
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void draw(Graphics2D g, int tilemapX, int tilemapY) {
        Vec2D mapScroll = Engine.camera.getScroll();
        if (image != null) {
            final int x = (int) (tilemapX * width - mapScroll.x);
            final int y = (int) (tilemapY * height - mapScroll.y);
            g.drawImage(image, x, y, null);
        }
    }
}
