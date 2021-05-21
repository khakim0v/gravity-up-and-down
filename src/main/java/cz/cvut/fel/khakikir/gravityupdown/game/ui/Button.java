package cz.cvut.fel.khakikir.gravityupdown.game.ui;

import cz.cvut.fel.khakikir.gravityupdown.engine.asset.audio.Sound;
import cz.cvut.fel.khakikir.gravityupdown.engine.ui.EngineButton;
import cz.cvut.fel.khakikir.gravityupdown.engine.util.Procedure;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

public class Button extends EngineButton {
    public Sound sndClick;
    private final Procedure callback;

    public Button(double x, double y, String text, Procedure callback) {
        super(x, y, Registry.Image.BUTTON.getPath(), text);

        this.sndClick = Sound.load(Registry.Sound.SFX_UI.getPath());
        this.callback = callback;

        this.onDown = this::onClick;
    }

    private void onClick() {
        if (sndClick.isPlaying())
            return;

        sndClick.play();
        sndClick.onComplete = () -> {
            if (callback != null)
                callback.run();
        };
    }
}
