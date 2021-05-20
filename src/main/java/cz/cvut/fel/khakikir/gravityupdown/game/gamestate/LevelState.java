package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.audio.Sound;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapSprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameStateManager;
import cz.cvut.fel.khakikir.gravityupdown.engine.tile.TileLayer;
import cz.cvut.fel.khakikir.gravityupdown.game.LevelLoader;
import cz.cvut.fel.khakikir.gravityupdown.game.input.Input;
import cz.cvut.fel.khakikir.gravityupdown.game.pojo.GameVars;
import cz.cvut.fel.khakikir.gravityupdown.game.pojo.LevelStats;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;
import org.tiledreader.TiledObject;

import java.util.Map;

public class LevelState extends GameState {
    // tilemap layers
    private TileLayer wallLayer;
    private TileLayer bounceLayer;
    private TileLayer spikeLayer;
    private TileLayer decorationLayer;

    // other objects
    private MapObject endZone;

    // player vars
    private MapSprite player;
    private int midJumpCount;
    private int flipCount;
    private int bounceCount;
    private double smashTime;
    private int timeLimit;

    private boolean stopActive;
    private int stopCount;
    private int preStopVelX;

    // sounds
    private Sound sndJump;
    private Sound sndHit;
    private Sound sndDie;
    private Sound sndBounce;
    private Sound sndPowerUpFlip;
    private Sound sndPowerUpSmash;
    private Sound sndPowerUpSavePoint;
    private Sound sndSmash;

    // background and foreground
    private Backdrop backdrop;
    private MapSprite scanlines;

    // hud
    private LevelStateHUD hud;

    // stats
    private LevelStats stats;

    public LevelState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        // Signals
        Engine.focusLost.add(this::showPauseMenu);
        //Engine.focusLost.add(this::autoSave);

        // Load Sounds
        sndJump = Sound.load(Registry.Sound.SFX_JUMP.getPath());
        sndHit = Sound.load(Registry.Sound.SFX_HIT.getPath());
        sndDie = Sound.load(Registry.Sound.SFX_DIE.getPath());
        sndBounce = Sound.load(Registry.Sound.SFX_BOUNCE.getPath());
        sndPowerUpFlip = Sound.load(Registry.Sound.SFX_POWERUP_FLIP.getPath());
        sndPowerUpSmash = Sound.load(Registry.Sound.SFX_POWERUP_SMASH.getPath());
        sndPowerUpSavePoint = Sound.load(Registry.Sound.SFX_POWERUP_SAVEPOINT.getPath());
        sndSmash = Sound.load(Registry.Sound.SFX_SMASH.getPath());

        // Load Sprites
        player = new MapSprite(0, 0);
        player.loadGraphic(Registry.Image.PLAYER.getPath());
        player.width = 16;
        player.height = 16;
        // TBD: load player animation

        // Player Handling
        player.acceleration.set(100, 500);
        player.maxVelocity.set(100, 200);
        player.facing = MapObject.RIGHT;

        // Player Variables
        midJumpCount = 1;
        flipCount = 0;
        bounceCount = 5;
        smashTime = 0.0;
        timeLimit = 0;

        stopActive = false;
        stopCount = 0;
        preStopVelX = 0;

        // Level Objects
        endZone = new MapObject();

        // Load The Map
        // TODO: Use conversion from relative paths to absolute
        LevelLoader.loadTiledMap("D:\\[Work]\\Studium\\Projects\\B0B36PJV\\GravityUpDown\\src\\main\\resources\\data\\level1_test.tmx", this);

        // Camera Setup
        Engine.camera.follow(player);

        // HUD Setup
        stats = new LevelStats(GameVars.LEVEL);

        if (timeLimit == 0) timeLimit = 300;
        stats.levelTimeLimit = timeLimit;

        hud = new LevelStateHUD();
        hud.setScore(0);
        hud.setTime(timeLimit);

        // Background
        backdrop = new Backdrop(Registry.Image.BACKDROP.getPath());
        backdrop.setVelocity(0, 0);

        // Foreground (Scanlines Effect (overlayed))
        scanlines = new MapSprite(0, 0);
        scanlines.loadGraphic(Registry.Image.SCANLINES.getPath());
        scanlines.setAlpha(0.1f);
        scanlines.scrollFactor.set(0, 0);

        // Add things in draw order
        add(backdrop);
        if (wallLayer != null)
            add(wallLayer);
        if (bounceLayer != null)
            add(bounceLayer);
        if (spikeLayer != null)
            add(spikeLayer);
        if (decorationLayer != null)
            add(decorationLayer);

        add(endZone);

        add(player);
        add(scanlines);

        add(hud);

        // Finally!
        Engine.camera.focusOn(player.getMidpoint());
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

