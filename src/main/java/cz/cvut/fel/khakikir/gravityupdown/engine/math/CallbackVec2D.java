package cz.cvut.fel.khakikir.gravityupdown.engine.math;

import java.util.function.Consumer;

public class CallbackVec2D extends Vec2D {
    private double x;
    private double y;

    private Consumer<Vec2D> callback;

    public CallbackVec2D(Consumer<Vec2D> callback) {
        this.callback = callback;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Sets the x and y values of this vector and calls the callback
     *
     * @param x value in x
     * @param y value in y
     * @return this vector for chaining
     */
    public Vec2D set(double x, double y) {
        this.x = x;
        this.y = y;

        callCallback();
        return this;
    }

    /**
     * Sets the x and y values of this vector to match input vector 'v'
     *
     * @param v v contains values to copy
     * @return this vector for chaining
     */
    public Vec2D set(Vec2D v) {
        set(v.x, v.y);

        callCallback();
        return this;
    }

    private void callCallback() {
        if (callback != null)
            callback.accept(this);
    }
}
