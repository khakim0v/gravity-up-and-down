package cz.cvut.fel.khakikir.gravityupdown.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {
    private static final int WINDOW_WIDTH = 320;
    private static final int WINDOW_HEIGHT = 240;
    private static final int WINDOW_SCALE = 1;

    private Thread thread;
    private boolean running; // TODO: Should be volatile?
    private static final int FPS = 60;
    private static final long FRAME_TARGET_TIME = 1000 / FPS; // in milliseconds

    // for off-screen rendering
    private BufferedImage bufferedImage;
    private Graphics2D graphics;

    private GameStateManager gsm;

    public GamePanel() {
        setPreferredSize(new Dimension(WINDOW_WIDTH * WINDOW_SCALE, WINDOW_HEIGHT * WINDOW_SCALE));
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        start();
    }

    private void start() {
        if (thread == null) {
            addKeyListener(this);
            addMouseListener(this);
            thread = new Thread(this);
            thread.start();
        }
    }

    private void initialize() {
        System.out.println("GamePanel initialization");
        bufferedImage = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
        graphics = (Graphics2D) bufferedImage.getGraphics();
        gsm = new GameStateManager();
        running = true;
    }

    @Override
    public void run() {
        System.out.println("GamePanel Thread started");
        initialize();

        // time and timestamp variables in nanoseconds
        long lastTimestamp = System.nanoTime();
        long currentTimestamp;
        long waitTime;
        long deltaTime;
        long frameTime;

        System.out.println("Game loop starting");
        while (running) { // game loop
            currentTimestamp = System.nanoTime();
            deltaTime = currentTimestamp - lastTimestamp; // Should it be the time from the end of last frame?
            handle(currentTimestamp, deltaTime);
            frameTime = System.nanoTime() - currentTimestamp;
            lastTimestamp = currentTimestamp;
            
            waitTime = FRAME_TARGET_TIME - (frameTime / 1_000_000);
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    System.out.println("Game loop sleep interrupted");
                }
            }
        }
    }

    /**
     * This method is going to be called in every frame.
     *
     * @param now   The timestamp of the current frame given in nanoseconds.
     * @param delta The elapsed time from the previous frame in nanoseconds.
     */
    private void handle(long now, long delta) {
        // float instant_fps = 1_000_000_000.0f / delta;
        // System.out.println(instant_fps);
        update(delta);
        draw();
    }

    private void update(long delta) {
        gsm.update(delta);
        Keys.update();
    }

    private void draw() {
        gsm.draw(graphics); // render frame off-screen
        repaint(); // calls paintComponent(Graphics g)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0,
                WINDOW_WIDTH * WINDOW_SCALE, WINDOW_HEIGHT * WINDOW_SCALE,
                null);
    }

    /* Keyboard events (KeyListener methods) */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Keys.setState(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Keys.setState(e.getKeyCode(), false);
    }


    /* Mouse events (MouseListener methods) */
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
