package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.Time;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.image.Sprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Dummy;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameStateManager;
import cz.cvut.fel.khakikir.gravityupdown.engine.handler.Keys;
import cz.cvut.fel.khakikir.gravityupdown.engine.tile.TileLayer;
import cz.cvut.fel.khakikir.gravityupdown.game.LevelLoader;
import cz.cvut.fel.khakikir.gravityupdown.game.main.GamePanel;
import org.tiledreader.TiledObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Map;

public class LevelState extends GameState {
    // tilemap layers
    private TileLayer wallLayer;
    private TileLayer bounceLayer;
    private TileLayer spikeLayer;
    private TileLayer decorationLayer;

    // background and foreground
    private Backdrop backdrop;
    private BufferedImage scanlines;

    private MapObject player;
    private MapObject endZone;

    private MapObject dummy;

    // TODO: Move to Sprite class
    private AlphaComposite acTransparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
    private AlphaComposite acNormal = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);

    public LevelState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        // TODO: Use conversion from relative paths to absolute
        LevelLoader.loadTiledMap("D:\\[Work]\\Studium\\Projects\\B0B36PJV\\GravityUpDown\\src\\main\\resources\\data\\level1_test.tmx", this);

        backdrop = new Backdrop("/images/backdrop.png");
        backdrop.setVelocity(0, 0);

        scanlines = Sprite.loadImage("/images/scanlines.png");

        dummy = new Dummy();
        dummy.setPosition(GamePanel.WINDOW_WIDTH / 2.0, GamePanel.WINDOW_HEIGHT / 2.0);
        dummy.setVelocity(240, 240);
        Engine.camera.follow(dummy);
    }

    @Override
    public void update() {
        super.update();
        backdrop.update();
    }

    @Override
    public void draw(Graphics2D g) {
        backdrop.draw(g);
        wallLayer.draw(g);
        bounceLayer.draw(g);
        spikeLayer.draw(g);
        decorationLayer.draw(g);
        drawScanlines(g);
    }

    private void drawScanlines(Graphics2D g) {
        g.setComposite(acTransparent);
        g.drawImage(scanlines, 0, 0, null);
        g.setComposite(acNormal);
    }

    @Override
    public void handleInput() {
        if (Keys.isPressed(KeyEvent.VK_LEFT)) {
            dummy.position.x -= dummy.velocity.x * Time.deltaTime;
        } else if (Keys.isPressed(KeyEvent.VK_RIGHT)) {
            dummy.position.x += dummy.velocity.x * Time.deltaTime;
        }

        if (Keys.isPressed(KeyEvent.VK_UP)) {
            dummy.position.y -= dummy.velocity.y * Time.deltaTime;
        } else if (Keys.isPressed(KeyEvent.VK_DOWN)) {
            dummy.position.y += dummy.velocity.y * Time.deltaTime;
        }
    }

    public void handleLevelProperties(Map<String, Object> levelProperties) {
        System.out.println(levelProperties);
    }

    public void handleLevelLayer(TileLayer tileLayer, String tileLayerName) {
        switch (tileLayerName) {
            case "map" -> wallLayer = tileLayer;
            case "bounce" -> bounceLayer = tileLayer;
            case "spikes" -> spikeLayer = tileLayer;
            case "decoration" -> decorationLayer = tileLayer;
        }
    }

    public void handleLevelObject(TiledObject object) {
        final float x = object.getX();
        final float y = object.getY();

        switch (object.getName()) {
            case "player" -> {
                //player.setPosition(x, y);
                return;
            }
            case "end_zone" -> {
                // TODO: Set width, height
                //endZone.setPosition(x, y);
                return;
            }
        }

        switch (object.getType()) {
            case "powerup_flip" -> {

            }
        }
    }
}
