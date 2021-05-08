package cz.cvut.fel.khakikir.gravityupdown.engine.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.game.gamestate.MainMenu;

import java.awt.*;

public class GameStateManager {
    private GameState gameState;

    public GameStateManager() {
        this.gameState = new MainMenu(this);
    }

    public void update() {
        if (gameState != null) {
            gameState.update();
        }
    }

    public void draw(Graphics2D g) {
        if (gameState != null) {
            gameState.draw(g);
        }
    }
}
