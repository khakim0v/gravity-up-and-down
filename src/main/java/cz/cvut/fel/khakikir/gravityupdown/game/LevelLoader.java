package cz.cvut.fel.khakikir.gravityupdown.game;

import cz.cvut.fel.khakikir.gravityupdown.engine.asset.ResourceException;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.image.Sprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.tile.TileLayer;
import cz.cvut.fel.khakikir.gravityupdown.game.gamestate.LevelState;
import org.tiledreader.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Set;

public class LevelLoader {
    private static final String TILESET_PATH = "/images/tileset.png";

    public static void loadTiledMap(String path, LevelState state) {
        String pathToExecutable = null;
        try {
            File file = new File(LevelLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            pathToExecutable = file.isDirectory() ? file.getPath() : file.getParent();
        } catch (URISyntaxException e) {
            throw new ResourceException("Failed to load the map", e);
        }
        path = pathToExecutable + path;

        FileSystemTiledReader tiledReader = new FileSystemTiledReader();
        TiledMap map = tiledReader.getMap(path);

        // Handle level properties
        state.handleLevelProperties(map.getProperties());

        // Handle level layers
        for (TiledLayer layer : map.getTopLevelLayers()) {
            if (layer instanceof TiledTileLayer) {
                TiledTileLayer tiledTileLayer = (TiledTileLayer) layer;

                Integer[][] mapData = new Integer[map.getHeight()][map.getWidth()];

                Set<Point> points = tiledTileLayer.getTileLocations();
                points.forEach(p -> {
                    TiledTile tile = tiledTileLayer.getTile(p.x, p.y);
                    mapData[p.y][p.x] = tile.getID();
                });

                BufferedImage tileSetImage = Sprite.loadImage(TILESET_PATH);
                TileLayer tileLayer = new TileLayer();
                tileLayer.loadMapFromArray(mapData,
                        map.getWidth(), map.getHeight(),
                        tileSetImage,
                        map.getTileWidth(), map.getTileHeight());
                state.handleLevelLayer(tileLayer, tiledTileLayer.getName());
            } else if (layer instanceof TiledObjectLayer) {
                TiledObjectLayer tiledObjectLayer = (TiledObjectLayer) layer;
                for (var tiledObject : tiledObjectLayer.getObjects()) {
                    state.handleLevelObject(tiledObject);
                }
            }
        }


//        var tmxMapReader = new TMXMapReader();

//        Map map;
//        try {
//            InputStream is = LevelLoader.class.getResourceAsStream(path);
//            map = tmxMapReader.readMap(is);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        // Handle level properties
//        state.handleLevelProperties(map.getProperties());
//
//        // Handle level layers
//        for (MapLayer layer : map.getLayers()) {
//            if (layer instanceof TileLayer) {
//                TileLayer tileLayer = (TileLayer) layer;
//                TileLayer tilemap = new TileLayer();
//                //tilemap.loadMapFromArray();
//            } else if (layer instanceof ObjectGroup) {
//                ObjectGroup objectGroup = (ObjectGroup) layer;
//                objectGroup.getObjects();
//            }
//        }
    }
}
