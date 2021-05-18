package cz.cvut.fel.khakikir.gravityupdown.engine.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.game.gamestate.MainMenu;

import java.awt.*;

public class GameStateManager {
    private GameState currentState;

    public GameStateManager() {
        this.currentState = new MainMenu(this); }

    public void setState(GameState state) {
        currentState = state;
    }

    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }

    public void draw(Graphics2D g) {
        if (currentState != null) {
            currentState.draw(g);
        }
    }
}
