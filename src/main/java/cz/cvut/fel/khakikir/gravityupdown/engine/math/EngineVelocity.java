package cz.cvut.fel.khakikir.gravityupdown.engine.math;

public class EngineVelocity {
    private EngineVelocity() {
    }

    /**
     * A tween-like function that takes a starting velocity and some other factors and returns an altered velocity.
     *
     * @param velocity     Any component of velocity (e.g. 20).
     * @param acceleration Rate at which the velocity is changing.
     * @param drag         Really kind of a deceleration, this is how much the velocity changes if Acceleration is not set.
     * @param max          An absolute value cap for the velocity (0 for no cap).
     * @param elapsed      The amount of time passed in to the latest update cycle
     * @return The altered velocity value.
     */
    public static double computeVelocity(double velocity, double acceleration, double drag, double max, double elapsed) {
        if (acceleration != 0) {
            velocity += acceleration * elapsed;
        } else if (drag != 0) {
            drag *= elapsed;
            if (velocity - drag > 0) {
                velocity -= drag;
            } else if (velocity + drag < 0) {
                velocity += drag;
            } else {
                velocity = 0;
            }
        }
        if ((velocity != 0) && (max != 0)) {
            if (velocity > max) {
                velocity = max;
            } else if (velocity < -max) {
                velocity = -max;
            }
        }

        return velocity;
    }
}
