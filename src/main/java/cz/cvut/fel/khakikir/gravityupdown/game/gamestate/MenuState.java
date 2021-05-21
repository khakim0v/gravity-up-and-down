package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.audio.Sound;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameStateManager;
import cz.cvut.fel.khakikir.gravityupdown.engine.handler.Keys;
import cz.cvut.fel.khakikir.gravityupdown.engine.ui.EngineText;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
    private Backdrop backdrop;

    private EngineText creditsText;
    private EngineText fpsText;

    private boolean isMuted;

    @Override
    public void init() {
        // TBD: Camera fade

        // Background
        backdrop = new Backdrop(Registry.Image.BACKDROP.getPath());
        backdrop.setVelocity(20, 20);

        EngineText titleText = new EngineText(50, 30, -1, "Gravity", 32, Registry.Font.NOKIAFC.getPath());
        EngineText subtitleText = new EngineText(60, 70, -1, "Up&Down", 20, Registry.Font.NOKIAFC.getPath());
        titleText.setShadowSize(3);
        subtitleText.setShadowSize(3);

        add(backdrop);

        add(titleText);
        add(subtitleText);

        Font font = new Font("Arial", Font.PLAIN, 14);
        Font font2 = new Font("Arial", Font.PLAIN, 10);
        String[] options = {
                "Resume Game",
                "Start Game",
                "High Score",
                "Settings",
                "Quit",
        };
        for (int i = 0; i < options.length; i++) {
            EngineText optionText = new EngineText(145, 115 + 20 * i, -1, options[i], font);
            optionText.color = Color.WHITE;
            add(optionText);
        }

        creditsText = new EngineText(10 , 222, -1, "2021 Kirill Khakimov", font2);
        fpsText = new EngineText(260 , 222, -1, "FPS: 0.00", font2);

        add(creditsText);
        add(fpsText);

        Sound musicSnd = Sound.load(Registry.Sound.MUSIC_BIT.getPath());
        musicSnd.loop();
    }

    @Override
    public void update() {
        super.update();
        fpsText.setText(String.format("FPS: %.2f", Engine.averageFps));
    }

    @Override
    public void handleInput() {
        // TODO: REMOVE!
        if (Keys.justPressed(KeyEvent.VK_M)) {
            System.out.println("VK_M was just pressed!");
            isMuted = !isMuted;
            Sound.setMuted(isMuted);
        } else if (Keys.justPressed(KeyEvent.VK_S)) {
            gsm.switchState(new LevelState(gsm));
        }
    }
}
