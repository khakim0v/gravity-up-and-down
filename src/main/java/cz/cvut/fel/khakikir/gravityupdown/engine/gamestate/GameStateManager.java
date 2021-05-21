package cz.cvut.fel.khakikir.gravityupdown.engine.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Camera;
import cz.cvut.fel.khakikir.gravityupdown.engine.util.EngineTimer;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GameStateManager {
    private GameState currentState;

    public GameStateManager(GameState state) {
        switchState(state);
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void switchState(GameState state) {
        // Reset timers
        EngineTimer.globalManager.clear();
        Engine.camera = new Camera(Engine.width, Engine.height);

        // Init state
        state.init();

        // Finally assign and create the new state
        state.gsm = this;
        currentState = state;
    }

    /**
     * Request a reset of the current game state.
     * Calls `switchState()` with a new instance of the current `state`.
     */
    public void resetCurrentState() {
        GameState state = null;
        try {
            Constructor<?> constructor = currentState.getClass().getConstructors()[0];
            state = (GameState) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        assert state != null;
        switchState(state);
    }

    public void update() {
        if (currentState != null) {
            // Update timers
            EngineTimer.globalManager.update();
            // Update state
            currentState.update();
        }
    }

    public void draw(Graphics2D g) {
        if (currentState != null) {
            currentState.draw(g);
        }
    }
}
