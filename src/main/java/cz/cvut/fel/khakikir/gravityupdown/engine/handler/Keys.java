package cz.cvut.fel.khakikir.gravityupdown.engine.handler;

import java.util.HashMap;

public class Keys {
    private static final HashMap<Integer, Boolean> states;
    private static final HashMap<Integer, Boolean> prevStates;

    static {
        states = new HashMap<>();
        prevStates = new HashMap<>();
    }

    private Keys() {
    }

    public static void setState(int keyCode, boolean isPressed) {
        states.put(keyCode, isPressed);
    }

    public static void update() {
        prevStates.putAll(states);
    }

    public static boolean wasJustPressed(Integer keyCode) {
        return states.getOrDefault(keyCode, false) &&
                !prevStates.getOrDefault(keyCode, false);
    }

    public static boolean isPressed(Integer keyCode) {
        return states.getOrDefault(keyCode, false);
    }
}
