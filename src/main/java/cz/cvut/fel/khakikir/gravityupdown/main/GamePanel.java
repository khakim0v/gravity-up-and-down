package cz.cvut.fel.khakikir.gravityupdown.main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 240;
    private static final int SCALE = 1;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }
}
