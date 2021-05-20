package cz.cvut.fel.khakikir.gravityupdown.engine.ui;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

// TODO: Add weight
public class EngineText extends MapObject {
    public Color color = Color.WHITE;
    public Alignment alignment = Alignment.LEFT;
    public boolean antialiasing = false;

    /**
     * Change the size of your font's graphic.
     */
    public Vec2D scale; // TODO: Implement text scaling

    private String text = "";
    private final int size;
    private Font font;

    private final double fieldWidth;
    private int shadowSize = 0;

    // for off-screen rendering
    private BufferedImage bufferedImage;
    private Graphics2D graphics;

    /**
     * Creates a new `EngineText` object at the specified position.
     *
     * @param x          The x position of the text.
     * @param y          The y position of the text.
     * @param fieldWidth The `width` of the text object.
     * @param text       The actual text you would like to display initially.
     * @param font       The font that will be used to render the text
     */
    public EngineText(double x, double y, double fieldWidth, String text, Font font) {
        this(x, y, fieldWidth, text, font.getSize());

        this.font = font.deriveFont(this.size);
        recalculateDimensions();
    }

    /**
     * Creates a new `EngineText` object at the specified position.
     *
     * @param x          The x position of the text.
     * @param y          The y position of the text.
     * @param fieldWidth The `width` of the text object.
     * @param text       The actual text you would like to display initially.
     * @param size       The font size for this text object.
     * @param fontPath   The path to the font that will be used to render the text
     */
    public EngineText(double x, double y, double fieldWidth, String text, int size, String fontPath) {
        this(x, y, fieldWidth, text, size);

        InputStream is = EngineText.class.getResourceAsStream(fontPath);
        if (is != null) {
            try {
                this.font = Font.createFont(Font.TRUETYPE_FONT, is)
                        .deriveFont((float) this.size);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
        }
        recalculateDimensions();
    }

    private EngineText(double x, double y, double fieldWidth, String text, int size) {
        super(x, y);
        this.scale = new Vec2D(1, 1);
        this.fieldWidth = fieldWidth;

        if (text != null) {
            this.text = text;
        }
        this.size = size;

        this.moves = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        recalculateDimensions();
    }

    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
        recalculateDimensions();
    }

    private void recalculateDimensions() {
        Rectangle2D r = getTextBounds();
        this.width = this.fieldWidth > 0 ? this.fieldWidth : r.getWidth() + shadowSize + 10; // FIXME: magic constant
        this.height = r.getHeight() + shadowSize;

        this.bufferedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
        this.graphics = bufferedImage.createGraphics();
    }

    private Rectangle2D getTextBounds() {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        return font.getStringBounds(this.text, frc);
    }

    @Override
    public void draw(Graphics2D g) {
        renderOffScreen();

        Vec2D screenPosition = getScreenPosition();
        int x = (int) screenPosition.x;
        int y = (int) screenPosition.y;

        g.drawImage(bufferedImage, x, y, null);
    }

    public void renderOffScreen() {
        graphics.setFont(font);
        Object hintValue = antialiasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, hintValue);

        int x = 0;
        int y = 0;

        // Shift the text down by the ascender height
        FontMetrics metrics = graphics.getFontMetrics(font);
        int offset = metrics.getAscent();
        y += offset;

        if (alignment == Alignment.RIGHT) {
            Rectangle2D r = getTextBounds();
            x += width - r.getWidth();
        }

        if (shadowSize > 0) {
            // draw black shadow
            graphics.setColor(Color.BLACK);
            for (int i = 1; i <= shadowSize; i++) {
                graphics.drawString(text, x + i, y + i);
            }
        }

        graphics.setColor(color);
        graphics.drawString(text, x, y);
    }

    public enum Alignment {
        LEFT, RIGHT
    }
}
