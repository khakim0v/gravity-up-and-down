package cz.cvut.fel.khakikir.gravityupdown.engine.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStateManagerTest {
    private GameState gameState;

    @BeforeAll
    static void beforeAll() {
        Engine.elapsed = 1.0 / 60.0;
    }

    @BeforeEach
    void setUp() {
        gameState = new GameState() {
            @Override
            public void init() {

            }
        };
    }

    @Test
    void constructor_ChangesCurrentStateToStateInParameter() {
        GameStateManager gsm = new GameStateManager(gameState);
        Assertions.assertEquals(gameState, gsm.getCurrentState());
    }

    @Test
    void switchState_ChangesCurrentState() {
        GameStateManager gsm = new GameStateManager(gameState);
        Assertions.assertEquals(gameState, gsm.getCurrentState());

        GameState gameState2 = new TestGameState();

        gsm.switchState(gameState2);
        Assertions.assertEquals(gameState2, gsm.getCurrentState());
    }

    @Test
    void resetCurrentState_createsNewInstanceOfCurrentState() {
        gameState = new TestGameState();
        GameStateManager gsm = new GameStateManager(gameState);
        Assertions.assertEquals(gameState, gsm.getCurrentState());

        gsm.resetCurrentState();
        Assertions.assertNotEquals(gameState, gsm.getCurrentState());           // another object
        assertEquals(gameState.getClass(), gsm.getCurrentState().getClass());   // but the same type
    }

    @Test
    void update_CallsUpdateOnCurrentState() {
        Wrapper<Boolean> isUpdateCalled = new Wrapper<>(false);

        gameState = new GameState() {
            @Override
            public void init() {

            }

            @Override
            public void update() {
                isUpdateCalled.setValue(true);
                super.update();
            }
        };

        GameStateManager gsm = new GameStateManager(gameState);
        gsm.update();

        Assertions.assertTrue(isUpdateCalled.getValue());
    }

    private static class TestGameState extends GameState {
        public TestGameState() {
        }

        @Override
        public void init() {
            // nothing
        }
    }

    private static class Wrapper<T> {
        T value;

        public Wrapper(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}