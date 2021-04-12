package cz.cvut.fel.khakikir.gravityupdown.engine.gamestate;

import java.awt.*;

public class GameStateManager {
    private GameState gameState;

    public void update(long delta) {
        if (gameState != null) {
            gameState.update(delta);
        }
    }

    public void draw(Graphics2D g) {
        if (gameState != null) {
            gameState.draw(g);
        }
    }
}
