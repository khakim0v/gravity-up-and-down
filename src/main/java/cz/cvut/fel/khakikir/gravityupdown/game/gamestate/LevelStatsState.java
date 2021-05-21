package cz.cvut.fel.khakikir.gravityupdown.game.gamestate;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.Backdrop;
import cz.cvut.fel.khakikir.gravityupdown.engine.gamestate.GameState;
import cz.cvut.fel.khakikir.gravityupdown.engine.ui.EngineText;
import cz.cvut.fel.khakikir.gravityupdown.engine.util.EngineTimer;
import cz.cvut.fel.khakikir.gravityupdown.engine.util.Procedure;
import cz.cvut.fel.khakikir.gravityupdown.game.pojo.GameVars;
import cz.cvut.fel.khakikir.gravityupdown.game.pojo.LevelStats;
import cz.cvut.fel.khakikir.gravityupdown.game.ui.Button;
import cz.cvut.fel.khakikir.gravityupdown.game.ui.LabeledValueText;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

import java.util.ArrayList;
import java.util.List;

public class LevelStatsState extends GameState {
    private LevelStats stats;
    private Procedure onContinue;

    public LevelStatsState(LevelStats stats, Procedure onContinue) {
        this.stats = stats;
        this.onContinue = onContinue;
    }

    @Override
    public void init() {
        var backdrop = new Backdrop(Registry.Image.BACKDROP.getPath());
        backdrop.scrollFactor.set(0, 0);
        add(backdrop);

        var titleString = String.format("Completed Level %s of %s", stats.level + 1, GameVars.LEVELS.length);
        var title = new EngineText(0, 0, 0, 0, titleString, 16, Registry.Font.NOKIAFC.getPath());
        title.setShadowSize(1);
        title.screenCenter(true, false);
        title.position.y = 12;
        add(title);

        List<LabeledValueText> values = new ArrayList<>();
        values.add(new LabeledValueText("Time:", stats.elapsedTime));
        values.add(new LabeledValueText("Air Flips:", stats.airJumps));
        values.add(new LabeledValueText("Jumps:", stats.jumps));
        values.add(new LabeledValueText("Bounces:", stats.bounces));
        values.add(new LabeledValueText("Flips Collected:", stats.flipsCollected));
        values.add(new LabeledValueText("Flips Saved:", stats.flipsCollected - stats.flipsUsed));
        values.add(new LabeledValueText("Bounces Collected:", stats.bouncesCollected));
        values.add(new LabeledValueText("Bounces Saved:", stats.bouncesCollected - stats.bouncesUsed));
        values.add(new LabeledValueText("Stops Collected:", stats.stopsCollected));
        values.add(new LabeledValueText("Stops Saved:", stats.stopsCollected - stats.stopsUsed));
        values.add(new LabeledValueText("Spikes Smashed:", stats.spikesSmashed));

        var points = LevelStats.calculatePoints(stats, true);
        var pointsText = new LabeledValueText("Points:", points);
        pointsText.offsetY = 10;
        values.add(pointsText);

        var xLbl = 10;
        var xPos = 120;
        var yPos = 40;
        var delay = 0.5;

        for (var val : values) {
            if (val.value == 0)
                continue;

            xPos += val.offsetX;
            yPos += val.offsetY;

            val.text.position.x = xPos;
            val.text.position.y = yPos;
            add(val.text);

            val.label.position.x = xLbl;
            val.label.position.y = yPos;
            val.label.visible = false;
            add(val.label);

            new EngineTimer(null).start(delay, timer -> {
                val.label.visible = true;
                timer.cancel();
            }, 0);

            delay += 0.5;
            yPos += 20;
        }

        var btn = new Button(10, 10, "Continue", onContinue);
        btn.position.x = Engine.width - btn.width - 10;
        btn.position.y = Engine.height - btn.height - 10;
        add(btn);
    }

    @Override
    public void handleInput() {

    }
}
