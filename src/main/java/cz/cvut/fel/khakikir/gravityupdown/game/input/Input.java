package cz.cvut.fel.khakikir.gravityupdown.game.input;

import cz.cvut.fel.khakikir.gravityupdown.engine.handler.Keys;

import java.awt.event.KeyEvent;

public final class Input {
    public static boolean flipPressed() {
        return Keys.justPressed(KeyEvent.VK_SPACE);
    }

    private Input() {

    }
}
