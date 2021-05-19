package cz.cvut.fel.khakikir.gravityupdown.engine.math;

public class EngineMath {
    private EngineMath() {}

    public static int bound(int value, int min, int max) {
        int lowerBound = Math.max(value, min);
        return Math.min(lowerBound, max);
    }
}
