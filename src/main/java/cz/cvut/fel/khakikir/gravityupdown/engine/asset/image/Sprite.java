package cz.cvut.fel.khakikir.gravityupdown.engine.asset.image;

import cz.cvut.fel.khakikir.gravityupdown.engine.asset.ResourceException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Sprite {
    public static BufferedImage loadImage(String path) {
        try {
            InputStream is = Sprite.class.getResourceAsStream(path);
            if (is != null) {
                return ImageIO.read(is);
            } else {
                throw new ResourceException(String.format("No resource with name %s is found", path));
            }
        } catch (Exception e) {
            throw new ResourceException(e);
        }
    }
}
