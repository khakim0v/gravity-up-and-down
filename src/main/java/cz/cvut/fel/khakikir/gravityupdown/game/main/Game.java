package cz.cvut.fel.khakikir.gravityupdown.game.main;

import cz.cvut.fel.khakikir.gravityupdown.game.util.Strings;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        JFrame window = new JFrame(Strings.GAME_TITLE);
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null); // Set the window in the center of the screen
        window.setVisible(true);
    }
}
