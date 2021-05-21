package cz.cvut.fel.khakikir.gravityupdown.game.main;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Camera;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameStateManager;
import cz.cvut.fel.khakikir.gravityupdown.engine.handler.EngineInput;
import cz.cvut.fel.khakikir.gravityupdown.engine.util.EngineSave;
import cz.cvut.fel.khakikir.gravityupdown.game.gamestate.MenuState;
import cz.cvut.fel.khakikir.gravityupdown.game.pojo.GameVars;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, FocusListener {
    private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName());

    private static final int WINDOW_WIDTH = 320;
    private static final int WINDOW_HEIGHT = 240;
    public static final int WINDOW_SCALE = 3;

    private static final int FPS = 60;
    private static final long FRAME_TARGET_TIME = 1000 / FPS; // in milliseconds

    private Thread thread;
    private boolean running; // TODO: Should be volatile?

    // for off-screen rendering
    private BufferedImage bufferedImage;
    private Graphics2D graphics;

    private GameStateManager gsm;

    public GamePanel() {
        setPreferredSize(new Dimension(WINDOW_WIDTH * WINDOW_SCALE, WINDOW_HEIGHT * WINDOW_SCALE));
        setFocusable(true);
        requestFocus();
        addFocusListener(this);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            addKeyListener(this);
            addMouseListener(this);
            thread = new Thread(this);
            thread.start();
        }
    }

    private void initialize() {
        LOGGER.info("GamePanel initialization");
        bufferedImage = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
        graphics = (Graphics2D) bufferedImage.getGraphics();
        setCustomCursor();

        // Init Engine variables
        Engine.width = WINDOW_WIDTH;
        Engine.height = WINDOW_HEIGHT;
        Engine.camera = new Camera(GamePanel.WINDOW_WIDTH, GamePanel.WINDOW_HEIGHT);

        // Load save from file
        tryLoadSave();

        // Init GameStateManager
        gsm = new GameStateManager(new MenuState());

        running = true;
    }

    private void setCustomCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            InputStream stream = GamePanel.class.getResourceAsStream("/images/cursor.png");
            BufferedImage image = ImageIO.read(stream);
            Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), "Pixel Cursor");
            setCursor(cursor);
        } catch (IOException e) {
            LOGGER.warning("Can't load custom cursor");
        }
    }

    private void tryLoadSave() {
        attachAutoSave();
        if (GameVars.AUTOSAVE.get("active") != null && Boolean.parseBoolean(GameVars.AUTOSAVE.get("active"))) {
            GameVars.SCORE = Integer.parseInt(GameVars.AUTOSAVE.get("score"));
            GameVars.LEVEL = Integer.parseInt(GameVars.AUTOSAVE.get("level"));
            LOGGER.info("Found active AUTOSAVE. Loading it...");

            GameVars.RESUMED = true;
        } else {
            GameVars.RESUMED = false;
        }
    }

    private static void attachAutoSave() {
        if (GameVars.SAVEPOINT != null) {
            return;
        }

        LOGGER.info("Creating and binding AUTOSAVE");
        GameVars.AUTOSAVE = new EngineSave();
        GameVars.AUTOSAVE.bind("gravity-autosave");
    }

    public static void autoSave()
    {
        LOGGER.info(String.format("Saving AUTOSAVE: Score = %s,  Level Index = %s", GameVars.SCORE, GameVars.LEVEL));
        GameVars.AUTOSAVE.put("active", Boolean.toString(true));
        GameVars.AUTOSAVE.put("level", Integer.toString(GameVars.LEVEL));
        GameVars.AUTOSAVE.put("score",  Integer.toString(GameVars.SCORE));
        GameVars.AUTOSAVE.flush();
    }

    public static void clearAutoSave()
    {
        LOGGER.info("Clearing AUTOSAVE");
        GameVars.AUTOSAVE.put("active", Boolean.toString(false));
        GameVars.AUTOSAVE.put("level", Integer.toString(0));
        GameVars.AUTOSAVE.put("score",  Integer.toString(0));
        GameVars.AUTOSAVE.flush();
    }

    @Override
    public void run() {
        LOGGER.info("GamePanel Thread started");
        initialize();

        // time and timestamp variables in nanoseconds
        long currentTime = System.nanoTime();
        final double dt = 1.0 / 60.0;

        LOGGER.info("Game loop starting");
        while (running) { // game loop
            long newTime = System.nanoTime();
            double sinceLastFrameTime = (double) (newTime - currentTime) / 1_000_000_000;

            // Calculate FPS
            double alpha = 0.9;
            Engine.instantFps = 1_000_000_000.0d / (newTime - currentTime);
            Engine.averageFps = alpha * Engine.averageFps + (1.0 - alpha) * Engine.instantFps;

            currentTime = newTime;

            while (sinceLastFrameTime > 0.0) {
                double deltaTime = Math.min(sinceLastFrameTime, dt);
                update(deltaTime);
                sinceLastFrameTime -= deltaTime;
            }

            draw();

            long frameTime = System.nanoTime() - newTime;
            long waitTime = FRAME_TARGET_TIME - (frameTime / 1_000_000);
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    LOGGER.severe("Game loop sleep interrupted");
                }
            }
        }
    }

    /**
     * Method to update the physics.
     *
     * @param delta The delta time in seconds.
     */
    private void update(double delta) {
        Engine.elapsed = delta;
        gsm.update();
        Engine.camera.update();
        EngineInput.update();
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
        EngineInput.setKeyState(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        EngineInput.setKeyState(e.getKeyCode(), false);
    }

    /* Mouse events (MouseListener methods) */
    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        point.x /= WINDOW_SCALE;
        point.y /= WINDOW_SCALE;
        EngineInput.setMouseClickPosition(point);
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

    @Override
    public void focusGained(FocusEvent e) {
        Engine.focusGained.handle();
    }

    @Override
    public void focusLost(FocusEvent e) {
        Engine.focusLost.handle();;
    }
}
