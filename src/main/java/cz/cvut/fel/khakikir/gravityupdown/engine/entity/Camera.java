package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

public class Camera extends MapBasic {
    /**
     * Stores the camera's top-left corner position in world coordinates.
     */
    protected Vec2D scroll = new Vec2D();

    /**
     * How wide the camera display is, in game pixels.
     */
    protected double width;

    /**
     * How tall the camera display is, in game pixels.
     */
    protected double height;

    /**
     * Tells the camera to follow this MapBasic object around.
     */
    public MapObject target;

    public Camera(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public Vec2D getScroll() {
        return scroll;
    }

    /**
     * Tells this camera object what MapObject to track.
     * @param target The object the camera should track. Set to null to not follow anything.
     */
    public void follow(MapObject target) {
        this.target = target;
    }

    /**
     * Move the camera focus to this location instantly.
     * @param point Where you want the camera to focus.
     */
    public void focusOn(Vec2D point) {
        scroll.set(point.x - width * 0.5, point.y - height * 0.5);
    }


    @Override
    public void update() {
        if (target != null) {
            Vec2D point = target.getMidpoint();
            focusOn(point);
        }
    }
}
