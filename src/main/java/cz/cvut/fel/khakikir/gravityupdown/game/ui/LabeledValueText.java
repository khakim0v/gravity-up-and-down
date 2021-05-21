package cz.cvut.fel.khakikir.gravityupdown.game.ui;

import cz.cvut.fel.khakikir.gravityupdown.engine.ui.EngineText;

public class LabeledValueText {
    public EngineText label;
    public EngineText text;
    public int value;

    public int offsetX;
    public int offsetY;

    private LabeledValueText(String label, String value) {
        this.label = new EngineText(0, 0, 0, 0, 8, label);
        this.text = new EngineText(0, 0, 0, 0, 8, value);
    }

    public LabeledValueText(String label, int value) {
        this(label, String.valueOf(value));
        this.value = value;
    }

    public LabeledValueText(String label, double value) {
        this(label, String.valueOf((int) value));
        this.value = (int) value;
    }
}
