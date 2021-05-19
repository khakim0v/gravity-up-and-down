package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.image.Sprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Dummy;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapSprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameStateManager;
import cz.cvut.fel.khakikir.gravityupdown.engine.tile.TileLayer;
import cz.cvut.fel.khakikir.gravityupdown.game.LevelLoader;
import cz.cvut.fel.khakikir.gravityupdown.game.main.GamePanel;
import org.tiledreader.TiledObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class LevelState extends GameState {
    // tilemap layers
    private TileLayer wallLayer;
    private TileLayer bounceLayer;
    private TileLayer spikeLayer;
    private TileLayer decorationLayer;

    // player vars
    private MapSprite player;

    // background and foreground
    private Backdrop backdrop;
    private BufferedImage scanlines;

    private MapObject dummy;

    // TODO: Move to Sprite class
    private AlphaComposite acTransparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
    private AlphaComposite acNormal = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);

    public LevelState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        // Load Sprites
        player = new MapSprite(0, 0);
        player.loadGraphic("/images/player_single.png");
        player.width = 16;
        player.height = 16;

        // Player Handling
        player.acceleration.set(-20, 500);
        player.maxVelocity.set(100, 200);
        player.facing = MapObject.LEFT;

        // Load The Map
        // TODO: Use conversion from relative paths to absolute
        LevelLoader.loadTiledMap("D:\\[Work]\\Studium\\Projects\\B0B36PJV\\GravityUpDown\\src\\main\\resources\\data\\level1_test.tmx", this);

        // Camera Setup
        dummy = new Dummy();
        dummy.setPosition(GamePanel.WINDOW_WIDTH / 2.0, GamePanel.WINDOW_HEIGHT / 2.0);
        dummy.setVelocity(240, 240);
        Engine.camera.follow(player);

        // Init Background
        backdrop = new Backdrop("/images/backdrop.png");
        backdrop.setVelocity(0, 0);

        // Init Foreground (Scanlines Effect (overlayed))
        scanlines = Sprite.loadImage("/images/scanlines.png");

        // Finally!
        Engine.camera.focusOn(player.getMidpoint());
    }

    @Override
    public void update() {
        super.update();
        backdrop.update();
        player.update();

        // Collisions
        if (wallLayer != null) {
            Engine.collide(wallLayer, player);
        }

        if (bounceLayer != null) {
            Engine.collide(bounceLayer, player, this::onBounceHit);
        }

        if (spikeLayer != null) {
            Engine.overlap(spikeLayer, player, this::onSpikeHit);
        }

        // Player Handling
        player.flipX = (player.facing != MapObject.RIGHT);
    }

    private void onBounceHit(MapObject object1, MapObject object2) {
        if (object1 != player && object2 != player)
            return;

        double maxVel = Math.abs(player.maxVelocity.x);

        if (player.facing == MapObject.RIGHT) {
            player.velocity.x = -maxVel;
            player.facing = MapObject.LEFT;
        } else if (player.facing == MapObject.LEFT) {
            player.velocity.x = maxVel;
            player.facing = MapObject.RIGHT;
        }

        player.acceleration.x = -player.acceleration.x;

        // TODO:
        // stats.bounces++;
        // sndBounce.play(true);
    }

    private void onSpikeHit(MapObject object1, MapObject object2) {
        var x = 2;
    }

    @Override
    public void draw(Graphics2D g) {
        backdrop.draw(g);
        wallLayer.draw(g);
        bounceLayer.draw(g);
        spikeLayer.draw(g);
        decorationLayer.draw(g);
        player.draw(g);
        drawScanlines(g);
    }

    private void drawScanlines(Graphics2D g) {
        g.setComposite(acTransparent);
        g.drawImage(scanlines, 0, 0, null);
        g.setComposite(acNormal);
    }

    @Override
    public void handleInput() {
//        double velocity = 100;
//        if (Keys.isPressed(KeyEvent.VK_LEFT)) {
//            player.velocity.x = -velocity;
//        } else if (Keys.isPressed(KeyEvent.VK_RIGHT)) {
//            player.velocity.x = velocity;
//        } else {
//            player.velocity.x = 0;
//        }
//
//        if (Keys.isPressed(KeyEvent.VK_UP)) {
//            player.velocity.y = -velocity;
//        } else if (Keys.isPressed(KeyEvent.VK_DOWN)) {
//            player.velocity.y = velocity;
//        } else {
//            player.velocity.y = -0;
//        }
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
                player.setPosition(x, y);
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
