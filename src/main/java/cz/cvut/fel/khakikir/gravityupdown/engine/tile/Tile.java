package cz.cvut.fel.khakikir.gravityupdown.engine.tile;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void draw(Graphics2D g, int tilemapX, int tilemapY) {
        if (image != null) {
            final int x = tilemapX * width;
            final int y = tilemapY * height;
            g.drawImage(image, x, y, null);
        }
    }
}
