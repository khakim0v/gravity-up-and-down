package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Time;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameStateManager;
import cz.cvut.fel.khakikir.gravityupdown.game.main.GamePanel;

import java.awt.*;

public class MainMenu extends GameState {
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

    public MainMenu(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        backdrop = new Backdrop("/images/backdrop.png");
        backdrop.setVelocity(20, 20);
        titleColor = Color.WHITE;
        titleFont = new Font("Times New Roman", Font.PLAIN, 28);
        font = new Font("Arial", Font.PLAIN, 14);
        font2 = new Font("Arial", Font.PLAIN, 10);
    }

    @Override
    public void update() {
        handleInput();
        backdrop.update();
    }

    @Override
    public void draw(Graphics2D g) {
        backdrop.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Gravity Up&Down", 50, 90);

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

    }
}
