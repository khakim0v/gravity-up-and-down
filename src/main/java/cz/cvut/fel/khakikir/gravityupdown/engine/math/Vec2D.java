package cz.cvut.fel.khakikir.gravityupdown.engine.math;

/**
 * Representation of 2D vectors and points.
 */
public class Vec2D {
    public double x;
    public double y;

    public Vec2D() {
    }

    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2D(Vec2D v) {
        set(v);
    }

    /**
     * Returns the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the distance between the two sets of specified
     * coordinates.
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    /**
     * Returns the square of the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the square of the distance between the two
     * sets of specified coordinates.
     */
    public static double distanceSq(double x1, double y1, double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return (x1 * x1 + y1 * y1);
    }

    /**
     * Sets the x and y values of this vector
     *
     * @param x value in x
     * @param y value in y
     * @return this vector for chaining
     */
    public Vec2D set(double x, double y) {
        this.x = x;
        this.y = y;

        return this;
    }

    /**
     * Sets the x and y values of this vector to match input vector 'v'
     *
     * @param v v contains values to copy
     * @return this vector for chaining
     */
    public Vec2D set(Vec2D v) {
        return set(v.x, v.y);
    }

    /**
     * Adds the given values to this vector.
     *
     * @param x value in x
     * @param y value in y
     * @return new vector
     */
    public Vec2D add(double x, double y) {
        return new Vec2D(this.x + x, this.y + y);
    }

    /**
     * Adds the given vector 'v' to this vector.
     *
     * @param v vector to add
     * @return new vector
     */
    public Vec2D add(Vec2D v) {
        return add(v.x, v.y);
    }

    /**
     * Subtracts the given values from this vector.
     *
     * @param x value in x to subtract
     * @param y value in y to subtract
     * @return new vector
     */
    public Vec2D sub(double x, double y) {
        return new Vec2D(this.x - x, this.y - y);
    }

    /**
     * Subtracts the given vector 'v' from this vector.
     *
     * @param v vector to subtract
     * @return new vector
     */
    public Vec2D sub(Vec2D v) {
        return sub(v.x, v.y);
    }

    /**
     * Scales this vector's values by a constant.
     *
     * @param scalar constant to scale this vector's values by
     * @return new vector
     */
    public Vec2D scale(double scalar) {
        return new Vec2D(this.x * scalar, this.y * scalar);
    }

    /**
     * Calculates the remainder after dividing each element in a vector by
     * the corresponding parameter.
     *
     * @param x value in x to subtract
     * @param y value in y to subtract
     * @return new vector
     */
    public Vec2D mod(double x, double y) {
        return new Vec2D(this.x % x, this.y % y);
    }

    /**
     * Returns the distance from this {@code Vec2D} to
     * a specified point.
     *
     * @param vx the X coordinate of the specified point to be measured
     *           against this {@code Vec2D}
     * @param vy the Y coordinate of the specified point to be measured
     *           against this {@code Vec2D}
     * @return the distance between this {@code Vec2D}
     * and a specified point.
     */
    public double distance(double vx, double vy) {
        vx -= x;
        vy -= y;
        return Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * Returns the distance from this {@code Vec2D} to a
     * specified {@code Vec2D}.
     *
     * @param v the specified point to be measured
     *          against this {@code Vec2D}
     * @return the distance between this {@code Vec2D} and
     * the specified {@code Vec2D}.
     */
    public double distance(Vec2D v) {
        double vx = v.x - this.x;
        double vy = v.y - this.y;
        return Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * Returns the square of the distance from this
     * {@code Vec2D} to a specified point.
     *
     * @param vx the X coordinate of the specified point to be measured
     *           against this {@code Vec2D}
     * @param vy the Y coordinate of the specified point to be measured
     *           against this {@code Vec2D}
     * @return the square of the distance between this
     * {@code Vec2D} and the specified point.
     */
    public double distanceSq(double vx, double vy) {
        vx -= x;
        vy -= y;
        return (vx * vx + vy * vy);
    }

    /**
     * Returns the square of the distance from this
     * {@code Vec2D} to a specified {@code Vec2D}.
     *
     * @param v the specified point to be measured
     *          against this {@code Vec2D}
     * @return the square of the distance between this
     * {@code Vec2D} to a specified {@code Vec2D}.
     */
    public double distanceSq(Vec2D v) {
        double vx = v.x - this.x;
        double vy = v.y - this.y;
        return (vx * vx + vy * vy);
    }

    /**
     * Returns the hashcode for this {@code Vec2D}.
     *
     * @return a hash code for this {@code Vec2D}.
     */
    @Override
    public int hashCode() {
        long bits = 7L;
        bits = 31L * bits + Double.doubleToLongBits(x);
        bits = 31L * bits + Double.doubleToLongBits(y);
        return (int) (bits ^ (bits >> 32));
    }

    /**
     * Determines whether or not two 2D points or vectors are equal.
     * Two instances of {@code Vec2D} are equal if the values of their
     * {@code x} and {@code y} member fields, representing
     * their position in the coordinate space, are the same.
     *
     * @param obj an object to be compared with this {@code Vec2D}
     * @return {@code true} if the object to be compared is
     * an instance of {@code Vec2D} and has
     * the same values; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec2D) {
            Vec2D v = (Vec2D) obj;
            return (x == v.x) && (y == v.y);
        }
        return false;
    }

    /**
     * Returns a {@code String} that represents the value
     * of this {@code Vec2D}.
     *
     * @return a string representation of this {@code Vec2D}.
     */
    @Override
    public String toString() {
        return "Vec2D[" + x + ", " + y + "]";
    }
}