    @Override
    public void update() {
        super.update();

        // Collisions
        if (wallLayer != null)
            Engine.collide(wallLayer, player);
        if (bounceLayer != null)
            Engine.collide(bounceLayer, player, this::onBounceHit);
        if (spikeLayer != null)
            Engine.overlap(spikeLayer, player, this::onSpikeHit);
        Engine.collide(endZone, player, this::onEndZoneHit);


        // Player Handling
        player.flipX = (player.facing != MapObject.RIGHT);

        boolean prevTouchingSurface = (player.wasTouching & (MapObject.UP | MapObject.DOWN)) != 0;
        boolean nowTouchingSurface = (player.touching & (MapObject.UP | MapObject.DOWN)) != 0;
        boolean touchingSurface = prevTouchingSurface || nowTouchingSurface;

        if (touchingSurface)
            midJumpCount = 1;

        boolean canJump = (touchingSurface || midJumpCount > 0);
        boolean canFlip = (flipCount > 0);

        if (nowTouchingSurface) {
            // play "walk" animation
            if (!prevTouchingSurface) {
                sndHit.play();
            }
        } else {
            // play "jump" animation
        }

        // flips & jumps
        if (player.alive && (canJump || canFlip) && Input.flipPressed()) {
            if (!touchingSurface) {
                if (!canJump && canFlip) {
                    flipCount--;
                    stats.flipsUsed++;
                    hud.setFlipCount(flipCount);
                } else {
                    stats.airJumps++;
                    midJumpCount--;
                }
            } else {
                stats.jumps++;
            }

            player.velocity.y = 0;
            player.flipY = !player.flipY;

            if (player.acceleration.y == 0) {
                double mult = 1.0;
                if (player.flipY)
                    mult = -mult;
                player.acceleration.y = 500 * mult;
            } else {
                player.acceleration.y *= -1.0;
            }

            // play "jump" animation
            sndJump.play();
        }

        // bounce
        var canBounce = (bounceCount > 0);
        if (canBounce && Input.bouncePressed()) {
            if (playerBounce()) {
                stats.bouncesUsed++;
                bounceCount--;
                hud.setBounceCount(bounceCount);
            }
        }

        // stop player animation
        if (stopActive || (player.velocity.x == 0 && player.velocity.y == 0)) {
            // player.animation.play("stop");
        }

        // Warn player they can't do anything!
        hud.setCannotJumpOrFlip(midJumpCount == 0 && flipCount == 0);

        // Player death if they go off the map
        if (player.position.x <= -player.width || player.position.y <= -player.height ||
                player.position.x >= wallLayer.getWidthInTiles() * wallLayer.getTileWidth() ||
                player.position.y >= wallLayer.getHeightInTiles() * wallLayer.getTileHeight()) {
            doDie();
        }

        // Pause menu activation
        if (player.alive && Input.escapePressed()) {
            showPauseMenu();
        }

        // HUD Score update
        hud.setScore(LevelStats.calculatePoints(stats, false));

        // Level elapsed time update
        if (player.alive)
            stats.elapsedTime += Engine.elapsed;

        // Time limit
        var remaining = timeLimit - stats.elapsedTime;
        if (remaining > 0) {
            hud.setTime((int) (timeLimit - Math.floor(stats.elapsedTime)));
        } else {
            hud.setTime(0);
            doDie();
        }

        // Smash Time
        if (smashTime > 0.0) {
            smashTime -= Engine.elapsed;
            smashTime = Math.max(0.0, smashTime);
        }
    }

    /* Collisions Callbacks */
    private void onBounceHit(MapObject object1, MapObject object2) {
        if (object1 != player && object2 != player)
            return;

        playerBounce();
    }

    private boolean playerBounce() {
        if (!player.alive)
            return false;

        double maxVel = Math.abs(player.maxVelocity.x);

        if (player.facing == MapObject.RIGHT) {
            player.velocity.x = -maxVel;
            player.facing = MapObject.LEFT;
        } else if (player.facing == MapObject.LEFT) {
            player.velocity.x = maxVel;
            player.facing = MapObject.RIGHT;
        }

        player.acceleration.x = -player.acceleration.x;

        stats.bounces++;
        sndBounce.play();

        return true;
    }

    private void onSpikeHit(MapObject object1, MapObject object2) {
        if (smashTime > 0.0) {
            // explode spike
        } else if (player.alive) {
            // death
            doDie();
        }
    }

    private void onEndZoneHit(MapObject object, MapObject object1) {
        if (!player.alive)
            return;

        player.alive = false;
        player.velocity.set(0, 0);

        finishLevel();
    }

    /* Other */
    private void doDie() {
        if (!player.alive)
            return;

        sndDie.play();

        player.alive = false;
        player.velocity.set(0, 0);
        player.acceleration.set(0, 0);

        GameVars.LEVEL_STATS.put(stats.level, stats);
        // fade and then reset level
    }

    private void finishLevel() {
        stats.levelPassed = true;
        GameVars.LEVEL_STATS.put(stats.level, stats);
        GameVars.SCORE += LevelStats.calculatePoints(stats, true);
        //GameVars.LEVEL = nextLevel();

        System.out.printf("Level '%s' finished, starting Level '%s'%n",
                stats.level, GameVars.LEVEL);

        // fade, then switch state
    }

    private void showPauseMenu() {
    }

    /* Level Handling */
    public void handleLevelProperties(Map<String, Object> levelProperties) {
        final String key = "time_limit";
        if (levelProperties.containsKey(key)) {
            timeLimit = Integer.parseInt((String) levelProperties.get(key));
        }
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
        final float width = object.getWidth();
        final float height = object.getHeight();

        switch (object.getName()) {
            case "player" -> {
                player.setPosition(x, y);
                return;
            }
            case "end_zone" -> {
                endZone.setPosition(x, y);
                endZone.width = width;
                endZone.height = height;
                endZone.immovable = true;
                return;
            }
        }

        switch (object.getType()) {
            case "powerup_flip" -> {

            }
        }
    }
}
