package cz.cvut.fel.khakikir.gravityupdown.engine.tile;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapBasic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TileLayer extends MapBasic {
    private static final int TILESET_WIDTH = 16;
    private static final int TILESET_HEIGHT = 16;

    private int widthInTiles;
    private int heightInTiles;
    private int totalTiles;

    /**
     * Collection of tile objects, one for each type of tile in the layer
     * (NOT one for every single tile in the whole layer).
     */
    private Map<Integer, Tile> tiles;

    /**
     * Representation of the actual tile data, as a large 1D array of integers.
     */
    private Integer[][] data;

    public TileLayer() {
        this.tiles = new HashMap<>();
    }

    public TileLayer loadMapFromArray(Integer[][] mapData,
                                      int widthInTiles, int heightInTiles,
                                      BufferedImage tileSetImage,
                                      int tileWidth, int tileHeight) {
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.data = mapData.clone();

        loadMapHelper(tileSetImage, tileWidth, tileHeight);
        return this;
    }

    /**
     * Loads the tilelayer with image data and a tile graphic.
     */
    private void loadMapHelper(BufferedImage tileSetImage, int tileWidth, int tileHeight) {
        Set<Integer> tileIds = Stream.of(data)
                .flatMap(Stream::of)
                .collect(Collectors.toSet());
        tileIds.remove(null); // remove empty tile
        totalTiles = tileIds.size();

        for (Integer tileId : tileIds) {
            BufferedImage tileImage = tileSetImage.getSubimage(
                    (tileId % TILESET_WIDTH) * tileWidth, (tileId / TILESET_HEIGHT) * tileHeight,
                    tileWidth, tileHeight);
            Tile tile = new Tile(this, tileImage, tileId, tileWidth, tileHeight);
            tiles.put(tile.getId(), tile);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        for (int i = 0; i < heightInTiles; i++) {
            for (int j = 0; j < widthInTiles; j++) {
                Integer tileId = data[i][j];
                if (tileId != null) {
                    Tile tile = tiles.get(tileId);
                    tile.draw(g, j, i);
                }
            }
        }
    }
}
