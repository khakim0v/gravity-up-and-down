package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.audio.Sound;
import cz.cvut.fel.khakikir.gravityupdown.engine.effects.EngineFlicker;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapGroup;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapSprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.tile.TileLayer;
import cz.cvut.fel.khakikir.gravityupdown.game.LevelLoader;
import cz.cvut.fel.khakikir.gravityupdown.game.entity.PowerUp;
import cz.cvut.fel.khakikir.gravityupdown.game.entity.PowerUpType;
import cz.cvut.fel.khakikir.gravityupdown.game.input.Input;
import cz.cvut.fel.khakikir.gravityupdown.game.pojo.GameVars;
import cz.cvut.fel.khakikir.gravityupdown.game.pojo.LevelStats;
import cz.cvut.fel.khakikir.gravityupdown.game.pojo.SavePoint;
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
    private MapGroup powerUpGroup;

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

    private SavePoint savePoint;

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

    @Override
    public void init() {
        // Signals
        //Engine.focusLost.add(this::showPauseMenu);
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
        // TODO: load player animation

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

        savePoint = GameVars.SAVEPOINT;

        // Level Objects
        endZone = new MapObject();
        powerUpGroup = new MapGroup();

        // Load The Map
        // TODO: Use conversion from relative paths to absolute
        String levelMapPath = GameVars.LEVELS[GameVars.LEVEL].getPath();
        LevelLoader.loadTiledMap(levelMapPath, this);

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


        add(powerUpGroup);
        add(endZone);

        add(player);
        add(scanlines);

        add(hud);

        // Finally!
        boolean restored = savePointRestore();
        Engine.camera.focusOn(player.getMidpoint());
    }

    @Override
    public void handleInput() {


//        double velocity = 100;
//        if (EngineInput.isPressed(KeyEvent.VK_LEFT)) {
//            player.velocity.x = -velocity;
//        } else if (EngineInput.isPressed(KeyEvent.VK_RIGHT)) {
//            player.velocity.x = velocity;
//        } else {
//            player.velocity.x = 0;
//        }
//
//        if (EngineInput.isPressed(KeyEvent.VK_UP)) {
//            player.velocity.y = -velocity;
//        } else if (EngineInput.isPressed(KeyEvent.VK_DOWN)) {
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

        if (powerUpGroup != null)
            Engine.overlap(powerUpGroup, player, this::onPowerUpHit);

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
            // move through spikes
            // TBD: explode spike
            // stats.spikesSmashed++;
            // sndSmash.play();
        } else if (player.alive) {
            // death
            doDie();
        }
    }

    private void onPowerUpHit(MapObject object1, MapObject object2) {
        if (object1 instanceof PowerUp) {
            PowerUp powerUp = (PowerUp) object1;
            powerUp.kill();

            switch (powerUp.getType()) {
                case Flip -> {
                    stats.flipsCollected += 5;
                    flipCount = flipCount + 5;
                    hud.setFlipCount(flipCount);
                    sndPowerUpFlip.play();
                }
                case Bounce -> {
                    bounceCount++;
                    stats.bouncesCollected++;
                    hud.setBounceCount(bounceCount);
                    sndPowerUpFlip.play();
                }
                case Smash -> {
                    smashTime = 5.0;
                    EngineFlicker.flicker(spikeLayer, smashTime, 0.04);
                    sndPowerUpSmash.play();
                }
                case Savepoint -> {
                    savePointSave();
                    sndPowerUpSavePoint.play();
                }
                case Stop -> {
                    stopCount++;
                    stats.stopsCollected++;
                    hud.setStopCount(stopCount);
                    sndPowerUpSavePoint.play();
                }
            }
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

        GameVars.LEVEL_STATS[stats.level] = stats;
        // TODO: fade and then (after some time) reset level
        // keep save point around so we can restore it
        GameVars.SAVEPOINT = savePoint;
        gsm.resetCurrentState();
    }

    private void finishLevel() {
        stats.levelPassed = true;
        GameVars.LEVEL_STATS[stats.level] = stats;
        GameVars.SCORE += LevelStats.calculatePoints(stats, true);
        GameVars.LEVEL++;

        System.out.printf("Level '%s' finished, starting Level '%s'%n",
                stats.level, GameVars.LEVEL);
        GameVars.SAVEPOINT = null;

        // TODO: Autosave

        // TODO: fade, then switch state
        var state = new LevelStatsState(stats, () -> {
            var end = GameVars.LEVEL == GameVars.LEVELS.length;
            if (end) {
                //Engine.focusLost.remove(Game.autoSave);
                //Game.autoSaveClear();
            }

            gsm.switchState(end ? new MenuState() : new LevelState());
        });

        gsm.switchState(state);
    }

    /* Signals */
    private void showPauseMenu() {
        // PlayStateMenu
    }

    private void onRestart() {
        player.alive = false;
        GameVars.SAVEPOINT = null; // invalidate
        // TODO: Add fade timeout
        gsm.resetCurrentState();
    }

    private void onLeave() {
        player.active = false;
        GameVars.SAVEPOINT = null; // invalidate

        // Engine.focusLost.remove(this::autoSave);
        // game.autoSaveClear();

        gsm.switchState(new MenuState());
    }

    /* Save Points Handling */
    private void savePointSave() {
        if (savePoint == null)
            savePoint = new SavePoint();

        savePoint.position.set(player.position.x, player.position.y);
        savePoint.velocity.set(player.velocity.x, player.velocity.y);
        savePoint.acceleration.set(player.acceleration.x, player.acceleration.y);
        savePoint.flipY = player.flipY;
        savePoint.facing = player.facing;
        savePoint.timeElapsed = stats.elapsedTime;
    }

    private boolean savePointRestore() {
        if (savePoint == null)
            return false;

        player.facing = savePoint.facing;
        player.flipY = savePoint.flipY;
        player.acceleration.set(savePoint.acceleration.x, savePoint.acceleration.y);
        player.velocity.set(savePoint.velocity.x, savePoint.velocity.y);
        player.setPosition(savePoint.position.x, savePoint.position.y);

        stats.elapsedTime = savePoint.timeElapsed;

        return true;
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
            // TODO: Text and Platform
            case "powerup_flip" -> powerUpGroup.add(new PowerUp(x, y, PowerUpType.Flip));
            case "powerup_bounce" -> powerUpGroup.add(new PowerUp(x, y, PowerUpType.Bounce));
            case "powerup_smash" -> powerUpGroup.add(new PowerUp(x, y, PowerUpType.Smash));
            case "powerup_save" -> powerUpGroup.add(new PowerUp(x, y, PowerUpType.Savepoint));
            case "powerup_stop" -> powerUpGroup.add(new PowerUp(x, y, PowerUpType.Stop));
        }
    }
}
