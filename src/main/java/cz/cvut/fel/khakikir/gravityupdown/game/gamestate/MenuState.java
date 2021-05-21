package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.audio.Sound;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.ui.EngineText;
import cz.cvut.fel.khakikir.gravityupdown.game.input.Input;
import cz.cvut.fel.khakikir.gravityupdown.game.ui.Button;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

import java.awt.*;

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

        EngineText titleText = new EngineText(0, 20, 0, 0, "Gravity", 32, Registry.Font.NOKIAFC.getPath());
        titleText.screenCenter(true, false);
        titleText.setShadowSize(3);

        EngineText subtitleText = new EngineText(0, 60, 0, 0, "Up&Down", 20, Registry.Font.NOKIAFC.getPath());
        subtitleText.screenCenter(true, false);
        subtitleText.setShadowSize(3);

        add(backdrop);

        add(titleText);
        add(subtitleText);

        int btnX = 0;
        int btnY = 110;
        int btnOffsetY = 24;
        Button btnStart = new Button(btnX, btnY + btnOffsetY * 0, "Start Game", true, this::onStartGame);
        Button btnHighScore = new Button(btnX, btnY + btnOffsetY * 1, "High Score", false, this::onHighScore);
        Button btnSettings = new Button(btnX, btnY + btnOffsetY * 2, "Settings", false, this::onSettings);
        Button btnQuit = new Button(btnX, btnY + btnOffsetY * 3, "Quit", true, this::onQuit);

        btnStart.screenCenter(true, false);
        btnHighScore.screenCenter(true, false);
        btnSettings.screenCenter(true, false);
        btnQuit.screenCenter(true, false);

        add(btnStart);
        add(btnHighScore);
        add(btnSettings);
        add(btnQuit);

        Font font2 = new Font("Arial", Font.PLAIN, 10);

        creditsText = new EngineText(10, 222, 0, 0, "2021 Kirill Khakimov", font2);
        fpsText = new EngineText(260, 222, 0, 0, "FPS: 0.00", font2);

        add(creditsText);
        add(fpsText);

        Sound musicSnd = Sound.load(Registry.Sound.MUSIC_BIT.getPath());
        musicSnd.loop();
    }

    @Override
    public void update() {
        super.update();
        fpsText.setText(String.format("FPS: %.2f", Engine.averageFps));

        if (Input.escapePressed()) {
            onQuit();
        }
    }

    @Override
    public void handleInput() {

    }

    /* Buttons callbacks */
    private void onStartGame() {
        gsm.switchState(new InstructionsState());
    }

    public void onHighScore() {

    }

    public void onSettings() {

    }

    private void onQuit() {
        // TODO: Add fade
        System.exit(0);
    }
}
