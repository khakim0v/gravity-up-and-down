package cz.cvut.fel.khakikir.gravityupdown.engine.handler;

import java.awt.*;
import java.util.HashMap;

public class EngineInput {
    private static final HashMap<Integer, Boolean> keysStates;
    private static final HashMap<Integer, Boolean> keysPrevStates;
    private static Point mouseClickPosition = null;

    static {
        keysStates = new HashMap<>();
        keysPrevStates = new HashMap<>();
    }

    private EngineInput() {
    }

    public static void setKeyState(int keyCode, boolean isPressed) {
        keysStates.put(keyCode, isPressed);
    }

    public static void setMouseClickPosition(Point point) {
        mouseClickPosition = point;
    }

    public static void update() {
        keysPrevStates.putAll(keysStates);
        mouseClickPosition = null;
    }

    public static boolean justPressed(Integer keyCode) {
        return keysStates.getOrDefault(keyCode, false) &&
                !keysPrevStates.getOrDefault(keyCode, false);
    }

    public static boolean isPressed(Integer keyCode) {
        return keysStates.getOrDefault(keyCode, false);
    }

    public static boolean mouseJustPressed() {
        return mouseClickPosition != null;
    }

    public static Point getMouseClickPosition() {
        return mouseClickPosition;
    }
}
