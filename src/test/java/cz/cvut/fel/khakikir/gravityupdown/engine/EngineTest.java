package cz.cvut.fel.khakikir.gravityupdown.engine;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EngineTest {
    MapObject object1;
    MapObject object2;
    MapObject object3;

    @BeforeAll
    static void beforeAll() {
        Engine.elapsed = 1.0 / 60.0;
    }

    @BeforeEach
    void setUp() {
        object1 = new MapObject();
        object1.setPosition(10, 10);
        object1.width = 20;
        object1.height = 40;

        object2 = new MapObject();
        object2.setPosition(20, 20);
        object2.width = 30;
        object2.height = 10;

        object3 = new MapObject();
        object3.setPosition(55, 55);
        object3.width = 40;
        object3.height = 40;

        object1.update();
        object2.update();
        object3.update();
    }

    @Test
    void collide_ObjectsCollideThenSuccessfullySeparated() {
        Assertions.assertTrue(Engine.collide(object1, object2));
        Assertions.assertFalse(Engine.collide(object1, object2));
    }

    @Test
    void collide_ObjectsDoNotCollide() {
        Assertions.assertFalse(Engine.collide(object2, object3));
    }

    @Test
    void overlap_ObjectsDoNotOverlapWhenTheyDidNotMove() {
        object1.update();
        object2.update();
        object3.update();

        Assertions.assertFalse(Engine.overlap(object1, object2));
        Assertions.assertFalse(Engine.overlap(object1, object3));
    }

    @Test
    void overlap_ObjectsOverlapWhenTheyMoved() {
        object1.setVelocity(0.1, 0.1);
        object2.setVelocity(0.1, 0.1);
        object3.setVelocity(0.1, 0.1);

        object1.update();
        object2.update();
        object3.update();

        Assertions.assertTrue(Engine.overlap(object1, object2));
        Assertions.assertFalse(Engine.overlap(object1, object3));
    }
}