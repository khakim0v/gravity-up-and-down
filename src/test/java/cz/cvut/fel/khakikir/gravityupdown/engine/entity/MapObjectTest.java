package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapObjectTest {
    MapObject mapObject;

    @BeforeEach
    void setUp() {
        mapObject = new MapObject();
    }

    @Test
    void setPosition() {
        double x = 120;
        double y = 220;
        mapObject.setPosition(x, y);

        Assertions.assertEquals(mapObject.position.x, x);
        Assertions.assertEquals(mapObject.position.y, y);
    }

    @Test
    void setVelocity() {
        double x = 10;
        double y = -20;
        mapObject.setVelocity(x, y);

        Assertions.assertEquals(mapObject.velocity.x, x);
        Assertions.assertEquals(mapObject.velocity.y, y);
    }

    @Test
    void getMidpoint() {
        double x = 120;
        double y = 220;
        mapObject.setPosition(x, y);

        double width = 145;
        double height = 40;
        mapObject.width = width;
        mapObject.height = height;

        Vec2D midpoint = mapObject.getMidpoint();

        Assertions.assertEquals(midpoint.x, x + width * 0.5);
        Assertions.assertEquals(midpoint.y, y + height * 0.5);
    }
}