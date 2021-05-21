package cz.cvut.fel.khakikir.gravityupdown.engine.ui;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapSprite;
import cz.cvut.fel.khakikir.gravityupdown.engine.handler.EngineInput;
import cz.cvut.fel.khakikir.gravityupdown.engine.util.Procedure;

import java.awt.*;

public class EngineButton extends MapSprite {
    private EngineText label;
    protected Procedure onDown;

    public EngineButton(double x, double y, String path, String text) {
        super(x, y, path);

        // Since this is a UI element, the default scrollFactor is (0, 0)
        scrollFactor.set(0, 0);

        this.label = new EngineText(x, y, width, height, 8, text);
        this.label.alignment = EngineText.Alignment.CENTER;
        this.label.borderSize = 1;
        this.label.position = position;
    }

    /**
     * Called by the game loop automatically, handles mouseover and click detection.
     */
    @Override
    public void update() {
        super.update();

        if (visible) {
            if (EngineInput.mouseJustPressed()) {
                Point point = EngineInput.getMouseClickPosition();
                boolean overlaps = getBounds().contains(point);
                if (overlaps) {
                    onDown.run();
                }
            }
        }
    }

    /**
     * Just draws the button graphic and text label to the screen.
     */
    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        if (label != null && label.visible) {
            label.draw(g);
        }
    }
}
