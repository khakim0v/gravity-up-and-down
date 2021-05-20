package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.math.CallbackVec2D;
import cz.cvut.fel.khakikir.gravityupdown.engine.math.Vec2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpriteGroup extends MapObject {
    private final List<MapObject> members;
    public CallbackVec2D scrollFactor;

    public SpriteGroup() {
        this.members = new ArrayList<>();
        scrollFactor = new CallbackVec2D(this::scrollFactorCallback);
    }

    /**
     * Adds a new `MapObject` subclass (`MapObject`, `MapSprite`, `Object`, etc) to the group.
     * `MapGroup` will try to replace a `null` member of the list first.
     * Failing that, `FlxGroup` will add it to the end of the member array.
     *
     * @param object The object you want to add to the group.
     */
    public void add(MapObject object) {
        if (object == null) return;

        // Don't bother adding an object twice.
        if (members.contains(object)) return;

        object.scrollFactor.set(scrollFactor);
        // First, look for a null entry where we can add the object.
        int index = members.indexOf(null);
        if (index != -1) {
            members.set(index, object);
            return;
        }

        members.add(object);
    }

    /**
     * Removes an object from the group.
     *
     * @param object The `MapObject` you want to remove.
     * @param splice Whether the object should be cut from the array entirely or not.
     */
    public void remove(MapObject object, boolean splice) {
        int index = members.indexOf(object);

        if (index < 0) return;

        if (splice) {
            members.remove(index);
        } else {
            members.set(index, null);
        }
    }

    private void scrollFactorCallback(Vec2D scrollFactor) {
        for (MapObject member : members) {
            if (member != null) {
                member.scrollFactor.set(scrollFactor);
            }
        }
    }

    /**
     * Automatically goes through and calls render on everything you added.
     */
    @Override
    public void update() {
        for (MapObject member : members) {
            if (member != null && member.active) {
                member.update();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        for (MapObject member : members) {
            if (member != null && member.visible) {
                member.draw(g);
            }
        }
    }
}
