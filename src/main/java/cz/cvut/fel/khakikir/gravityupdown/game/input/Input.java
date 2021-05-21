package cz.cvut.fel.khakikir.gravityupdown.game.input;

import cz.cvut.fel.khakikir.gravityupdown.engine.handler.EngineInput;

import java.awt.event.KeyEvent;

public final class Input {
    private Input() {

    }

    public static boolean flipPressed() {
        return EngineInput.justPressed(KeyEvent.VK_SPACE);
    }

    public static boolean bouncePressed() {
        return EngineInput.justPressed(KeyEvent.VK_CONTROL);
    }

    public static boolean escapePressed() {
        return EngineInput.justPressed(KeyEvent.VK_ESCAPE);
    }

    public static boolean skipPressed() {
        return EngineInput.justPressed(KeyEvent.VK_SPACE);
    }
}
