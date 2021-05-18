package cz.cvut.fel.khakikir.gravityupdown.engine;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Camera;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapBasic;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;

import java.util.function.BiConsumer;

public class Engine {
    /**
     * A global camera
     */
    public static Camera camera;

    // TODO: Add groups of objects
    public static boolean collide(MapBasic object1, MapBasic object2) {
        return collide(object1, object2, null);
    }

    public static boolean collide(MapBasic object1, MapBasic object2, BiConsumer<MapBasic, MapBasic> onOverlap) {
        return false;
    }

    public static void separate(MapObject object1, MapObject object2) {

    }
}
