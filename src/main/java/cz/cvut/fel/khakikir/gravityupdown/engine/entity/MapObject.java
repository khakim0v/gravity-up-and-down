package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.Time;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.EngineVelocity;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;
import cz.cvut.fel.khakikir.gravityupdown.engine.tile.TileLayer;

import java.awt.geom.Rectangle2D;

@SuppressWarnings({"DuplicatedCode", "ConstantConditions"})
public abstract class MapObject extends MapBasic {
    /**
     * Generic value for "left". Used by `facing` and `touching`.
     */
    public static final int LEFT = 0x0001;
    /**
     * Generic value for "right". Used by `facing` and `touching`.
     */
    public static final int RIGHT = 0x0010;
    /**
     * Generic value for "up". Used by `facing` and `touching`.
     */
    public static final int UP = 0x0100;
    /**
     * Generic value for "down". Used by `facing` and `touching`.
     */
    public static final int DOWN = 0x1000;
    /**
     * Special-case constant meaning no collisions, used mainly by `touching`.
     */
    public static final int NONE = 0x0000;
    /**
     * Special-case constant meaning up, used mainly by `touching`.
     */
    public static final int CEILING = UP;
    /**
     * Special-case constant meaning down, used mainly by `touching`.
     */
    public static final int FLOOR = DOWN;
    /**
     * Special-case constant meaning only the left and right sides, used mainly by `touching`.
     */
    public static final int WALL = LEFT | RIGHT;
    /**
     * Special-case constant meaning any direction, used mainly by 'touching`.
     */
    public static final int ANY = LEFT | RIGHT | UP | DOWN;
    /**
     * This value dictates the maximum number of pixels two objects have to intersect
     * before collision stops trying to separate them.
     * Don't modify this unless your objects are passing through each other.
     */
    private static final double SEPARATE_BIAS = 4.0;
    public double width;
    public double height;

    public Vec2D position;

    /**
     * The basic speed of this object (in pixels per second).
     */
    public Vec2D velocity;

    /**
     * How fast the speed of this object is changing (in pixels per second).
     * Useful for smooth movement and gravity.
     */
    public Vec2D acceleration;

    /**
     * This isn't drag exactly, more like deceleration that is only applied
     * when `acceleration` is not affecting the sprite.
     */
    public Vec2D drag;

    /**
     * If you are using `acceleration`, you can use `maxVelocity` with it
     * to cap the speed automatically (very useful!).
     */
    public Vec2D maxVelocity;

    /**
     * Important variable for collision processing.
     * By default this value is set automatically during at the start of `update()`.
     */
    public Vec2D last;

    /**
     * The virtual mass of the object. Default value is 1.0. Currently only used with elasticity
     * during collision resolution. Change at your own risk; effects seem crazy unpredictable so far!
     */
    public double mass = 1.0;

    /**
     * The bounciness of this object. Only affects collisions.
     * Default value is 0.0, or "not bouncy at all."
     */
    public double elasticity = 0.0;

    /**
     * Bit field of flags (use with UP, DOWN, LEFT, RIGHT, etc) indicating surface contacts.
     * Use bitwise operators to check the values stored here, or use isTouching(), justTouched(), etc.
     */
    public int touching = NONE;

    /**
     * Bit field of flags (use with UP, DOWN, LEFT, RIGHT, etc) indicating surface contacts from the previous game loop step. Use bitwise operators to check the values
     * stored here, or use isTouching(), justTouched(), etc. You can even use them broadly as boolean values if you're feeling saucy!
     */
    public int wasTouching = NONE;

    /**
     * Whether this sprite is dragged along with the horizontal movement of objects it collides with
     * (makes sense for horizontally-moving platforms for example).
     */
    public boolean collisionXDrag = true;

    /**
     * Set this to `false` to skip the automatic motion/movement stuff (see `updateMotion()`).
     * `MapObject` default to `true`. `FlxTilemap` default to `false`.
     */
    public boolean moves = true;

    /**
     * Whether an object will move/alter position after a collision.
     */
    public boolean immovable = false;

    /**
     * Controls how much this object is affected by camera scrolling.
     * `0` = no movement (e.g. a background layer),
     * `1` = same movement speed as the foreground.
     * Default value is `(1,1)`.
     */
    public Vec2D scrollFactor;

    protected MapObject() {
        this.position = new Vec2D(0, 0);
        initVars();
    }

    protected MapObject(double x, double y) {
        this.position = new Vec2D(x, y);
        initVars();
    }

    public MapObject(double x, double y, double width, double height) {
        this.position = new Vec2D(x, y);
        initVars();

        this.width = width;
        this.height = height;
    }

