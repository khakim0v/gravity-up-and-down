package cz.cvut.fel.khakikir.gravityupdown.engine.tile;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.EngineMath;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TileLayer extends MapObject {
    private static final int TILESET_WIDTH = 16;
    private static final int TILESET_HEIGHT = 16;

    private int widthInTiles;
    private int heightInTiles;
    private int totalTiles;

    private int tileWidth;
    private int tileHeight;

    /**
     * Collection of tile objects, one for each type of tile in the layer
     * (NOT one for every single tile in the whole layer).
     */
    private Map<Integer, Tile> tiles;

    /**
     * Representation of the actual tile data, as a large 1D array of integers.
     */
    private Integer[][] data;

    private BufferedImage bufferedImage;
    private Graphics2D graphics;


    public TileLayer() {
        this.tiles = new HashMap<>();

        immovable = true;
        moves = false;
    }

    public TileLayer loadMapFromArray(Integer[][] mapData,
                                      int widthInTiles, int heightInTiles,
                                      BufferedImage tileSetImage,
                                      int tileWidth, int tileHeight) {
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.data = mapData.clone();
        this.bufferedImage = new BufferedImage(widthInTiles * tileWidth, heightInTiles * tileHeight,
                BufferedImage.TYPE_INT_ARGB);
        this.graphics = bufferedImage.createGraphics();

        loadMapHelper(tileSetImage, tileWidth, tileHeight);
        return this;
    }

    /**
     * Loads the tilelayer with image data and a tile graphic.
     */
    private void loadMapHelper(BufferedImage tileSetImage, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

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

    /**
     * Checks if the MapObject overlaps any tiles, and calls the specified function if it's true.
     *
     * @param object    The MapObject you are checking for overlaps against.
     * @param callback  An optional function, where first parameter is a Tile object, and second one
     *                  is the object passed in in the first parameter of this method.
     * @return Whether there were overlaps, or if a callback was specified, whatever the return value of the callback was.
     */
    public boolean overlapsWithCallback(MapObject object, BiFunction<MapObject, MapObject, Boolean> callback) {
        boolean results = false;

        // Figure out what tiles we need to check against
        int selectionX1 = (int) Math.floor(object.position.x / tileWidth);
        int selectionY1 = (int) Math.floor(object.position.y / tileHeight);
        int selectionX2 = selectionX1 + (int) (Math.ceil(object.width / tileWidth) + 1);
        int selectionY2 = selectionY1 + (int) (Math.ceil(object.height / tileWidth) + 1);

        // Then bound these coordinates by the map edges
        selectionX1 = EngineMath.bound(selectionX1, 0, widthInTiles);
        selectionY1 = EngineMath.bound(selectionY1, 0, heightInTiles);
        selectionX2 = EngineMath.bound(selectionX2, 0, widthInTiles);
        selectionY2 = EngineMath.bound(selectionY2, 0, heightInTiles);

        // Then loop through this selection of tiles
        double deltaX = position.x - last.x;
        double deltaY = position.y - last.y;
        for (int row = selectionY1; row < selectionY2; row++) {
            for (int column = selectionX1; column < selectionX2; column++) {
                Integer tileId = data[row][column];
                if (tileId != null) {
                    Tile tile = tiles.get(tileId);

                    // Instantiate a tile object for collision detection/resolution
                    TileInstance tileInstance = new TileInstance();
                    tileInstance.width = tileWidth;
                    tileInstance.height = tileHeight;
                    tileInstance.position.x = position.x + column * tile.getWidth();
                    tileInstance.position.y = position.y + row * tile.getHeight();
                    tileInstance.last.x = tileInstance.position.x - deltaX;
                    tileInstance.last.y = tileInstance.position.y - deltaY;

                    boolean overlapFound = ((object.position.x + object.width) > tileInstance.position.x)
                            && (object.position.x < (tileInstance.position.x + tileInstance.width))
                            && ((object.position.y + object.height) > tileInstance.position.y)
                            && (object.position.y < (tileInstance.position.y + tileInstance.height));

                    if (callback != null) {
                        overlapFound = callback.apply(tileInstance, object);
                    }

                    if (overlapFound) {
                        results = true;
                    }
                }
            }
        }

        return results;
    }

    @Override
    public void draw(Graphics2D g) {
        // don't try to render a tilemap that isn't loaded yet
        if (graphics == null)
            return;

        for (int row = 0; row < heightInTiles; row++) {
            for (int column = 0; column < widthInTiles; column++) {
                Integer tileId = data[row][column];
                if (tileId != null) {
                    Tile tile = tiles.get(tileId);
                    tile.draw(graphics, column, row);
                }
            }
        }

        // render tilemap to the screen
        Vec2D screenPosition = getScreenPosition();
        int x = (int) screenPosition.x;
        int y = (int) screenPosition.y;
        g.drawImage(bufferedImage, x, y,null);
    }
}
