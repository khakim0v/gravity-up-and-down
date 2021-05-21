package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapSprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.util.EngineTimer;
import cz.cvut.fel.khakikir.gravityupdown.game.input.Input;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

import java.awt.*;

public class InstructionsState extends GameState {

    @Override
    public void init() {
        this.bgColor = Color.BLACK;

        var screen = new MapSprite(0, 0, Registry.Image.INSTRUCTIONS.getPath());
        screen.screenCenter(true, true);
        add(screen);

        new EngineTimer(null).start(5, timer -> {
            doClose();
        }, 0);
    }

    @Override
    public void update() {
        super.update();

        if (Input.skipPressed()) {
            doClose();
        }
    }

    private void doClose() {
        gsm.switchState(new LevelState());
    }
}
