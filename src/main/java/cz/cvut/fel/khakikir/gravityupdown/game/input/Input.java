package cz.cvut.fel.khakikir.gravityupdown.game.input;

import cz.cvut.fel.khakikir.gravityupdown.engine.handler.Keys;

import java.awt.event.KeyEvent;

public final class Input {
    private Input() {

    }

    public static boolean flipPressed() {
        return Keys.justPressed(KeyEvent.VK_SPACE);
    }

    public static boolean bouncePressed() {
        return Keys.justPressed(KeyEvent.VK_CONTROL);
    }

    public static boolean escapePressed() {
        return Keys.justPressed(KeyEvent.VK_ESCAPE);
    }
}
