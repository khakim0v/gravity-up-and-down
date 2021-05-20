package cz.cvut.fel.khakikir.gravityupdown.engine.effects;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.util.EngineTimer;

import java.util.HashMap;
import java.util.Map;

/**
 * The retro flickering effect with callbacks.
 */
public class EngineFlicker {
    /**
     * Internal map for looking up which objects are currently flickering and getting their flicker data.
     */
    static Map<MapObject, EngineFlicker> boundObjects = new HashMap<>();
    /**
     * The flickering object.
     */
    public MapObject object;
    /**
     * The final visibility of the object after flicker is complete.
     */
    public boolean endVisibility;
    /**
     * The flicker timer.
     */
    public EngineTimer timer;
    /**
     * The duration of the flicker (in seconds). `0` means "forever".
     */
    public double duration;
    /**
     * The interval of the flicker.
     */
    public double interval;

    /**
     * A simple flicker effect for sprites using a ping-pong tween by toggling visibility.
     *
     * @param object   The object.
     * @param duration How long to flicker for (in seconds). `0` means "forever".
     * @param interval In what interval to toggle visibility. Set to `Engine.elapsed` if `<= 0`!
     * @return The `EngineFlicker` object.
     */
    public static EngineFlicker flicker(MapObject object, double duration, double interval) {
        if (isFlickering(object)) {
            stopFlickering(object);
        }

        if (interval <= 0) {
            interval = Engine.elapsed;
        }

        EngineFlicker flicker = new EngineFlicker();
        flicker.start(object, duration, interval);
        return boundObjects.put(object, flicker);
    }

    /**
     * Returns whether the object is flickering or not.
     *
     * @param object The object to test.
     */
    public static boolean isFlickering(MapObject object) {
        return boundObjects.containsKey(object);
    }

    /**
     * Stops flickering of the object. Also it will make the object visible.
     *
     * @param object The object to stop flickering.
     */
    public static void stopFlickering(MapObject object) {
        EngineFlicker boundFlicker = boundObjects.get(object);
        if (boundFlicker != null) {
            boundFlicker.stop();
        }
    }

    /**
     * Starts flickering behavior.
     */
    private void start(MapObject object, double duration, double interval) {
        this.object = object;
        this.duration = duration;
        this.interval = interval;
        this.endVisibility = true;
        timer = new EngineTimer(null).start(interval, this::flickerProgress, (int) (duration / interval));
    }

    /**
     * Prematurely ends flickering.
     */
    public void stop() {
        timer.cancel();
        object.visible = true;
        boundObjects.remove(object);
    }

    /**
     * Just a helper function for flicker() to update object's visibility.
     */
    private void flickerProgress(EngineTimer timer) {
        object.visible = !object.visible;

        if (timer.loops > 0 && timer.getLoopsLeft() == 0) {
            object.visible = endVisibility;
            boundObjects.remove(object);
        }
    }
}
