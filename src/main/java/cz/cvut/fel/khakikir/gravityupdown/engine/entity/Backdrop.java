package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.Time;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Used for showing infinitely scrolling backgrounds.
 */
public class Backdrop extends MapObject {
    private BufferedImage image;

    private final int width;
    private final int height;

    public Backdrop(String path) {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void update() {
        double x = (position.getX() + velocity.getX() * Time.deltaTime) % width;
        double y = (position.getY() + velocity.getY() * Time.deltaTime) % height;
        setPosition(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        int x = (int) position.getX();
        int y = (int) position.getY();
        // 1 2
        // 3 4
        g.drawImage(image, 0, 0, x, y, width - x, height - y, width, height, null);
        g.drawImage(image, x, 0, width, y,0 , height - y, width - x, height, null);
        g.drawImage(image, 0, y, x, height,width - x , 0, width, height - y, null);
        g.drawImage(image, x, y, width, height,0 , 0, width - x, height - y, null);
    }
}
