package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapSprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.SpriteGroup;
import cz.cvut.fel.khakikir.gravityupdown.engine.ui.EngineText;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

import java.awt.*;

public class LevelStateHUD extends SpriteGroup {
    private static final int MARGIN_TOP = 5;
    private static final int MARGIN_SIDE = 5;
    private static final int FONT_SIZE = 8;

    private int scoreValue;
    private final EngineText scoreText;
    //private final EngineText flipText;
    private int timeSecs;
    private final EngineText timeText;
    private int flips;
    private final MapSprite barBg;
    private final MapSprite barTicks;
    private final MapSprite barTickGreen;
    private final MapSprite barGfx;
    private int barCount;

    public LevelStateHUD() {
        scrollFactor.set(0, 0);

        // TODO: Bounce Power-Up Button
        // TODO: Stop Power-Up Button

        setBounceCount(0);
        setStopCount(0);

        barBg = new MapSprite(MARGIN_SIDE, MARGIN_TOP, Registry.Image.HUD_BAR.getPath());
        barTickGreen = new MapSprite(MARGIN_SIDE, MARGIN_SIDE, Registry.Image.HUD_BAR_TICK_GREEN.getPath());
        barTicks = new MapSprite(0, 0, Registry.Image.HUD_BAR_TICKS.getPath());
        barGfx = new MapSprite(MARGIN_SIDE, MARGIN_TOP);
        barGfx.makeGraphic(Math.floor(barTicks.width), Math.floor(barTicks.height));
        barCount = 0;

        setFlipCount(0);

        MapSprite timeIcon = new MapSprite(0, 0, Registry.Image.TIMER.getPath());
        timeIcon.position.x = Engine.width - 60;
        timeIcon.position.y = MARGIN_TOP + 1;

        timeText = new EngineText(((double) Engine.width / 4) * 3, MARGIN_TOP,
                100, "0", 16, Registry.Font.NOKIAFC.getPath());
        timeText.alignment = EngineText.Alignment.RIGHT;
        timeText.setShadowSize(1);
        timeText.scale.set(1.8, 1.8);
        timeText.position.x = Engine.width - MARGIN_SIDE - timeText.width;
        timeText.position.y = MARGIN_TOP;

        scoreText = new EngineText(((double) Engine.width / 4) * 3, MARGIN_TOP,
                100, "", 16, Registry.Font.NOKIAFC.getPath());
        scoreText.alignment = EngineText.Alignment.RIGHT;
        scoreText.setShadowSize(1);
        scoreText.scale.set(1.8, 1.8);
        scoreText.position.x = timeText.position.x;
        scoreText.position.y = timeText.position.y + 24;

        scoreValue = -1;
        timeSecs = 0;

        add(scoreText);

        add(timeIcon);
        add(timeText);

        add(barBg);
        add(barTickGreen);
        add(barGfx);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
    }

    public void setScore(int amount) {
        if (scoreValue != amount) {
            scoreText.setText(String.valueOf(amount));
            scoreValue = amount;
        }
    }

    public void setTime(int seconds) {
        if (seconds != timeSecs) {
            timeSecs = seconds;
            timeText.setText(String.valueOf(timeSecs));

//            if (seconds == 10) {
//                // Text tween
//            } else if (seconds == 5) {
//                // Text tween
//            }
        }
    }

    public void setCannotJumpOrFlip(boolean cannotFlip) {
        barTickGreen.visible = !cannotFlip;
    }

    public void setBounceCount(int amount) {
        var canBounce = (amount > 0);
        //bounceBtn.visible = canBounce;
        //bounceBtn.active = canBounce;
    }

    public void setStopCount(int amount) {
        var canStop = (amount > 0);
        //stopBtn.visible = canStop;
        //stopBtn.active = canStop;
    }

    public void setFlipCount(int amount) {
        flips = amount;
        // TODO: Uncomment after implementing flipText
        //flipText.setText(String.valueOf(amount));
        updateBarGfx(amount);
    }

    private void updateBarGfx(double amount) {
        int barCount = (int) Math.floor(amount);
        int width = (int) Math.floor(Math.min(amount, 8) * 8);

        Graphics2D g = barGfx.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f ));
        g.fillRect(0, 0, (int) barGfx.width, (int) barGfx.height);
        g.drawImage(barTicks.getImage(),
                0,0, width, (int) barGfx.height,
                0,0, width, (int) barGfx.height, null);
    }
}