    /**
     * The main collision resolution function.
     *
     * @param object1 Any `MapObject`.
     * @param object2 Any other `MapObject`.
     * @return Whether the objects in fact touched and were separated.
     */
    public static boolean separate(MapObject object1, MapObject object2) {
        boolean separatedX = separateX(object1, object2);
        boolean separatedY = separateY(object1, object2);
        return separatedX || separatedY;
    }

    /**
     * Similar to `separate()`, but only checks whether any overlap is found and updates
     * the `touching` flags of the input objects, but no separation is performed.
     *
     * @param object1 Any `MapObject`.
     * @param object2 Any other `MapObject`.
     * @return Whether the objects in fact touched.
     */
    public static boolean updateTouchingFlags(MapObject object1, MapObject object2) {
        boolean touchingX = updateTouchingFlagsX(object1, object2);
        boolean touchingY = updateTouchingFlagsY(object1, object2);
        return touchingX || touchingY;
    }

    private void initVars() {
        this.last = new Vec2D(position.x, position.y);
        this.scrollFactor = new Vec2D(1, 1);

        // motions vars
        this.velocity = new Vec2D(0, 0);
        this.acceleration = new Vec2D(0, 0);
        this.drag = new Vec2D(0, 0);
        this.maxVelocity = new Vec2D(10000, 10000);
    }

    /**
     * Call this function to figure out the on-screen position of the object.
     *
     * @return A new Vec2D, containing the screen X and Y position of this object.
     */
    public Vec2D getScreenPosition() {
        Vec2D point = new Vec2D(position);
        point.x -= Engine.camera.scroll.x * scrollFactor.x;
        point.y -= Engine.camera.scroll.y * scrollFactor.y;
        return point;
    }

    public void setPosition(double x, double y) {
        this.position.set(x, y);
    }

    public void setVelocity(double x, double y) {
        this.velocity.set(x, y);
    }

    /**
     * Retrieve the midpoint of this object in world coordinates.
     *
     * @return A `Vec2D` object containing the midpoint of this object in world coordinates.
     */
    public Vec2D getMidpoint() {
        return new Vec2D(position.x + width * 0.5, position.y + height * 0.5);
    }

