package cz.cvut.fel.khakikir.gravityupdown.engine.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapGroup;

public abstract class GameState extends MapGroup {
    protected GameStateManager gsm;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;

        init();
    }

    public GameStateManager getGameStateManager() {
        return gsm;
    }

    public abstract void init();

    public void update() {
        handleInput();
        super.update();
    }

    public abstract void handleInput();
}
