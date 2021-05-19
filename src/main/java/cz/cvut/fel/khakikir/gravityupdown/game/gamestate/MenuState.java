package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Time;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.audio.AudioPlayer;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameStateManager;
import cz.cvut.fel.khakikir.gravityupdown.engine.handler.Keys;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;

public class MenuState extends GameState {
    private final String[] options = {
            "Resume Game",
            "Start Game",
            "High Score",
            "Settings",
            "Quit",
    };

    private Backdrop backdrop;

    private Color titleColor;
    private Font titleFont;
    private Font font;
    private Font font2;

    private boolean isMuted;

    public MenuState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        // Camera fade
        backdrop = new Backdrop("/images/backdrop.png");
        backdrop.setVelocity(20, 20);
        titleColor = Color.WHITE;

        // Move somewhere
        InputStream is = MenuState.class.getResourceAsStream("/fonts/nokiafc22.ttf");
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(32.0f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        font = new Font("Arial", Font.PLAIN, 14);
        font2 = new Font("Arial", Font.PLAIN, 10);

        AudioPlayer.load(Registry.Sound.MUSIC_BIT.name(), Registry.Sound.MUSIC_BIT.getPath());
        // AudioPlayer.loop(SFX.MUSIC_BIT.getName());
    }

    @Override
    public void update() {
        super.update();
        backdrop.update();
    }

    @Override
    public void draw(Graphics2D g) {
        backdrop.draw(g);

        // draw title
        g.setFont(titleFont);
        g.setColor(Color.BLACK);
        g.drawString("Gravity", 50+1, 60+1);
        g.drawString("Gravity", 50+2, 60+2);
        g.drawString("Gravity", 50+3, 60+3);
        g.setColor(Color.WHITE);
        g.drawString("Gravity", 50, 60);

        g.setFont(titleFont.deriveFont(20.0f));
        g.setColor(Color.BLACK);
        g.drawString("Up&Down", 60+1, 90+1);
        g.drawString("Up&Down", 60+2, 90+2);
        g.drawString("Up&Down", 60+3, 90+3);
        g.setColor(Color.WHITE);
        g.drawString("Up&Down", 60, 90);

        // draw menu options
        g.setFont(font);
        g.setColor(Color.WHITE);
        for (int i = 0; i < options.length; i++) {
            g.drawString(options[i], 145, 125 + 20 * i);
        }

        // other
        g.setFont(font2);
        g.drawString("2021 Kirill Khakimov", 10, 232);
        g.drawString(String.format("FPS: %.2f", Time.averageFps), 260, 232);
    }

    @Override
    public void handleInput() {
        // TODO: REMOVE!
        if (Keys.justPressed(KeyEvent.VK_M)) {
            System.out.println("VK_M was just pressed!");
            isMuted = !isMuted;
            AudioPlayer.setMuted(isMuted);
        } else if (Keys.justPressed(KeyEvent.VK_S)) {
            gsm.setState(new LevelState(gsm));
        }
    }
}