    /**
     * Checking overlap and updating `touching` variables, X-axis part used by `updateTouchingFlags`.
     *
     * @param object1 Any `MapObject`.
     * @param object2 Any other `MapObject`.
     * @return Whether the objects in fact touched along the X axis.
     */
    private static boolean updateTouchingFlagsX(MapObject object1, MapObject object2) {
        // If one of the objects is a tilelayer, just pass it off.
        if (object1 instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) object1;
            return tileLayer.overlapsWithCallback(object2, MapObject::updateTouchingFlagsX);
        }
        if (object2 instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) object2;
            return tileLayer.overlapsWithCallback(object1, MapObject::updateTouchingFlagsX);
        }

        // Since we are not separating, always return any amount of overlap => false as last parameter
        return computeOverlapX(object1, object2, false) != 0;
    }

    /**
     * Checking overlap and updating `touching` variables, Y-axis part used by `updateTouchingFlags`.
     *
     * @param object1 Any `MapObject`.
     * @param object2 Any other `MapObject`.
     * @return Whether the objects in fact touched along the Y axis.
     */
    private static boolean updateTouchingFlagsY(MapObject object1, MapObject object2) {
        // If one of the objects is a tilelayer, just pass it off.
        if (object1 instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) object1;
            return tileLayer.overlapsWithCallback(object2, MapObject::updateTouchingFlagsY);
        }
        if (object2 instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) object2;
            return tileLayer.overlapsWithCallback(object1, MapObject::updateTouchingFlagsY);
        }

        // Since we are not separating, always return any amount of overlap => false as last parameter
        return computeOverlapY(object1, object2, false) != 0;
    }

    /**
     * The X-axis component of the object separation process.
     *
     * @param object1 Any `MapObject`.
     * @param object2 Any other `MapObject`.
     * @return Whether the objects in fact touched and were separated along the X axis.
     */
    private static boolean separateX(MapObject object1, MapObject object2) {
        // can't separate two immovable objects
        boolean obj1immovable = object1.immovable;
        boolean obj2immovable = object2.immovable;
        if (obj1immovable && obj2immovable) {
            return false;
        }

        // If one of the objects is a tilelayer, just pass it off.
        if (object1 instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) object1;
            return tileLayer.overlapsWithCallback(object2, MapObject::separateX);
        }
        if (object2 instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) object2;
            return tileLayer.overlapsWithCallback(object1, MapObject::separateX);
        }

        double overlap = computeOverlapX(object1, object2, true);
        // Then adjust their positions and velocities accordingly (if there was any overlap)
        if (overlap != 0.0) {
            double obj1v = object1.velocity.x;
            double obj2v = object2.velocity.x;

            if (!obj1immovable && !obj2immovable) {
                overlap *= 0.5;
                object1.position.x -= overlap;
                object2.position.x += overlap;

                double obj1velocity = Math.sqrt((obj2v * obj2v * object2.mass) / object1.mass) * ((obj2v > 0) ? 1 : -1);
                double obj2velocity = Math.sqrt((obj1v * obj1v * object1.mass) / object2.mass) * ((obj1v > 0) ? 1 : -1);
                double average = (obj1velocity + obj2velocity) * 0.5;
                obj1velocity -= average;
                obj2velocity -= average;
                object1.velocity.x = average + obj1velocity * object1.elasticity;
                object2.velocity.x = average + obj2velocity * object2.elasticity;
            } else if (!obj1immovable) {
                object1.position.x -= overlap;
                object1.velocity.x = obj2v - obj1v * object1.elasticity;
            } else if (!obj2immovable) {
                object2.position.x += overlap;
                object2.velocity.x = obj1v - obj2v * object2.elasticity;
            }

            return true;
        }

        return false;
    }

    /**
     * Internal function that computes overlap among two objects on the X axis. It also updates the `touching` variable.
     * `checkMaxOverlap` is used to determine whether we want to exclude (therefore check) overlaps which are
     * greater than a certain maximum (linked to `SEPARATE_BIAS`). Default is `true`, handy for `separateX` code.
     */
    private static double computeOverlapX(MapObject object1, MapObject object2, boolean checkMaxOverlap) {
        double overlap = 0.0;

        // First, get the two object deltas
        double obj1delta = object1.position.x - object1.last.x;
        double obj2delta = object2.position.x - object2.last.x;

        if (obj1delta != obj2delta) {
            // Check if the X hulls actually overlap
            double obj1deltaAbs = Math.abs(obj1delta);
            double obj2deltaAbs = Math.abs(obj2delta);

            Rectangle2D obj1rect = new Rectangle2D.Double(object1.position.x - ((obj1delta > 0) ? obj1delta : 0), object1.last.y,
                    object1.width + obj1deltaAbs, object1.height);
            Rectangle2D obj2rect = new Rectangle2D.Double(object2.position.x - ((obj2delta > 0) ? obj2delta : 0), object2.last.y,
                    object2.width + obj2deltaAbs, object2.height);

            boolean rectOverlap = (obj1rect.getX() + obj1rect.getWidth() > obj2rect.getX())
                    && (obj1rect.getX() < obj2rect.getX() + obj2rect.getWidth())
                    && (obj1rect.getY() + obj1rect.getHeight() > obj2rect.getY())
                    && (obj1rect.getY() < obj2rect.getY() + obj2rect.getHeight());

            if (rectOverlap) {
                double maxOverlap = checkMaxOverlap ? (obj1deltaAbs + obj2deltaAbs + SEPARATE_BIAS) : 0;

                // If they did overlap (and can), figure out by how much and flip the corresponding flags
                if (obj1delta > obj2delta) {
                    overlap = object1.position.x + object1.width - object2.position.x;
                    if (checkMaxOverlap && (overlap > maxOverlap)) {
                        overlap = 0;
                    } else {
                        object1.touching |= RIGHT;
                        object2.touching |= LEFT;
                    }
                } else if (obj1delta < obj2delta) {
                    overlap = object1.position.x - object2.width - object2.position.x;
                    if (checkMaxOverlap && (-overlap > maxOverlap)) {
                        overlap = 0;
                    } else {
                        object1.touching |= LEFT;
                        object2.touching |= RIGHT;
                    }
                }
            }
        }

        return overlap;
    }

    /**
     * The Y-axis component of the object separation process.
     *
     * @param object1 Any `MapObject`.
     * @param object2 Any other `MapObject`.
     * @return Whether the objects in fact touched and were separated along the Y axis.
     */
    private static boolean separateY(MapObject object1, MapObject object2) {
        // can't separate two immovable objects
        boolean obj1immovable = object1.immovable;
        boolean obj2immovable = object2.immovable;
        if (obj1immovable && obj2immovable) {
            return false;
        }

        // If one of the objects is a tilelayer, just pass it off.
        if (object1 instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) object1;
            return tileLayer.overlapsWithCallback(object2, MapObject::separateY);
        }
        if (object2 instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) object2;
            return tileLayer.overlapsWithCallback(object1, MapObject::separateY);
        }

        double overlap = computeOverlapY(object1, object2, true);
        // Then adjust their positions and velocities accordingly (if there was any overlap)
        if (overlap != 0.0) {
            double obj1delta = object1.position.y - object1.last.y;
            double obj2delta = object2.position.y - object2.last.y;
            double obj1v = object1.velocity.y;
            double obj2v = object2.velocity.y;

            if (!obj1immovable && !obj2immovable) {
                overlap *= 0.5;
                object1.position.y = object1.position.y - overlap;
                object2.position.y += overlap;

                double obj1velocity = Math.sqrt((obj2v * obj2v * object2.mass) / object1.mass) * ((obj2v > 0) ? 1 : -1);
                double obj2velocity = Math.sqrt((obj1v * obj1v * object1.mass) / object2.mass) * ((obj1v > 0) ? 1 : -1);
                double average = (obj1velocity + obj2velocity) * 0.5;
                obj1velocity -= average;
                obj2velocity -= average;
                object1.velocity.y = average + obj1velocity * object1.elasticity;
                object2.velocity.y = average + obj2velocity * object2.elasticity;
            } else if (!obj1immovable) {
                object1.position.y = object1.position.y - overlap;
                object1.velocity.y = obj2v - obj1v * object1.elasticity;

                // This is special case code that handles cases like horizontal moving platforms you can ride
                if (object1.collisionXDrag && object2.moves && (obj1delta > obj2delta)) {
                    object1.position.x += object2.position.x - object2.last.x;
                }
            } else if (!obj2immovable) {
                object2.position.y += overlap;
                object2.velocity.y = obj1v - obj2v * object2.elasticity;

                // This is special case code that handles cases like horizontal moving platforms you can ride
                if (object2.collisionXDrag && object1.moves && (obj1delta < obj2delta)) {
                    object2.position.x += object1.position.x - object1.last.x;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Internal function that computes overlap among two objects on the Y axis. It also updates the `touching` variable.
     * `checkMaxOverlap` is used to determine whether we want to exclude (therefore check) overlaps which are
     * greater than a certain maximum (linked to `SEPARATE_BIAS`). Default is `true`, handy for `separateY` code.
     */
    private static double computeOverlapY(MapObject object1, MapObject object2, boolean checkMaxOverlap) {
        double overlap = 0.0;

        // First, get the two object deltas
        double obj1delta = object1.position.y - object1.last.y;
        double obj2delta = object2.position.y - object2.last.y;

        if (obj1delta != obj2delta) {
            // Check if the Y hulls actually overlap
            double obj1deltaAbs = Math.abs(obj1delta);
            double obj2deltaAbs = Math.abs(obj2delta);

            Rectangle2D obj1rect = new Rectangle2D.Double(object1.position.x, object1.position.y - ((obj1delta > 0) ? obj1delta : 0),
                    object1.width, object1.height + obj1deltaAbs);
            Rectangle2D obj2rect = new Rectangle2D.Double(object2.position.x, object2.position.y - ((obj2delta > 0) ? obj2delta : 0), object2.width,
                    object2.height + obj2deltaAbs);

            boolean rectOverlap = (obj1rect.getX() + obj1rect.getWidth() > obj2rect.getX())
                    && (obj1rect.getX() < obj2rect.getX() + obj2rect.getWidth())
                    && (obj1rect.getY() + obj1rect.getHeight() > obj2rect.getY())
                    && (obj1rect.getY() < obj2rect.getY() + obj2rect.getHeight());
            if (rectOverlap) {
                double maxOverlap = checkMaxOverlap ? (obj1deltaAbs + obj2deltaAbs + SEPARATE_BIAS) : 0;

                // If they did overlap (and can), figure out by how much and flip the corresponding flags
                if (obj1delta > obj2delta) {
                    overlap = object1.position.y + object1.height - object2.position.y;
                    if (checkMaxOverlap && (overlap > maxOverlap)) {
                        overlap = 0;
                    } else {
                        object1.touching |= DOWN;
                        object2.touching |= UP;
                    }
                } else if (obj1delta < obj2delta) {
                    overlap = object1.position.y - object2.height - object2.position.y;
                    if (checkMaxOverlap && (-overlap > maxOverlap)) {
                        overlap = 0;
                    } else {
                        object1.touching |= UP;
                        object2.touching |= DOWN;
                    }
                }
            }
        }

        return overlap;
    }

    /**
     * Override this function to update your class's position and appearance.
     * This is where most of game rules and behavioral code will go.
     */
    @Override
    public void update() {
        last.set(position);

        if (moves) {
            updateMotion();
        }

        wasTouching = touching;
        touching = NONE;
    }

    /**
     * Internal function for updating the position and speed of this object.
     */
    private void updateMotion() {
        double velocityDelta = 0.5 * (EngineVelocity.computeVelocity(velocity.x, acceleration.x, drag.x, maxVelocity.x, Time.deltaTime) - velocity.x);
        velocity.x += velocityDelta;
        var delta = velocity.x * Time.deltaTime;
        velocity.x += velocityDelta;
        position.x += delta;

        velocityDelta = 0.5 * (EngineVelocity.computeVelocity(velocity.y, acceleration.y, drag.y, maxVelocity.y, Time.deltaTime) - velocity.y);
        velocity.y += velocityDelta;
        delta = velocity.y * Time.deltaTime;
        velocity.y += velocityDelta;
        position.y += delta;
    }
}