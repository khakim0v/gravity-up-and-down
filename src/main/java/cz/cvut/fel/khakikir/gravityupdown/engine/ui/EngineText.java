package cz.cvut.fel.khakikir.gravityupdown.engine.ui;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;

// TODO: Add weight
public class EngineText extends MapObject {
    public String text = "";
    private int size;
    private Font font;

    /**
     * Font color
     */
    public Color color = Color.WHITE;

    /**
     * Shadow size
     */
    public int shadowSize = 0;

    /**
     * Creates a new `EngineText` object at the specified position.
     *
     * @param x    The x position of the text.
     * @param y    The y position of the text.
     * @param text The actual text you would like to display initially.
     * @param font The font that will be used to render the text
     */
    public EngineText(double x, double y, String text, Font font) {
        this(x, y, text, font.getSize());

        this.font = font.deriveFont(this.size);
        calculateDimensions();
    }

    /**
     * Creates a new `EngineText` object at the specified position.
     *
     * @param x        The x position of the text.
     * @param y        The y position of the text.
     * @param text     The actual text you would like to display initially.
     * @param size     The font size for this text object.
     * @param fontPath The path to the font that will be used to render the text
     */
    public EngineText(double x, double y, String text, int size, String fontPath) {
        this(x, y, text, size);

        InputStream is = EngineText.class.getResourceAsStream(fontPath);
        if (is != null) {
            try {
                this.font = Font.createFont(Font.TRUETYPE_FONT, is)
                                .deriveFont((float) this.size);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
        }
        calculateDimensions();
    }

    private EngineText(double x, double y, String text, int size) {
        super(x, y);

        if (text != null) {
            this.text = text;
        }
        this.size = size;

        this.moves = false;
    }

    private void calculateDimensions() {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D r = font.getStringBounds(this.text, frc);
        this.width = r.getWidth();
        this.height = r.getHeight();
    }

    @Override
    public void draw(Graphics2D g) {
        // should probably save prev font and restore it later
        g.setFont(font);

        Vec2D screenPosition = getScreenPosition();
        int x = (int) screenPosition.x;
        int y = (int) screenPosition.y;
        if (shadowSize > 0) {
            // draw black shadow
            g.setColor(Color.BLACK);
            for (int i = 1; i <= shadowSize; i++) {
                g.drawString(text, x + i, y + i);
            }
        }

        g.setColor(color);
        g.drawString(text, x, y);
    }
}
