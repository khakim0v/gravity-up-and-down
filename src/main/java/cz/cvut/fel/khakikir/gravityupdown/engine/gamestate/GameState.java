package cz.cvut.fel.khakikir.gravityupdown.engine.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapGroup;

import java.awt.*;

public abstract class GameState extends MapGroup {
    protected Color bgColor;

    protected GameStateManager gsm;

    public GameStateManager getGameStateManager() {
        return gsm;
    }

    public abstract void init();

    public void update() {
        handleInput();
        super.update();
    }

    public void handleInput() {

    }

    @Override
    public void draw(Graphics2D g) {
        if (bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(0, 0, Engine.width, Engine.height);
        }

        super.draw(g);
    }
}
