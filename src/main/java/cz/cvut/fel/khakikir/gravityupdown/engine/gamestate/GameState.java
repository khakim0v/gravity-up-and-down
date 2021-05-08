package cz.cvut.fel.khakikir.gravityupdown.engine.gamestate;

import java.awt.*;

public abstract class GameState {
    protected GameStateManager gsm;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public GameStateManager getGameStateManager() {
        return gsm;
    }

    public abstract void init();
    public abstract void update();
    public abstract void draw(Graphics2D g);
    public abstract void handleInput();
}
