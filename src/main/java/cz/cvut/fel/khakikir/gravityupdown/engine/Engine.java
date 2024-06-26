package cz.cvut.fel.khakikir.gravityupdown.engine;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Camera;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapBasic;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapGroup;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Engine {
    /**
     * A global camera
     */
    public static Camera camera;
    public static int width;
    public static int height;

    /**
     * Signals
     */
    public static Signal focusGained = new Signal();
    public static Signal focusLost = new Signal();

    /**
     * The delta time for update() in seconds
     */
    public static double elapsed;
    public static double instantFps;
    public static double averageFps;

    /**
     * Call this function to see if one {@code MapObject} collides with another.
     * This function just calls `Engine.overlap` and presets the `processCallback` parameter to `MapObject.separate`.
     *
     * @param object1 The first object to check.
     * @param object2 The second object to check.
     * @return Whether any objects were successfully collided/separated.
     */
    public static boolean collide(MapObject object1, MapObject object2) {
        return collide(object1, object2, null);
    }

    /**
     * Call this function to see if one {@code MapObject} collides with another.
     * This function just calls `Engine.overlap` and presets the `processCallback` parameter to `MapObject.separate`.
     *
     * @param object1   The first object to check.
     * @param object2   The second object to check.
     * @param onOverlap A function with two {@code MapObject} parameters, that is called if those two objects overlap.
     * @return Whether any objects were successfully collided/separated.
     */
    public static boolean collide(MapObject object1, MapObject object2, BiConsumer<MapObject, MapObject> onOverlap) {
        return overlap(object1, object2, onOverlap, MapObject::separate);
    }

    /**
     * Call this function to see if one `MapObject` overlaps another.
     *
     * @param object1 The first object to check.
     * @param object2 The second object to check.
     * @return Whether any overlaps were detected.
     */
    public static boolean overlap(MapObject object1, MapObject object2) {
        return overlap(object1, object2, null);
    }

    /**
     * Call this function to see if one `MapObject` overlaps another.
     *
     * @param object1        The first object to check.
     * @param object2        The second object to check.
     * @param notifyCallback A function with two `MapObject` parameters,
     *                       that is called if those two objects overlap.
     * @return Whether any overlaps were detected.
     */
    public static boolean overlap(MapObject object1, MapObject object2,
                                  BiConsumer<MapObject, MapObject> notifyCallback) {
        return overlap(object1, object2, notifyCallback, MapObject::updateTouchingFlags);
    }

    /**
     * Call this function to see if any object in `MapGroup` overlaps `MapObject`.
     *
     * @param group          The group object to check.
     * @param object         The object to check.
     * @param notifyCallback A function with two `MapObject` parameters,
     *                       that is called if those two objects overlap.
     * @return Whether any overlaps were detected.
     */
    public static boolean overlap(MapGroup group, MapObject object,
                                  BiConsumer<MapObject, MapObject> notifyCallback) {
        List<MapBasic> groupObjects = group.getMembers();
        boolean result = false;
        for (MapBasic groupObject : groupObjects) {
            if (groupObject.exists) {
                boolean overlaps = overlap((MapObject) groupObject, object, notifyCallback, MapObject::updateTouchingFlags);
                if (overlaps) {
                    result = true;
                }
            }
        }

        return result;
    }

    private static boolean overlap(MapObject object1, MapObject object2,
                                   BiConsumer<MapObject, MapObject> notifyCallback,
                                   BiFunction<MapObject, MapObject, Boolean> processCallback) {
        boolean result = false;
        if (processCallback != null && processCallback.apply(object1, object2)) {
            result = true;
            if (notifyCallback != null) {
                notifyCallback.accept(object1, object2);
            }
        }

        return result;
    }
}
